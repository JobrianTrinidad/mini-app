package com.aat.application.data.repository;

import com.aat.application.data.entity.ZJTItem;
import com.aat.application.util.GlobalData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

    public List<ZJTItem> findByQuery(String query) {
        if (query == null || query.isEmpty()) {
            TypedQuery<ZJTItem> defaultQuery = entityManager.createQuery("SELECT e FROM " + getEntityClassName() + " e", ZJTItem.class);
            return defaultQuery.getResultList();
        } else {
            TypedQuery<ZJTItem> customQuery = entityManager.createQuery(query, ZJTItem.class);
            return customQuery.getResultList();
        }
    }

    public <T> List<T> findRecordsByField(String fieldName, Object fieldValue) {
        TypedQuery<T> query = (TypedQuery<T>) entityManager.createQuery(
                "SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :fieldValue",
                entityClass);
        query.setParameter("fieldValue", fieldValue);
        return query.getResultList();
    }
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