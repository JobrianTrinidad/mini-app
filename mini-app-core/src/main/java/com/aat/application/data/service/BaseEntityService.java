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
    public List<Object[]> findEntityByQuery(String query) {
        return generalRepository.findEntityByQuery(query);
    }

    @Override
    public int updateEntityByQuery(String query, Object[] params) {
        return generalRepository.updateEntityByQuery(query, params);
    }

    @Override
    public int deleteEntityByQuery(String query) {
        return generalRepository.deleteEntityByQuery(query);
    }

    @Override
    public <T1> T1 addNewEntity(Class<?> entityClass) {
        return generalRepository.addNewEntity(entityClass);
    }

    @Override
    public <T1> List<T1> findRecordsByField(String fieldName, Object fieldValue) {
        if (fieldValue instanceof String) {
            return generalRepository.findRecordsByField(fieldName, fieldValue);
        } else if (fieldValue instanceof Boolean) {
            return generalRepository.findRecordsByField(fieldName, fieldValue);
        } else if (fieldValue instanceof Integer) {
            return generalRepository.findRecordsByField(fieldName, fieldValue);
        }
        // Handle other types as needed
        else {
            throw new IllegalArgumentException("Unsupported field value type: " + fieldValue.getClass());
        }
    }

    @Override
    public <T> T save(T record) {
        return generalRepository.saveEntity(record);
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