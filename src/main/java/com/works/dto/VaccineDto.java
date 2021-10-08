package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Product;
import com.works.entities.Vaccine;
import com.works.models.ElasticProduct;
import com.works.models.ElasticVaccine;
import com.works.repositories._elastic.ElasticVaccineRepository;
import com.works.repositories._jpa.JpaVaccineRepository;
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
public class VaccineDto {

    final JpaVaccineRepository jpaVaccineRepository;
    final UtilService utilService;
    final ElasticVaccineRepository elasticVaccineRepository;

    public VaccineDto(JpaVaccineRepository jpaVaccineRepository, UtilService utilService, ElasticVaccineRepository elasticVaccineRepository) {
        this.jpaVaccineRepository = jpaVaccineRepository;
        this.utilService = utilService;
        this.elasticVaccineRepository = elasticVaccineRepository;
    }

    // Vaccine -> Add
    public Map<ERest, Object> add(@Valid Vaccine vaccine, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Aşı Ekleme Başarılı");
                Vaccine vaccine1 = jpaVaccineRepository.save(vaccine);
                hm.put(ERest.result, vaccine1);
                ElasticVaccine elasticVaccine = new ElasticVaccine();
                elasticVaccine.setVacid(Integer.toString(vaccine1.getVacid()));
                elasticVaccine.setVacName(vaccine1.getVacName());
                elasticVaccine.setVacUnit(vaccine1.getVacUnit());
                elasticVaccine.setVacCategory(vaccine1.getVacCategory());
                elasticVaccine.setVacDetail(vaccine1.getVacDetail());
                elasticVaccine.setVacType(vaccine1.getVacType());
                elasticVaccine.setVacSupplier(vaccine1.getVacSupplier());
                elasticVaccine.setVacBarcode(Long.toString(vaccine1.getVacBarcode()));
                elasticVaccine.setVacCode(Long.toString(vaccine1.getVacCode()));
                elasticVaccineRepository.save(elasticVaccine);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "vacBarcode ve vacCode Bilgisi (" + vaccine.getVacBarcode() + " , " + vaccine.getVacCode() + ") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Vaccine -> Delete
    public Map<ERest, Object> delete(Integer vacid) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaVaccineRepository.existsById(vacid)) {
                jpaVaccineRepository.deleteById(vacid);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, vacid);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, vacid);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, vacid);
        }
        return hm;
    }

    // Vaccine -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Vaccine> ls = jpaVaccineRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Vaccine -> Update
    public Map<ERest, Object> update(Vaccine vaccine) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (vaccine.getVacid() != null) {
            Optional<Vaccine> optionalVaccine = jpaVaccineRepository.findById(vaccine.getVacid());
            if (optionalVaccine.isPresent()) {
                try {
                    jpaVaccineRepository.saveAndFlush(vaccine);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, vaccine);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "vacBarcode ve vacCode Bilgisi (" + vaccine.getVacBarcode() + " , " + vaccine.getVacCode() + ") ile daha önce kayıt yapılmış";
                        hm.put(ERest.message, error);
                    }
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, vaccine);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, vaccine);
        }
        return hm;
    }

    // Vaccine -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Vaccine> pageList = jpaVaccineRepository.findByOrderByVacidAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Vaccine -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticVaccine> searchPage = elasticVaccineRepository.findByVaccine(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}
