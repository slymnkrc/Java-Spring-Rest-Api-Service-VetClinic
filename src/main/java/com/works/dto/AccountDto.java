package com.works.dto;

import com.works.Utils.ERest;
import com.works.Utils.Util;
import com.works.entities.User;
import com.works.repositories._jpa.JpaUserRepository;
import com.works.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountDto {

    final JpaUserRepository jpaUserRepository;
    final UserService userService;

    public AccountDto(JpaUserRepository jpaUserRepository, UserService userService) {
        this.jpaUserRepository = jpaUserRepository;
        this.userService = userService;
    }


    //Upload profile image
    public Map<ERest, Object> upload(MultipartFile file) {
        long maxFileUploadSize = 2048;
        int sendSuccessCount = 0;
        String errorMessage = "";
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!file.isEmpty() ) {
            long fileSizeMB = file.getSize() / 1024;
            if ( fileSizeMB > maxFileUploadSize ) {
                System.err.println("Dosya boyutu çok büyük Max 2MB");
                errorMessage = "Dosya boyutu çok büyük Max "+ (maxFileUploadSize / 1024) +"MB olmalıdır";
                hm.put(ERest.status, false);
                hm.put(ERest.message, errorMessage);
            }else {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                String ext = fileName.substring(fileName.length()-5, fileName.length());
                String uui = UUID.randomUUID().toString();
                fileName = uui + ext;
                try {
                    Path path = Paths.get(Util.UPLOAD_DIR + fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    sendSuccessCount += 1;

                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String email = auth.getName();

                    Optional<User> userOptional = jpaUserRepository.findByuEmailEqualsIgnoreCase(email);
                    if(userOptional.isPresent()){
                        User user = userOptional.get();
                        user.setUProfileImage(fileName);
                        jpaUserRepository.saveAndFlush(user);
                    }else {
                        errorMessage = "Kullanıcı bulunamadı. Lütfen hesabınıza giriş yapınız!";
                        hm.put(ERest.status, false);
                        hm.put(ERest.message, errorMessage);
                    }

                } catch (IOException e) {
                    errorMessage = "Dosya yüklenirken bir hata oluştu!";
                    hm.put(ERest.status, false);
                    hm.put(ERest.message, errorMessage);
                }
            }
        }else {
            errorMessage = "Lütfen resim seçiniz!";
            hm.put(ERest.status, false);
            hm.put(ERest.message, errorMessage);
        }

        if ( errorMessage.equals("") ) {
            hm.put(ERest.status, true);
            hm.put(ERest.message, "Yükleme Başarılı");
        }else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, errorMessage);
        }

        return hm;
    }

    //Change password in active user
    public Map<ERest,Object> changePassword(String newPass,String reNewPass){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        if(!newPass.equals("")  && !reNewPass.equals("")){
            if(newPass.equals(reNewPass)){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String email = auth.getName();
                Optional<User> userOptional = jpaUserRepository.findByuEmailEqualsIgnoreCase(email);
                if(userOptional.isPresent()){
                    hm.put(ERest.status,true);
                    hm.put(ERest.message,"Şifre başarılı bir şekilde değiştirildi.");
                    User user = userOptional.get();
                    user.setUPassword(userService.encoder().encode(reNewPass));
                    jpaUserRepository.save(user);
                }else{
                    String error = "Kullanıcı bulunamadı. Lütfen hesabınıza giriş yapınız!";
                    hm.put(ERest.status,false);
                    hm.put(ERest.message,error);
                    Util.logger(error,User.class);
                }
            }else{
                String error = "Yeni şifre ve tekrarı uyuşmuyor!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                Util.logger(error,User.class);
            }
        }else {
            String error = "Yeni şifre ve tekrarı boş olamaz!";
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,User.class);
        }
        return hm;
    }

}

