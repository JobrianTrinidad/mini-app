package com.aat.application.data.service;

import com.aat.application.core.data.service.ZJTService;
import com.aat.application.data.entity.ZJTItem;
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
    public List<ZJTItem> findByQuery(String query) {
        return generalRepository.findByQuery(query);
    }

    @Override
    public <T1> List<T1> findRecordsByField(String fieldName, Object fieldValue) {
        if (fieldValue instanceof String) {
            return generalRepository.findRecordsByField(fieldName, (String) fieldValue);
        } else if (fieldValue instanceof Boolean) {
            return generalRepository.findRecordsByField(fieldName, (Boolean) fieldValue);
        } else if (fieldValue instanceof Integer) {
            return generalRepository.findRecordsByField(fieldName, (Integer) fieldValue);
        }
        // Handle other types as needed
        else {
            throw new IllegalArgumentException("Unsupported field value type: " + fieldValue.getClass());
        }
    }

    @Override
    public void save(T record) {
        generalRepository.saveEntity(record);
    }

    @Override
    public void delete(T record) {
        generalRepository.deleteEntity(record);
    }

    @Override
    public <T1> List<T1> findRecordsByFieldId(String fieldName, int filterId) {
        return generalRepository.findRecordsByFieldId(fieldName, filterId);
    }
}