package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.LaboratoryDto;
import com.works.entities.Laboratory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/laboratory")
@Api(value = "LaboratoryRestController",authorizations = {@Authorization(value = "basicAuth")})
public class LaboratoryRestController {

    final LaboratoryDto laboratoryDto;

    public LaboratoryRestController(LaboratoryDto laboratoryDto) {
        this.laboratoryDto = laboratoryDto;
    }

    // Laboratory -> Add
    @PostMapping("/add")
    @ApiOperation(value = "Laboratuvar sonucu ekleme.")
    public Map<ERest,Object> add(@RequestPart("fileName") MultipartFile file, @Valid Laboratory laboratory, BindingResult bindingResult){
        return laboratoryDto.add(file,laboratory,bindingResult);
    }

    // Laboratory -> Pagination
    @GetMapping("/list/{stPage}")
    public Map<ERest,Object> list(@PathVariable String stPage){
        return laboratoryDto.list(stPage);
    }

    // Laboratory -> result detail
    @GetMapping("/detail/{stId}")
    public Map<ERest,Object> detail(@PathVariable String stId){
        return laboratoryDto.detail(stId);
    }

    // Laboratory -> Delete
    @DeleteMapping("/delete/{stId}")
    public Map<ERest,Object> delete(@PathVariable String stId){
        return laboratoryDto.delete(stId);
    }
}