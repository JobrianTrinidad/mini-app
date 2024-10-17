package com.jo.application.data.repository;

import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.data.entity.ZJTItem;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Repository
public class BaseEntityRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public BaseEntityRepository() {

    }

    public List<ZJTEntity> findAll(Class<?> entity) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ZJTEntity> cq = cb.createQuery(ZJTEntity.class);
        Root<ZJTEntity> root = (Root<ZJTEntity>) cq.from(entity);
        cq.select(root);
        TypedQuery<ZJTEntity> query = entityManager.createQuery(cq);
        return query.getResultList();
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
            String className = null;
            String style = null;
            boolean editable = true;
            int id = 0;
            //must be paired in sequence - startdate1, enddate1, startdate2, enddate2
            LocalDateTime startDate = null;
            LocalDateTime endDate = null;
            if (result[0] != null) {
                id = (int)result[0];
            }

            if (result[1] != null) {
                title = (String) result[1];
            }
            if (result[2] != null) {
                groupID = String.valueOf(((ZJTEntity) result[2]).getId());
            }
            if (result[3] != null) {
                className = (String) result[3];
                if (className.isEmpty()) {
                    className = null;
                } else if (className.startsWith("#")) {  //hex color
                    style = "background-color : " + className + ";";

                    if (result[4] != null) {
                        String borderColor = (String) result[4];
                        style += "border-width: 2px; border-color: " + borderColor + ";";
                    }
                    className = null;

                }
            }

            editable = (int)result[5] == 1;

            boolean isStartandEndPaired = false;
            for (Object resultObj : result) {
                int nStartDateId = 0;
                if (resultObj instanceof LocalDateTime) {
                    if (startDate == null) {
                        startDate = (LocalDateTime) resultObj;
                    } else {
                        endDate = (LocalDateTime) resultObj;
                        isStartandEndPaired = true;
                    }
                }
                if (isStartandEndPaired) {
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                    String formattedDate = startDate.format(inputFormatter);
                    startDate = LocalDateTime.parse(formattedDate, inputFormatter);
                    formattedDate = endDate.format(inputFormatter);
                    endDate = LocalDateTime.parse(formattedDate, inputFormatter);
                    ZJTItem item = new ZJTItem(title, groupID, startDate, endDate, className);
                    item.setId(id);
                    item.setStartDateId(nStartDateId);
                    if (style != null) {
                        item.setStyle(style);
                    }
                    item.setEditable(editable);
                    items.add(item);
                    startDate = null;
                    endDate = null;
                    nStartDateId++; //next series if applicable
                }
            }

        }
        return items;
    }

    public List<Object[]> findEntityByQuery(String query) {
        Query customQuery = entityManager.createQuery(query);
        return customQuery.getResultList();
    }

    public List<Object[]> findEntityByQuery(String query, Object[] params) {
        Query customQuery = entityManager.createQuery(query);
        for (int i = 0; i < params.length; i++) {
            customQuery.setParameter("param" + i, params[i]);
        }
        return customQuery.getResultList();
    }

    public List<ZJTEntity> findEntitiesFilteredBy(ZJTEntity filterEntity, String fieldName, Class<?> entityClass) {
        String sql = "SELECT p FROM " + entityClass.getSimpleName() + " AS p"
                + " WHERE p." + fieldName + " = :param0";
        Query customQuery = entityManager.createQuery(sql);
        customQuery.setParameter("param0", filterEntity);

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

    @Transactional
    public ZJTEntity updateEntity(ZJTEntity entity) {
        entityManager.merge(entity);
        return entity;
    }

    @Transactional
    public ZJTEntity deleteEntity(ZJTEntity entity) {
        entityManager.remove(entity);
        return entity;
    }

    public ZJTEntity findEntityById(Class<?> entityClass, int nEntityID) {
        return (ZJTEntity) entityManager.find(entityClass, nEntityID);
    }

    public List<?> findEntitiesByIds(Class<?> entityClass, int[] nEntityIDs) {
        Integer[] entityIDs = Arrays.stream(nEntityIDs).boxed().toArray(Integer[]::new);

        String query = "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e.id IN :ids";
        TypedQuery<ZJTEntity> typedQuery = entityManager.createQuery(query, ZJTEntity.class);
        typedQuery.setParameter("ids", Arrays.asList(entityIDs));

        return typedQuery.getResultList();
    }


    /**
     * Expose entity manager to run query directly for greater control
     * @return
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }
}