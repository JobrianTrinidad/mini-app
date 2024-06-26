package com.jo.application.data.service;

import com.jo.application.data.entity.ZJTTableInfo;
import com.jo.application.data.repository.TableInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TableInfoService {
    private final TableInfoRepository repository;

    @Autowired
    public TableInfoService(TableInfoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ZJTTableInfo save(ZJTTableInfo tableInfo) {
        return repository.save(tableInfo);
    }

    public ZJTTableInfo findByTableName(String tableName) {
        return repository.findByTableName(tableName);
    }
}
