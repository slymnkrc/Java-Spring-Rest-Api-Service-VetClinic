package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Patient;
import com.works.entities.PayOut;
import com.works.models.ElasticPatient;
import com.works.models.ElasticPayOut;
import com.works.repositories._elastic.ElasticPayOutRepository;
import com.works.repositories._jpa.JpaPayOutRepository;
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
public class PayOutDto {

    final JpaPayOutRepository jpaPayOutRepository;
    final ElasticPayOutRepository elasticPayOutRepository;
    final UtilService utilService;

    public PayOutDto(JpaPayOutRepository jpaPayOutRepository, ElasticPayOutRepository elasticPayOutRepository, UtilService utilService) {
        this.jpaPayOutRepository = jpaPayOutRepository;
        this.elasticPayOutRepository = elasticPayOutRepository;
        this.utilService = utilService;
    }

    // PayOut -> Add
    public Map<ERest, Object> add(@Valid PayOut payOut, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Kasadan Para Çıkışı İşlemi Başarılı");
                PayOut payOut1 = jpaPayOutRepository.save(payOut);
                hm.put(ERest.result, payOut1);
                ElasticPayOut elasticPayOut= new ElasticPayOut();
                elasticPayOut.setPoId(Integer.toString(payOut1.getPoId()));
                elasticPayOut.setPoutNote(payOut1.getPoutNote());
                elasticPayOut.setBuyReceiptNo(Long.toString(payOut1.getBuying().getBuyReceiptNo()));
                elasticPayOut.setCreatedDate(payOut1.getCreatedDate());
                elasticPayOut.setPoutAmount(Integer.toString(payOut1.getPoutAmount()));
                elasticPayOutRepository.save(elasticPayOut);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
//                    String error = "AirTagNo ve ChipNo (" + patient.getPaAirTagNo() + " , " + patient.getPaChipNo() +") ile daha önce kayıt yapılmış";
//                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // PayOut -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<PayOut> ls = jpaPayOutRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // PayOut -> Delete
    public Map<ERest, Object> delete(Integer poId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaPayOutRepository.existsById(poId)) {
                jpaPayOutRepository.deleteById(poId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, poId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, poId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, poId);
        }
        return hm;
    }

    // PayOut -> Update
    public Map<ERest, Object> update(PayOut payOut) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (payOut.getPoId() != null) {
            Optional<PayOut> optionalPayOut = jpaPayOutRepository.findById(payOut.getPoId());
            if (optionalPayOut.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaPayOutRepository.saveAndFlush(payOut);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, payOut);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, payOut);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, payOut);
        }
        return hm;
    }

    // PayOut -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<PayOut> pageList = jpaPayOutRepository.findByOrderByPoIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // PayOut -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticPayOut> searchPage = elasticPayOutRepository.findByPayOut(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}
