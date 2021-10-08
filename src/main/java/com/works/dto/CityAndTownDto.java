package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Cities;
import com.works.entities.Towns;
import com.works.models.RedisCities;
import com.works.models.RedisTowns;
import com.works.repositories._jpa.JpaCitiesRepository;
import com.works.repositories._jpa.JpaTownsRepository;
import com.works.repositories._redis.RedisCitiesRepository;
import com.works.repositories._redis.RedisTownsRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityAndTownDto {

    final RedisCitiesRepository redisCitiesRepository;
    final RedisTownsRepository redisTownsRepository;
    final JpaCitiesRepository jpaCitiesRepository;
    final JpaTownsRepository jpaTownsRepository;

    public CityAndTownDto(RedisCitiesRepository redisCitiesRepository, RedisTownsRepository redisTownsRepository, JpaCitiesRepository jpaCitiesRepository, JpaTownsRepository jpaTownsRepository) {
        this.redisCitiesRepository = redisCitiesRepository;
        this.redisTownsRepository = redisTownsRepository;
        this.jpaCitiesRepository = jpaCitiesRepository;
        this.jpaTownsRepository = jpaTownsRepository;
    }


    //MySQL veri tabanımdan bulunan il ve ilçe bilgilerini Redis veri tabanına yazma işlemi.
    public Map<ERest,Object> add(){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<Cities> cityList = jpaCitiesRepository.findAll();
        List<Towns> townList = jpaTownsRepository.findAll();


        if(cityList != null && townList != null){
            if(cityList.size() > 0 && townList.size() > 0){
                hm.put(ERest.status,true);
                hm.put(ERest.message,"İl ve İlçe bilgileri Redis veri tabanına başarılı bir şekilde aktarıldı!");
                cityList.forEach(item -> {
                    RedisCities redisCities = new RedisCities();
                    redisCities.setId(item.getId());
                    redisCities.setCityKey(item.getCityKey());
                    redisCities.setName(item.getName());
                    redisCitiesRepository.save(redisCities);
                });
                townList.forEach(item -> {
                    RedisTowns redisTowns = new RedisTowns();
                    redisTowns.setId(item.getId());
                    redisTowns.setName(item.getName());
                    redisTowns.setTownCityKey(item.getTownCityKey());
                    redisTowns.setTownKey(item.getTownKey());
                    redisTownsRepository.save(redisTowns);
                });
            }else{
                hm.put(ERest.status,true);
                hm.put(ERest.message,"İl ve İlçe bilgisi bulunmamaktadır!");
            }
        }else{
            hm.put(ERest.status,false);
            hm.put(ERest.message,"Redis veri tabanına veriler yüklenirken bir hata oluştu!");
            hm.put(ERest.result,null);
        }
        return hm;
    }

    //Redis veri tabanından bütün şehirleri sıralama
    public Map<ERest,Object> listCity(){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        hm.put(ERest.message,"Şehir listeleme işlemi başarılı!");
        hm.put(ERest.result,redisCitiesRepository.findAll());
        return hm;
    }

    //Plaka koduna göre ilçelerin listelenmesi.
    public Map<ERest,Object> listTowns(String stId){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            hm.put(ERest.status,true);
            hm.put(ERest.message,"Şehir koduna göre ilçe listeleme işlemi başarılı!");
            hm.put(ERest.result,redisTownsRepository.findByTownCityKeyEquals(id));
        } catch (Exception e) {
            hm.put(ERest.status,false);
            hm.put(ERest.message,"Listeleme işlemi sırasında bir hata oluştu!");
        }
        return hm;
    }

}

