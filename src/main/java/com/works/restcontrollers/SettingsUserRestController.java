package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.SettingsUserDto;
import com.works.entities.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/settingsUsers")
@Api(value = "SettingsUsersRestController",authorizations = {@Authorization(value = "basicAuth")})
public class SettingsUserRestController {

    final SettingsUserDto settingsUserDto;

    public SettingsUserRestController(SettingsUserDto settingsUserDto) {
        this.settingsUserDto = settingsUserDto;
    }

    // User -> add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid User user, BindingResult bindingResult){
        return settingsUserDto.add(user,bindingResult);
    }

    // User -> Pagination
    @GetMapping("/list/{stPage}")
    public Map<ERest,Object> list(@PathVariable String stPage){
        return settingsUserDto.list(stPage);
    }

    // User -> Delete
    @DeleteMapping("/delete/{stId}")
    public Map<ERest,Object> delete(@PathVariable String stId){
        return settingsUserDto.delete(stId);
    }

    // User -> Update
    @PutMapping("/update")
    public Map<ERest,Object> update(@RequestBody @Valid User user,BindingResult bindingResult){
        return settingsUserDto.update(user,bindingResult);
    }

}

