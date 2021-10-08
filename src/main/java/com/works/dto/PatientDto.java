package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Customer;
import com.works.entities.Patient;
import com.works.models.ElasticCustomer;
import com.works.models.ElasticPatient;
import com.works.repositories._elastic.ElasticPatientRepository;
import com.works.repositories._jpa.JpaPatientRepository;
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
public class PatientDto {

    final JpaPatientRepository jpaPatientRepository;
    final ElasticPatientRepository elasticPatientRepository;
    final UtilService utilService;

    public PatientDto(JpaPatientRepository jpaPatientRepository, ElasticPatientRepository elasticPatientRepository, UtilService utilService) {
        this.jpaPatientRepository = jpaPatientRepository;
        this.elasticPatientRepository = elasticPatientRepository;
        this.utilService = utilService;
    }

    // Patient -> Add
    public Map<ERest, Object> add(@Valid Patient patient, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Hasta Ekleme Başarılı");
                Patient patient1 = jpaPatientRepository.save(patient);
                hm.put(ERest.result, patient1);
                ElasticPatient elasticPatient= new ElasticPatient();
                elasticPatient.setPaId(Integer.toString(patient1.getPaId()));
                elasticPatient.setCustomerId(Integer.toString(patient1.getCustomerId()));
                elasticPatient.setPaName(patient1.getPaName());
                elasticPatient.setPaChipNo(patient1.getPaChipNo());
                elasticPatient.setPaAirTagNo(patient1.getPaAirTagNo());
                elasticPatient.setPaBirthDate(patient1.getPaBirthDate());
                elasticPatient.setPaType(patient1.getPaType());
                elasticPatient.setPaSpay(patient1.getPaSpay());
                elasticPatient.setPaKind(patient1.getPaKind());
                elasticPatient.setPaColor(patient1.getPaColor());
                elasticPatient.setPaSexType(patient1.getPaSexType());
                elasticPatient.setSaveDate(patient1.getSaveDate());
                elasticPatientRepository.save(elasticPatient);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "AirTagNo ve ChipNo (" + patient.getPaAirTagNo() + " , " + patient.getPaChipNo() +") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Patient -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Patient> ls = jpaPatientRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Patient -> Delete
    public Map<ERest, Object> delete(Integer paId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaPatientRepository.existsById(paId)) {
                jpaPatientRepository.deleteById(paId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, paId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, paId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, paId);
        }
        return hm;
    }

    // Patient -> Update
    public Map<ERest, Object> update(Patient patient) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (patient.getPaId() != null) {
            Optional<Patient> optionalPatient = jpaPatientRepository.findById(patient.getPaId());
            if (optionalPatient.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaPatientRepository.saveAndFlush(patient);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, patient);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, patient);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, patient);
        }
        return hm;
    }

    // Patient -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Patient> pageList = jpaPatientRepository.findByOrderByPaIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Patient -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticPatient> searchPage = elasticPatientRepository.findByPatient(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }

    // PatientByCustomerId -> List
    public Map<ERest,Object> listByCustomer(Integer cuId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Patient> ls = jpaPatientRepository.findByCustomer_CuIdEquals(cuId);
        hm.put(ERest.result,ls);
        return hm;
    }
}
