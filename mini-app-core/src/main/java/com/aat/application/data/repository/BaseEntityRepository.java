package com.aat.application.data.repository;

import com.aat.application.data.entity.ZJTItem;
import com.aat.application.util.GlobalData;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    private Class<T> getEntityClass() {
        if (entityClass == null) {
            Type genericSuperclass = getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
                @SuppressWarnings("unchecked")
                Class<T> actualTypeArgument = (Class<T>) actualTypeArguments[0];
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
            if (result[1] != null && result[2] != null) {
                for (Field field : result[1].getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getAnnotation(Id.class) != null) {
                        try {
                            groupID = String.valueOf(field.get(result[1]));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }
                startDate = (LocalDateTime) result[2];
                ZJTItem item = new ZJTItem(title, groupID, startDate);
                items.add(item);
            }
        }
        return items;
    }

    public List<Object[]> findEntityByQuery(String query) {
        Query customQuery = entityManager.createQuery(query);
        List<Object[]> temp = customQuery.getResultList();
//        temp.forEach(arr -> arr = Arrays.stream(arr)
//                .filter(obj -> obj != null)
//                .toArray());
        return temp;
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
            entityManager.merge(entity);
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