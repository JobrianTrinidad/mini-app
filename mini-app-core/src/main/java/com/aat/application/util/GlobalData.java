package com.aat.application.util;

import com.aat.application.core.data.entity.ZJTEntity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData {
    public static Map<String, List<?>> listData = new HashMap<>();

    public static void addData(String headerName, Class<? extends ZJTEntity> entityClass) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aat_persistence_unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            String queryString = "SELECT p FROM " + entityClass.getSimpleName() + " p";
            TypedQuery<?> query = em.createQuery(queryString, entityClass);
            List<?> results = query.getResultList();
//            List<ZJTEntity> results = (List<ZJTEntity>) GlobalData.executeQuery(queryString, entityClass, em);
            listData.put(headerName, results);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
//            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

    public static List<?> getDataById(Class<?> entityClass, int id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aat_persistence_unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<?> results = null;
        try {
            String queryString = "SELECT p FROM " + entityClass.getSimpleName() + " p WHERE p.id = :id";
            TypedQuery<?> query = em.createQuery(queryString, entityClass);
            query.setParameter("id", id);
            results = query.getResultList();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }

        return results;
    }

    public static <T> T convertToZJTEntity(Object entityData, Class<?> zjtEntityClass) {
        try {
            if (entityData instanceof HibernateProxy) {
                Hibernate.initialize(entityData);
                entityData = ((HibernateProxy) entityData).getHibernateLazyInitializer().getImplementation();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String json = mapper.writeValueAsString(entityData);
            return (T) mapper.readValue(json, zjtEntityClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getPrimaryKeyField(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        // Check superclass if no field found
        return getPrimaryKeyField(clazz.getSuperclass());
    }
    public static void addData(String header) {
        String headerName = header.substring(0, 1).toUpperCase()
                + header.substring(1);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aat_persistence_unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            TypedQuery<ZJTEntity> query = em.createNamedQuery("findAll" + headerName, ZJTEntity.class);
            List<ZJTEntity> results = query.getResultList();
            listData.put(header, results);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }
}