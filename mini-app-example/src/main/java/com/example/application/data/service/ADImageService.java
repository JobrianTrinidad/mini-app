package com.example.application.data.service;

import com.example.application.data.entity.ADImage;
import com.jo.application.data.repository.BaseEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ADImageService {

    @Autowired
    private BaseEntityRepository adImageRepository;

    public ADImage addNewEntity(ADImage adImage) {
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
        ADImage image = (ADImage) adImageRepository.findEntityById(ADImage.class, id);
       return (ADImage) adImageRepository.deleteEntity(image);
    }
}
