package com.works.restcontrollers;


import com.works.Utils.ERest;
import com.works.dto.RegisterDto;
import com.works.entities.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class RegisterRestController {

    final RegisterDto registerDto;

    public RegisterRestController(RegisterDto registerDto) {
        this.registerDto = registerDto;
    }


    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid User user, BindingResult bindingResult){
        return registerDto.add(user,bindingResult);
    }

}

