package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Customer;
import com.works.entities.Storage;
import com.works.models.ElasticCustomer;
import com.works.models.ElasticStorage;
import com.works.repositories._elastic.ElasticCustomerRepository;
import com.works.repositories._jpa.JpaCustomerRepository;
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
public class CustomerDto {

    final JpaCustomerRepository jpaCustomerRepository;
    final ElasticCustomerRepository elasticCustomerRepository;
    final UtilService utilService;

    public CustomerDto(JpaCustomerRepository jpaCustomerRepository, ElasticCustomerRepository elasticCustomerRepository, UtilService utilService) {
        this.jpaCustomerRepository = jpaCustomerRepository;
        this.elasticCustomerRepository = elasticCustomerRepository;
        this.utilService = utilService;
    }

    // Customer -> Add
    public Map<ERest, Object> add(@Valid Customer customer, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Müşteri Ekleme Başarılı");
                Customer customer1 = jpaCustomerRepository.save(customer);
                hm.put(ERest.result, customer1);
                ElasticCustomer elasticCustomer= new ElasticCustomer();
                elasticCustomer.setCuId(Integer.toString(customer1.getCuId()));
                elasticCustomer.setCuName(customer1.getCuName());
                elasticCustomer.setCuSurname(customer1.getCuSurname());
                elasticCustomer.setCuTax(customer1.getCuTax());
                elasticCustomer.setCuTaxOffice(customer1.getCuTaxOffice());
                elasticCustomer.setCuPhone(customer1.getCuPhone());
                elasticCustomer.setCuPhone2(customer1.getCuPhone2());
                elasticCustomer.setCuEmail(customer1.getCuEmail());
                elasticCustomer.setCuType(customer1.getCuType());
                elasticCustomer.setCuCity(Integer.toString(customer1.getCuCity()));
                elasticCustomer.setCuTown(customer1.getCuTown());
                elasticCustomer.setCuAddress(customer1.getCuAddress());
                elasticCustomer.setCuNote(customer1.getCuNote());
                elasticCustomerRepository.save(elasticCustomer);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "Name,Email ve Phone Bilgisi (" + customer.getCuName() + " , " + customer.getCuEmail() + " , " + customer.getCuPhone() + ") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Customer -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Customer> ls = jpaCustomerRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Customer -> Delete
    public Map<ERest, Object> delete(Integer cuId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaCustomerRepository.existsById(cuId)) {
                jpaCustomerRepository.deleteById(cuId);
                elasticCustomerRepository.deleteById(Integer.toString(cuId));
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, cuId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, cuId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, cuId);
        }
        return hm;
    }

    // Customer -> Update
    public Map<ERest, Object> update(Customer customer) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (customer.getCuId() != null) {
            Optional<Customer> optionalCustomer = jpaCustomerRepository.findById(customer.getCuId());
            if (optionalCustomer.isPresent()) {
                try {  // try catch kurmamızın sebebi gğncelleme sırasında başka kullanıcaya ait mail adresinin girilmesi sonucu hatayı önlemek
                    jpaCustomerRepository.saveAndFlush(customer);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, customer);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, customer);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, customer);
        }
        return hm;
    }

    // Customer -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Customer> pageList = jpaCustomerRepository.findByOrderByCuIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }

    // Customer -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticCustomer> searchPage = elasticCustomerRepository.findByCustomer(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}
