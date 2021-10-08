package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.SalesDto;
import com.works.entities.Sales;
import com.works.repositories._jpa.JpaSalesRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SalesRestController {

    final JpaSalesRepository jpaSalesRepository;
    final UtilService utilService;
    final SalesDto salesDto;

    public SalesRestController(JpaSalesRepository jpaSalesRepository, UtilService utilService, SalesDto salesDto) {
        this.jpaSalesRepository = jpaSalesRepository;
        this.utilService = utilService;
        this.salesDto = salesDto;
    }

    // Sales -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Sales sales, BindingResult bindingResult){
        return salesDto.add(sales,bindingResult);
    }

    // Sales -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return salesDto.list();
    }

    // Sales -> Delete
    @DeleteMapping("/delete/{saId}")
    public Map<ERest,Object> delete(@PathVariable Integer saId) {
        return salesDto.delete(saId);
    }

    // Sales -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Sales sales) {
        return salesDto.update(sales);
    }

    // Sales -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return salesDto.pageList(pageNumber);
    }

    // Sales -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return salesDto.search(data,pageNo);
    }

}
