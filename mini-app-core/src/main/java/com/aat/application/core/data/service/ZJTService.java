package com.aat.application.core.data.service;

import java.util.List;

public interface ZJTService<T> {
    List<T> findAll(String filter);
    void save(T record);
    void delete(T t);
}
