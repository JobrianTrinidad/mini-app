package com.aat.application.views;

import org.springframework.stereotype.Component;

@Component
public interface LayoutSetter {

    void addDrawerContent();
    void addHeaderContent();
}
