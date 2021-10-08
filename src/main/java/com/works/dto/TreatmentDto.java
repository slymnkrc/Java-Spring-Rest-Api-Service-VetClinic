package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Customer;
import com.works.entities.Treatment;
import com.works.models.ElasticCustomer;
import com.works.models.ElasticTreatment;
import com.works.repositories._elastic.ElasticTreatmentRepository;
import com.works.repositories._jpa.JpaTreatmentRepository;
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
public class TreatmentDto {

    final JpaTreatmentRepository jpaTreatmentRepository;
    final UtilService utilService;
    final ElasticTreatmentRepository elasticTreatmentRepository;

    public TreatmentDto(JpaTreatmentRepository jpaTreatmentRepository, UtilService utilService, ElasticTreatmentRepository elasticTreatmentRepository) {
        this.jpaTreatmentRepository = jpaTreatmentRepository;
        this.utilService = utilService;
        this.elasticTreatmentRepository = elasticTreatmentRepository;
    }

    // Treatment -> Add
    public Map<ERest, Object> add(@Valid Treatment treatment, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Muayene Ekleme Başarılı");
                Treatment treatment1 = jpaTreatmentRepository.save(treatment);
                hm.put(ERest.result, treatment1);
                ElasticTreatment elasticTreatment= new ElasticTreatment();
                elasticTreatment.setTreId(Integer.toString(treatment1.getTreId()));
                elasticTreatment.setTreNote(treatment1.getTreNote());
                elasticTreatment.setTreLab(treatment1.getTreLab());
                elasticTreatment.setTreOperation(treatment1.getTreOperation());
                elasticTreatment.setTreDressing(treatment1.getTreDressing());
                elasticTreatment.setTreRadiography(treatment1.getTreRadiography());
                elasticTreatment.setTreMedicine(treatment1.getTreMedicine());
                elasticTreatment.setTreCode(Long.toString(treatment1.getTreCode()));
                elasticTreatment.setCuName(treatment1.getCustomer().getCuName());
                elasticTreatment.setPaName(treatment1.getPatient().getPaName());
                elasticTreatment.setVacName(treatment1.getVaccine().getVacName());
                elasticTreatmentRepository.save(elasticTreatment);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
//                if (ex.toString().contains("constraint")) {
//                    String error = "Name,Email ve Phone Bilgisi (" + customer.getCuName() + " , " + customer.getCuEmail() + " , " + customer.getCuPhone() + ") ile daha önce kayıt yapılmış";
//                    hm.put(ERest.message, error);
//                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Treatment -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Treatment> ls = jpaTreatmentRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Treatment -> Delete
    public Map<ERest, Object> delete(Integer treId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaTreatmentRepository.existsById(treId)) {
                jpaTreatmentRepository.deleteById(treId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, treId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, treId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, treId);
        }
        return hm;
    }

    // Treatment -> Update
    public Map<ERest, Object> update(Treatment treatment) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (treatment.getTreId() != null) {
            Optional<Treatment> optionalCustomer = jpaTreatmentRepository.findById(treatment.getTreId());
            if (optionalCustomer.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaTreatmentRepository.saveAndFlush(treatment);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, treatment);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, treatment);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, treatment);
        }
        return hm;
    }

    // Treatment -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Treatment> pageList = jpaTreatmentRepository.findByOrderByTreIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Treatment -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticTreatment> searchPage = elasticTreatmentRepository.findByTreatment(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}
