/*
Created by IntelliJ IDEA.
By trohi
Date: 20.05.2019 15:30
Project Name: football
*/
package com.mcw.football.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {

    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}