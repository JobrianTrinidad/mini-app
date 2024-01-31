package com.aat.application.data.repository;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.entity.ZJTItem;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class BaseEntityRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public BaseEntityRepository() {

    }

    @SuppressWarnings("unchecked")
    public List<ZJTItem> findByQuery(String query) {
//        String newQuery = query + " From " + entityClass.getSimpleName() + " as p";
//        String newQuery = "SELECT p.timelineItemTitle as title, p.planDate as startDate FROM " + entityClass.getSimpleName() + " AS p";
        Query customQuery = entityManager.createQuery(query);
        List<Object[]> results = customQuery.getResultList();
        List<ZJTItem> items = new ArrayList<>();
        for (Object[] result : results) {
            String title = "new item";
            String groupID = null;
            LocalDateTime startDate = LocalDateTime.now();
            if (result[0] != null) {
                title = (String) result[0];
            }
            if (result[1] != null) {
                groupID = String.valueOf(((ZJTEntity) result[1]).getId());
            }
            if (result[2] != null) {
                startDate = (LocalDateTime) result[2];
                ZJTItem item = new ZJTItem(title, groupID, startDate);
                items.add(item);
            }
        }
        return items;
    }

    public List<Object[]> findEntityByQuery(String query) {
        Query customQuery = entityManager.createQuery(query);
        return customQuery.getResultList();
    }

    @Transactional
    public int updateEntityByQuery(String query, Object[] params) {
        Query customQuery = entityManager.createQuery(query);
        String paramType = customQuery.getParameter("param1").getParameterType().getSimpleName();
        switch (paramType) {
            case "Boolean":
            case "boolean":
                customQuery.setParameter("param1", Boolean.parseBoolean(params[2].toString()));
                break;
            case "LocalDateTime":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a", Locale.ENGLISH);
                customQuery.setParameter("param1", LocalDateTime.parse(params[2].toString(), formatter));
                break;
            default:
                customQuery.setParameter("param1", params[2]);
                break;
        }

        if (customQuery.getParameter("param1").getParameterType().equals(String.class))
            customQuery.setParameter("param1", params[2]);
        customQuery.setParameter("param2", params[0]);
        return customQuery.executeUpdate();
    }

    @Transactional
    public int deleteEntityByQuery(String query) {
        Query deleteQuery = entityManager.createQuery(query);
        try {
            return deleteQuery.executeUpdate();
        } catch (PersistenceException e) {
            throw e; // rethrow the exception if it's not a constraint violation
        }
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public <T1> T1 addNewEntity(Class<?> entityClass) {
        T1 newEntity = null;
        try {
            newEntity = (T1) entityClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                 | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        entityManager.persist(newEntity);
        return newEntity;
    }

    @Transactional
    public ZJTEntity addNewEntity(ZJTEntity entity) {
        entityManager.persist(entity);
        return entity;
    }

    public ZJTEntity findEntityById(Class<?> entityClass, int nEntityID) {
        return (ZJTEntity) entityManager.find(entityClass, nEntityID);
    }
}