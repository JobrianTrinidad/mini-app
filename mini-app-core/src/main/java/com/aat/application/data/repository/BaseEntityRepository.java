package com.aat.application.data.repository;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.entity.ZJTItem;
import com.aat.application.util.GlobalData;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BaseEntityRepository<T> {
    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entityClass;

    public BaseEntityRepository() {
        this.entityClass = null;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        if (entityClass == null) {
            Type genericSuperclass = getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                Class<T> actualTypeArgument = null;
                try {
                    actualTypeArgument = (Class<T>) Class.forName(actualTypeArguments[0].getTypeName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                entityClass = actualTypeArgument;
            } else {
                throw new IllegalArgumentException("Unable to determine entity class type.");
            }
        }
        return entityClass;
    }

    public List<T> findAll(String stringFilter) {

        if (stringFilter == null || stringFilter.isEmpty()) {
            TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClassName() + " e", entityClass);
            return query.getResultList();
        } else {
            TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClassName() + " e WHERE lower(e.name) LIKE lower(:filter)", entityClass);
            query.setParameter("filter", "%" + stringFilter + "%");
            return query.getResultList();
        }
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

    @SuppressWarnings("unchecked")
    public <T> List<T> findRecordsByField(String fieldName, Object fieldValue) {
        TypedQuery<T> query = (TypedQuery<T>) entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :fieldValue",
                entityClass);
        query.setParameter("fieldValue", fieldValue);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findRecordsByFieldId(String fieldName, int fieldId) {
        TypedQuery<T> query = (TypedQuery<T>) entityManager.createQuery(
                "SELECT e FROM " + getEntityClassName() + " e WHERE e." + fieldName + " = :fieldId", entityClass);
        query.setParameter("fieldId", fieldId);
        return query.getResultList();
    }

    private String getEntityClassName() {
        return getEntityClass().getSimpleName();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public <T> T saveEntity(T entity) {
        Object objEntity = entity;
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {

            ClassLoader emClassLoader = entityManager.getClass().getClassLoader();
            Class<?> entityClass = Class.forName(entity.getClass().getName(), true, emClassLoader);

            ClassLoader entityClassLoader = entity.getClass().getClassLoader();
            Thread.currentThread().setContextClassLoader(entityClassLoader);

            entity = (T) GlobalData.convertToZJTEntity(entity, entityClass);
            objEntity = entityManager.merge(entity);
            entityManager.flush();

        } catch (Exception e) {
            // Handle reflection exceptions here
            e.printStackTrace();
        } finally {
            // Restore the original class loader
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
        return (T) objEntity;
    }

    @Transactional
    public void deleteEntity(T entity) {
        try {
//            T mergedEntity = entityManager.merge(entity);
//            entityManager.remove(mergedEntity);

            Object primaryKey = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity);
            entity = entityManager.find(entityClass, primaryKey);
            if (entity != null) {
                entityManager.remove(entity);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


}