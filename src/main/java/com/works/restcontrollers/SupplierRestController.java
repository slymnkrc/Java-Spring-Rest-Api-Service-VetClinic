package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.SupplierDto;
import com.works.entities.Supplier;
import com.works.repositories._jpa.JpaSupplierRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/supplier")
public class SupplierRestController {

    final JpaSupplierRepository jpaSupplierRepository;
    final UtilService util;
    final SupplierDto supplierDto;

    public SupplierRestController(JpaSupplierRepository jpaSupplierRepository, UtilService util, SupplierDto supplierDto) {
        this.jpaSupplierRepository = jpaSupplierRepository;
        this.util = util;
        this.supplierDto = supplierDto;
    }
    // Supplier -> Add
    @PostMapping("/add")
    public Map<ERest, Object> add(@RequestBody @Valid Supplier supplier, BindingResult bindingResult) {
        return supplierDto.add(supplier, bindingResult);
    }

    // Supplier -> List
    @GetMapping("/list")
    public Map<ERest, Object> list() {
        return supplierDto.list();
    }

    // Supplier -> Delete
    @DeleteMapping("/delete/{supId}")
    public Map<ERest, Object> delete(@PathVariable Integer supId) {
        return supplierDto.delete(supId);
    }

    //Supplier -> Update
    @PutMapping("/update")
    public Map<ERest, Object> update(@RequestBody Supplier supplier) {
        return supplierDto.update(supplier);
    }

    // Supplier -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest, Object> pageList( @PathVariable Integer pageNumber ) {
        return supplierDto.pageList(pageNumber);
    }

    // Supplier -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data,@PathVariable int pageNo){
        return supplierDto.search(data,pageNo);
    }

}
