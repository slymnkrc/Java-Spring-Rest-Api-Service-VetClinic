package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.CustomerDto;
import com.works.entities.Customer;
import com.works.repositories._jpa.JpaCustomerRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    final JpaCustomerRepository jpaCustomerRepository;
    final UtilService utilService;
    final CustomerDto customerDto;

    public CustomerRestController(JpaCustomerRepository jpaCustomerRepository, UtilService utilService, CustomerDto customerDto) {
        this.jpaCustomerRepository = jpaCustomerRepository;
        this.utilService = utilService;
        this.customerDto = customerDto;
    }

    // Customer -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Customer customer, BindingResult bindingResult){
        return customerDto.add(customer,bindingResult);
    }

    // Customer -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return customerDto.list();
    }

    // Customer -> Delete
    @DeleteMapping("/delete/{cuId}")
    public Map<ERest,Object> delete(@PathVariable Integer cuId) {
        return customerDto.delete(cuId);
    }

    // Customer -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Customer customer) {
        return customerDto.update(customer);
    }

    // Customer -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return customerDto.pageList(pageNumber);
    }

    // Customer -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return customerDto.search(data,pageNo);
    }
}
