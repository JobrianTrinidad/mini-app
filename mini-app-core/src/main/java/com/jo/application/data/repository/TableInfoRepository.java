package com.jo.application.data.repository;

import com.jo.application.data.entity.ZJTTableInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TableInfoRepository extends JpaRepository<ZJTTableInfo, Integer> {
    @Query("SELECT t FROM ZJTTableInfo t WHERE t.table_name = :tableName")
    ZJTTableInfo findByTableName(@Param("tableName") String tableName);
    ZJTTableInfo save(ZJTTableInfo tableInfo);

}
