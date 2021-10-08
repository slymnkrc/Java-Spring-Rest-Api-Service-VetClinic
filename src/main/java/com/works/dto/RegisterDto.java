package com.works.dto;

import com.works.Utils.ERest;
import com.works.Utils.Util;
import com.works.entities.User;
import com.works.repositories._jpa.JpaUserRepository;
import com.works.services.UserService;
import com.works.services.UtilService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RegisterDto {

    final JpaUserRepository jpaUserRepository;
    final UserService userService;
    final UtilService utilService;

    public RegisterDto(JpaUserRepository jpaUserRepository, UserService userService, UtilService utilService) {
        this.jpaUserRepository = jpaUserRepository;
        this.userService = userService;
        this.utilService = utilService;
    }


    public Map<ERest,Object> add(User user, BindingResult bindingResult){
        Map<ERest,Object> hm = new LinkedHashMap<>();

        if (!bindingResult.hasErrors()) {

            try {
                User us = userService.register(user);
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Kullanıcı başarılı bir şekilde kaydedildi.");
                hm.put(ERest.result,us);
            } catch (Exception e) {
                hm.put(ERest.status,false);
                if(e.toString().contains("constraint")){
                    String error = "Bu e-mail ("+user.getUEmail()+") adresi ile daha önce kayıt yapılmış!";
                    Util.logger(error,User.class);
                    hm.put(ERest.message,error);
                }
            }

        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

}
