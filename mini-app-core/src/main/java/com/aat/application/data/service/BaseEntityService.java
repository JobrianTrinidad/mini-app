package com.aat.application.data.service;

import com.aat.application.core.ZJTService;
import com.aat.application.data.repository.BaseEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseEntityService<T> implements ZJTService<T> {

    private final BaseEntityRepository<T> generalRepository;

    @Autowired
    public BaseEntityService(BaseEntityRepository<T> generalRepository) {
        this.generalRepository = generalRepository;
    }

    @Override
    public List<T> findAll(String filter) {
        return generalRepository.findAll(filter);
    }

    @Override
    public void save(T record) {
        generalRepository.saveEntity(record);
    }

    @Override
    public void delete(T record) {
        generalRepository.deleteEntity(record);
    }
}
