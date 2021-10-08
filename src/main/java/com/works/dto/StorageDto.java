package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Storage;
import com.works.models.ElasticStorage;
import com.works.repositories._elastic.ElasticStorageRepository;
import com.works.repositories._jpa.JpaStorageRepository;
import com.works.services.UtilService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StorageDto {

    final JpaStorageRepository jpaStorageRepository;
    final UtilService utilService;
    final ElasticStorageRepository elasticStorageRepository;

    public StorageDto(JpaStorageRepository jpaStorageRepository, UtilService utilService, ElasticStorageRepository elasticStorageRepository) {
        this.jpaStorageRepository = jpaStorageRepository;
        this.utilService = utilService;
        this.elasticStorageRepository = elasticStorageRepository;
    }

    // Storage -> Add
    public Map<ERest, Object> add(@Valid Storage storage, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Depo Ekleme Başarılı");
                Storage storage1 = jpaStorageRepository.save(storage);
                hm.put(ERest.result, storage1);
                ElasticStorage elasticStorage = new ElasticStorage();
                elasticStorage.setStorId(Integer.toString(storage1.getStorId()));
                elasticStorage.setStorName(storage1.getStorName());
                elasticStorage.setStorNo(storage1.getStorNo());
                elasticStorage.setStorStatus(storage1.getStorStatus());
                elasticStorageRepository.save(elasticStorage);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Storage -> Delete
    public Map<ERest, Object> delete(Integer storId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaStorageRepository.existsById(storId)) {
                jpaStorageRepository.deleteById(storId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, storId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, storId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, storId);
        }
        return hm;
    }

    // Storage -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Storage> ls = jpaStorageRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Storage -> Update
    public Map<ERest, Object> update(Storage storage) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (storage.getStorId() != null) {
            Optional<Storage> optionalStorage = jpaStorageRepository.findById(storage.getStorId());
            if (optionalStorage.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaStorageRepository.saveAndFlush(storage);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, storage);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, storage);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, storage);
        }
        return hm;
    }

    // Storage -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Storage> pageList = jpaStorageRepository.findByOrderByStorIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Storage -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticStorage> searchPage = elasticStorageRepository.findByStorage(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}
