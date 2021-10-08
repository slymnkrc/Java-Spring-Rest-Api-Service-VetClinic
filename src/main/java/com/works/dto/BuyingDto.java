package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Buying;
import com.works.entities.Sales;
import com.works.models.ElasticBuying;
import com.works.models.ElasticSales;
import com.works.repositories._elastic.ElasticBuyingRepository;
import com.works.repositories._jpa.JpaBuyingRepository;
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
public class BuyingDto {

    final JpaBuyingRepository jpaBuyingRepository;
    final ElasticBuyingRepository elasticBuyingRepository;
    final UtilService utilService;

    public BuyingDto(JpaBuyingRepository jpaBuyingRepository, ElasticBuyingRepository elasticBuyingRepository, UtilService utilService) {
        this.jpaBuyingRepository = jpaBuyingRepository;
        this.elasticBuyingRepository = elasticBuyingRepository;
        this.utilService = utilService;
    }

    // Buying -> Add
    public Map<ERest, Object> add(@Valid Buying buying, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Satış Ekleme Başarılı");
                Buying buying1 = jpaBuyingRepository.save(buying);
                hm.put(ERest.result, buying1);
                ElasticBuying elasticBuying= new ElasticBuying();
                elasticBuying.setBuyId(Integer.toString(buying1.getBuyId()));
                elasticBuying.setBuyReceiptNo(Long.toString(buying1.getBuyReceiptNo()));
                elasticBuying.setBuyNote(buying1.getBuyNote());
                elasticBuying.setBuyDate(buying1.getBuyDate());
                elasticBuying.setSupName(buying1.getSupplier().getSupName());
                elasticBuying.setProName(buying1.getSupplierProduct().getSupProName());
                elasticBuyingRepository.save(elasticBuying);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "Bu fiş no (" + buying.getBuyReceiptNo() + ") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Buying -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Buying> ls = jpaBuyingRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Buying -> Delete
    public Map<ERest, Object> delete(Integer buyId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaBuyingRepository.existsById(buyId)) {
                jpaBuyingRepository.deleteById(buyId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, buyId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, buyId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, buyId);
        }
        return hm;
    }

    // Buying -> Update
    public Map<ERest, Object> update(Buying buying) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (buying.getBuyId() != null) {
            Optional<Buying> optionalBuying= jpaBuyingRepository.findById(buying.getBuyId());
            if (optionalBuying.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaBuyingRepository.saveAndFlush(buying);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, buying);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, buying);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, buying);
        }
        return hm;
    }

    // Buying -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Buying> pageList = jpaBuyingRepository.findByOrderByBuyIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Buying -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticBuying> searchPage = elasticBuyingRepository.findByBuying(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }

}
