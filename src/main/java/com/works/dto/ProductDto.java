package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.Product;
import com.works.entities.Supplier;
import com.works.models.ElasticProduct;
import com.works.models.ElasticSupplier;
import com.works.repositories._elastic.ElasticProductRepository;
import com.works.repositories._jpa.JpaProductRepository;
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
public class ProductDto {

    final JpaProductRepository jpaProductRepository;
    final UtilService utilService;
    final ElasticProductRepository elasticProductRepository;

    public ProductDto(JpaProductRepository jpaProductRepository, UtilService utilService, ElasticProductRepository elasticProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
        this.utilService = utilService;
        this.elasticProductRepository = elasticProductRepository;
    }

    // Product -> Add
    public Map<ERest, Object> add(@Valid Product product, BindingResult bindingResult) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            try {
                hm.put(ERest.status, true);
                hm.put(ERest.message, "Ürün Ekleme Başarılı");
                Product  product1 = jpaProductRepository.save(product);
                hm.put(ERest.result, product1);
                ElasticProduct elasticProduct = new ElasticProduct();
                elasticProduct.setPid(Integer.toString(product1.getProId()));
                elasticProduct.setProName(product1.getProName());
                elasticProduct.setProUnit(product1.getProUnit());
                elasticProduct.setProCategory(product1.getProCategory());
                elasticProduct.setProDetail(product1.getProDetail());
                elasticProduct.setProType(product1.getProType());
                elasticProduct.setProSupplier(product1.getProSupplier());
                elasticProduct.setProBarcode(Integer.toString(product1.getProBarcode()));
                elasticProduct.setProCode(Integer.toString(product1.getProCode()));
                elasticProduct.setProTax(product1.getProTax());
                elasticProductRepository.save(elasticProduct);
            } catch (Exception ex) {
                hm.put(ERest.status, false);
                if (ex.toString().contains("constraint")) {
                    String error = "proBarcode ve proCode Bilgisi (" + product.getProBarcode() + " , " + product.getProCode() + ") ile daha önce kayıt yapılmış";
                    hm.put(ERest.message, error);
                }
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, utilService.errors(bindingResult));
        }
        return hm;
    }

    // Product -> Delete
    public Map<ERest, Object> delete(Integer proId) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            if (jpaProductRepository.existsById(proId)) {
                jpaProductRepository.deleteById(proId);
                hm.put(ERest.status, true);
                hm.put(ERest.message, "silme başarılı");
                hm.put(ERest.result, proId);

            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "silme başarısız, girilen Id geçersiz");
                hm.put(ERest.result, proId);
            }
        } catch (Exception ex) {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "silme sırasında hata oluştu");
            hm.put(ERest.result, proId);
        }
        return hm;
    }

    // Product -> List
    public Map<ERest, Object> list() {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        hm.put(ERest.status,true);
        List<Product> ls = jpaProductRepository.findAll();
        hm.put(ERest.result, ls);
        return hm;
    }

    // Product -> Update
    public Map<ERest, Object> update(Product product) {
        Map<ERest, Object> hm = new LinkedHashMap<>();

        if (product.getProId() != null) {
            Optional<Product> optionalProduct = jpaProductRepository.findById(product.getProId());
            if (optionalProduct.isPresent()) {
                try {
                    jpaProductRepository.saveAndFlush(product);
                    hm.put(ERest.status, true);
                    hm.put(ERest.message, "Güncelleme başarılı");
                    hm.put(ERest.result, product);
                } catch (Exception ex) {
                    hm.put(ERest.status, false);
                    if (ex.toString().contains("constraint")) {
                        String error = "proBarcode ve proCode Bilgisi (" + product.getProBarcode() + " , " + product.getProCode() + ") ile daha önce kayıt yapılmış";
                        hm.put(ERest.message, error);
                    }
                }
            } else {
                hm.put(ERest.status, false);
                hm.put(ERest.message, "Update işlemi sırasında hata oluştu!");
                hm.put(ERest.result, product);
            }
        } else {
            hm.put(ERest.status, false);
            hm.put(ERest.message, "Update işlemi sırasında hata oluştu!!!");
            hm.put(ERest.result, product);
        }
        return hm;
    }

    // Product -> Pagination
    public Map<ERest, Object> pageList( Integer pageNumber ) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            Pageable pageable = PageRequest.of(pageNumber,10);
            List<Product> pageList = jpaProductRepository.findByOrderByProIdAsc(pageable);
            hm.put(ERest.status, true);
            hm.put(ERest.message,"Sayfalama işlemi başarılı");
            hm.put(ERest.result, pageList);
        }catch (Exception ex){

        }
        return hm;
    }


    // Product -> ElasticSearch
    public Map<String, Object> search(String data,int pageNo){
        Map<String, Object> hm = new LinkedHashMap<>();
        Page<ElasticProduct> searchPage = elasticProductRepository.findByProduct(data, PageRequest.of(pageNo, 10));
        hm.put("totalPages", searchPage.getTotalPages());
        hm.put("pagesSearchResult",searchPage.getContent());
        return hm;
    }
}

