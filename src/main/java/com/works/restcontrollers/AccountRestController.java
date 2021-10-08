package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.AccountDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/account")
@Api(value = "AccountRestController",authorizations = {@Authorization(value = "basicAuth")})
public class AccountRestController {

    final AccountDto accountDto;

    public AccountRestController(AccountDto accountDto) {
        this.accountDto = accountDto;
    }


    // Account -> Upload profile image
    @PostMapping("/upload")
    //@ApiModelProperty(value = "Kullanıcı hesabına profil resmi ekleme.",required = true)
    public Map<ERest, Object> upload(@RequestParam("fileName") MultipartFile file){
        return accountDto.upload(file);
    }

    // Account -> Change password in active user
    @PostMapping("/changePass")
    public Map<ERest,Object> changePassword(@RequestParam String newPass,@RequestParam String reNewPass){
        return accountDto.changePassword(newPass,reNewPass);
    }

}
