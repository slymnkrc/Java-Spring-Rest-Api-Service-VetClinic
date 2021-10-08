package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Customer;
import com.works.entities.Sales;
import com.works.models.ElasticCustomer;
import com.works.models.ElasticSales;
import com.works.repositories._elastic.ElasticSalesRepository;
import com.works.repositories._jpa.JpaSalesRepository;
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
public class SalesDto {

    final JpaSalesRepository jpaSalesRepository;
    final ElasticSalesRepository elasticSalesRepository;
    final UtilService utilService;


    public SalesDto(JpaSalesRepository jpaSalesRepository, ElasticSalesRepository elasticSalesRepository, UtilService utilService) {
        this.jpaSalesRepository = jpaSalesRepository;
        this.elasticSalesRepository = elasticSalesRepository;
        this.utilService = utilService;
    }

    // Sales -> Add
    public Map<ERest, Object> add(@Valid Sales sales, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Satış Ekleme Başarılı");
                Sales sales1 = jpaSalesRepository.save(sales);
                hm.put(ERest.result, sales1);
                ElasticSales elasticSales= new ElasticSales();
                elasticSales.setSaId(Integer.toString(sales1.getSaId()));
                elasticSales.setSaNote(sales1.getSaNote());
                elasticSales.setSaLabType(sales1.getSaLabType());
                elasticSales.setSaReceiptNo(sales1.getSaReceiptNo());
                elasticSales.setSaSoldDate(sales1.getSaSoldDate());
                elasticSales.setPaName(sales1.getPatient().getPaName());
                elasticSales.setCuName(sales1.getCustomer().getCuName());
                elasticSales.setVacName(sales1.getVaccine().getVacName());
                elasticSales.setProName(sales1.getProduct().getProName());
                elasticSalesRepository.save(elasticSales);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "Bu fiş no (" + sales.getSaReceiptNo() + ") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Sales -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Sales> ls = jpaSalesRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Sales -> Delete
    public Map<ERest, Object> delete(Integer saId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaSalesRepository.existsById(saId)) {
                jpaSalesRepository.deleteById(saId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, saId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, saId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, saId);
        }
        return hm;
    }

    // Sales -> Update
    public Map<ERest, Object> update(Sales sales) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (sales.getSaId() != null) {
            Optional<Sales> optionalSales= jpaSalesRepository.findById(sales.getSaId());
            if (optionalSales.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaSalesRepository.saveAndFlush(sales);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, sales);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, sales);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, sales);
        }
        return hm;
    }

    // Sales -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Sales> pageList = jpaSalesRepository.findByOrderBySaIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Sales -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticSales> searchPage = elasticSalesRepository.findBySales(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}
