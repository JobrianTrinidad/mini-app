package com.aat.application.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
            List<T> result = query.getResultList();
            return result;
        } else {
            TypedQuery<T> query = entityManager.createQuery("SELECT e FROM " + getEntityClassName() + " e WHERE lower(e.name) LIKE lower(:filter)", entityClass);
            query.setParameter("filter", "%" + stringFilter + "%");
            return query.getResultList();
        }
    }

    private String getEntityClassName() {
        return getEntityClass().getSimpleName();
    }

    @Transactional
    public <T> T saveEntity(T entity) {
        Object objEntity = entity;
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            // Get the ClassLoader of the entity
            ClassLoader entityClassLoader = entity.getClass().getClassLoader();
            Thread.currentThread().setContextClassLoader(entityClassLoader);


            // Use reflection to call merge and flush
            Method mergeMethod = entityManager.getClass().getMethod("merge", Object.class);
            objEntity = (T) mergeMethod.invoke(entityManager, objEntity);

            Method flushMethod = entityManager.getClass().getMethod("flush");
            flushMethod.invoke(entityManager);
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
            T mergedEntity = entityManager.merge(entity);
            entityManager.remove(mergedEntity);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}