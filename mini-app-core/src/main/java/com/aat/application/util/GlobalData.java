package com.aat.application.util;

import com.aat.application.core.data.entity.ZJTEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

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

    public static void addData(String headerName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aat_persistence_unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            TypedQuery<ZJTEntity> query = em.createNamedQuery("findAll" + headerName, ZJTEntity.class);
            List<ZJTEntity> results = query.getResultList();
            listData.put(headerName, results);
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