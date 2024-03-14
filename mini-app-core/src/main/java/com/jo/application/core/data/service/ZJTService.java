package com.jo.application.core.data.service;

import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.data.entity.ZJTItem;

import java.util.List;

public interface ZJTService {
    List<ZJTItem> findByQuery(String query);

    List<Object[]> findEntityByQuery(String query);

    int updateEntityByQuery(String query, Object[] params);

    int deleteEntityByQuery(String query);

    <T> T addNewEntity(Class<?> entityClass);

    ZJTEntity addNewEntity(ZJTEntity entity);

    ZJTEntity findEntityByID(Class<?> entityClass, int id);

}