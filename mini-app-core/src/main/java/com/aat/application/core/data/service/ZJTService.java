package com.aat.application.core.data.service;

import com.aat.application.data.entity.ZJTItem;

import java.util.List;

public interface ZJTService<T> {
    List<T> findAll(String filter);

    List<ZJTItem> findByQuery(String query);

    List<Object[]> findEntityByQuery(String query);

    int updateEntityByQuery(String query, Object[] params);

    <T> List<T> findRecordsByField(String fieldName, Object fieldValue);

    void save(T record);

    void delete(T t);

    <T> List<T> findRecordsByFieldId(String fieldName, int fieldId);
}
