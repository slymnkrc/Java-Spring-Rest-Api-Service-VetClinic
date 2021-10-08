package com.works.services;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class UtilService {

    public List<Map<String, String>> errors(BindingResult bindingResult) {
        List<Map<String, String>> ls = new LinkedList<>();
        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String fieldMessage = error.getDefaultMessage();

            Map<String, String> erhm = new HashMap<>();
            erhm.put("fieldName", fieldName);
            erhm.put("fieldMessage", fieldMessage);
            ls.add(erhm);

        });
        return ls;
    }

}
