package com.aat.application.core.data.service;

import java.util.List;

public interface ZJTService<T> {
    List<T> findAll(String filter);
    <T> List<T> findRecordsByField(String fieldName, Object fieldValue);
    void save(T record);
    void delete(T t);
}
