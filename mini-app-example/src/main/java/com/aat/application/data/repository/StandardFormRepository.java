package com.aat.application.data.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class StandardFormRepository<T> extends BaseEntityRepository<T>{
}