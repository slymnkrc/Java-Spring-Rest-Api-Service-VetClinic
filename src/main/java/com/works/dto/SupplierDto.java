package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Supplier;
import com.works.models.ElasticSupplier;
import com.works.repositories._elastic.ElasticSupplierRepository;
import com.works.repositories._jpa.JpaSupplierRepository;
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
public class SupplierDto {

    final JpaSupplierRepository jpaSupplierRepository;
    final UtilService utilService;
    final ElasticSupplierRepository elasticSupplierRepository;

    public SupplierDto(JpaSupplierRepository jpaSupplierRepository, UtilService utilService, ElasticSupplierRepository elasticSupplierRepository) {
        this.jpaSupplierRepository = jpaSupplierRepository;
        this.utilService = utilService;
        this.elasticSupplierRepository = elasticSupplierRepository;
    }

    // Supplier -> Add
    public Map<ERest, Object> add(@Valid Supplier supplier, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Tedarikçi Ekleme Başarılı");
                Supplier supplier1 = jpaSupplierRepository.save(supplier);
                hm.put(ERest.result, supplier1);
                ElasticSupplier elasticSupplier = new ElasticSupplier();
                elasticSupplier.setSid(Integer.toString(supplier1.getSupId()));
                elasticSupplier.setSupName(supplier1.getSupName());
                elasticSupplier.setSupEmail(supplier1.getSupEmail());
                elasticSupplier.setSupPhone(supplier1.getSupPhone());
                elasticSupplierRepository.save(elasticSupplier);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "Name,Email ve Phone Bilgisi (" + supplier.getSupName() + " , " + supplier.getSupEmail() + " , " + supplier.getSupPhone() + ") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Supplier -> Delete
    public Map<ERest, Object> delete(Integer supId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaSupplierRepository.existsById(supId)) {
                jpaSupplierRepository.deleteById(supId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, supId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, supId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, supId);
        }
        return hm;
    }

    // Supplier -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Supplier> ls = jpaSupplierRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Supplier -> Update
    public Map<ERest, Object> update(Supplier supplier) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (supplier.getSupId() != null) {
            Optional<Supplier> optionalSupplier = jpaSupplierRepository.findById(supplier.getSupId());
            if (optionalSupplier.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaSupplierRepository.saveAndFlush(supplier);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, supplier);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "Name,Email ve Phone Bilgisi (" + supplier.getSupName() + " , " + supplier.getSupEmail() + " , " + supplier.getSupPhone() + ") ile daha önce kayıt yapılmış";
                        hm.put(ERest.message, error);
                    }
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, supplier);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, supplier);
        }
        return hm;
    }

    // Supplier -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Supplier> pageList = jpaSupplierRepository.findByOrderBySupIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Supplier -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticSupplier> searchPage = elasticSupplierRepository.findBySupplier(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }


}
