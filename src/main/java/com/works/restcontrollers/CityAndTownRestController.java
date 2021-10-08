package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.CityAndTownDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cat")
@Api(value = "CityAndTownRestController",authorizations = {@Authorization(value = "basicAuth")})
public class CityAndTownRestController {

    final CityAndTownDto cityAndTownDto;

    public CityAndTownRestController(CityAndTownDto cityAndTownDto) {
        this.cityAndTownDto = cityAndTownDto;
    }


    //MySQL veri tabanımdan bulunan il ve ilçe bilgilerini Redis veri tabanına yazma işlemi.
    @GetMapping("/add")
    public Map<ERest,Object> add(){
        return cityAndTownDto.add();
    }

    //Redis veri tabanından bütün şehirleri sıralama
    @GetMapping("/listCity")
    public Map<ERest,Object> listCity(){
        return cityAndTownDto.listCity();
    }

    //Plaka koduna göre ilçelerin listelenmesi.
    @GetMapping("/listTownsById/{stId}")
    public Map<ERest,Object> listTowns(@PathVariable String stId){
        return cityAndTownDto.listTowns(stId);
    }

}
