package com.jo.application.data.service;

import com.jo.application.data.entity.ADImage;
import com.jo.application.data.repository.BaseEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ADImageService {

    private static final Logger logger = LoggerFactory.getLogger(ADImageService.class);

    @Autowired
    private BaseEntityRepository adImageRepository;

    public ADImage addNewEntity(ADImage adImage) {
        logger.info("Adding new entity: {}", adImage);
        adImageRepository.addNewEntity(adImage);
        return adImage;
    }

    public ADImage findEntityByID(int id) {
        return (ADImage) adImageRepository.findEntityById(ADImage.class, id);
    }


    public ADImage updateEntity(ADImage adImage) {
        return (ADImage) adImageRepository.updateEntity(adImage);
    }

    public ADImage deleteImage(int id) {
        logger.info("Finding entity by ID: {}", id);
        ADImage image = (ADImage) adImageRepository.findEntityById(ADImage.class, id);
       return (ADImage) adImageRepository.deleteEntity(image);
    }
}
