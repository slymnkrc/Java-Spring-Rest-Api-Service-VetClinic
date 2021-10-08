package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.HomeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/home")
@Api(value = "HomeRestController",authorizations = {@Authorization(value = "basicAuth")})
public class HomeRestController {

    final HomeDto homeDto;

    public HomeRestController(HomeDto homeDto) {
        this.homeDto = homeDto;
    }



    //Kazançlar Grafiği
    @GetMapping("/priceChart")
    public Map<ERest,Object> priceChart(){
        return homeDto.priceChart();
    }

    //Genel İstatistikler Bilgisi
    @GetMapping("/generalStatics")
    public Map<ERest,Object> generalStatics(){
        return homeDto.generalStatics();
    }

    //Günlük Giriş Yapan Hasta Bilgileri Tablosu
    @GetMapping("/patientTable")
    public  Map<ERest,Object> patientList(){
        return homeDto.patientList();
    }

    //Günlük Randevu Bilgilendirme Kartı
    @GetMapping("/schedule/{stPageNo}")
    public Map<ERest,Object> scheduleAppointment(@PathVariable String stPageNo){
        return homeDto.scheduleAppointment(stPageNo);
    }

}
