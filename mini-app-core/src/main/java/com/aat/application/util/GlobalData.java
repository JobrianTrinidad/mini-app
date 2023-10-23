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
    public static Map<String, List<ZJTEntity>> listData = new HashMap<>();

    public static void addData(String headerName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("aat_persistence_unit");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.createNativeQuery("alter table if exists zjt_product alter column tripdayoffset set data type integer").executeUpdate();
            em.createNativeQuery("alter table if exists zjt_product alter column tripdayoffset set default 0").executeUpdate();
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
