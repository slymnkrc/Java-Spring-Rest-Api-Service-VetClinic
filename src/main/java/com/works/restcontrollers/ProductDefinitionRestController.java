package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.ProductDto;
import com.works.entities.Customer;
import com.works.entities.Product;
import com.works.repositories._jpa.JpaProductRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/productDefinition")
public class ProductDefinitionRestController {

    final JpaProductRepository jpaProductRepository;
    final UtilService utilService;
    final ProductDto productDto;

    public ProductDefinitionRestController(JpaProductRepository jpaProductRepository, UtilService utilService, ProductDto productDto) {
        this.jpaProductRepository = jpaProductRepository;
        this.utilService = utilService;
        this.productDto = productDto;
    }

    // Product -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Product product, BindingResult bindingResult){
        return productDto.add(product,bindingResult);
    }

    // Product -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return productDto.list();
    }

    // Product -> Delete
    @DeleteMapping("/delete/{proId}")
    public Map<ERest,Object> delete(@PathVariable Integer proId) {
        return productDto.delete(proId);
    }

    // Product -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Product product) {
        return productDto.update(product);
    }

    // Product -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return productDto.pageList(pageNumber);
    }

    // Product -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return productDto.search(data,pageNo);
    }

}
