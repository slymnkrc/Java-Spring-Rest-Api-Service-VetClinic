package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.VaccineDto;
import com.works.entities.Product;
import com.works.entities.Vaccine;
import com.works.repositories._elastic.ElasticVaccineRepository;
import com.works.repositories._jpa.JpaVaccineRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/vaccine")
public class VaccineRestController {

    final JpaVaccineRepository jpaVaccineRepository;
    final UtilService utilService;
    final VaccineDto vaccineDto;

    public VaccineRestController(JpaVaccineRepository jpaVaccineRepository, UtilService utilService, VaccineDto vaccineDto) {
        this.jpaVaccineRepository = jpaVaccineRepository;
        this.utilService = utilService;
        this.vaccineDto = vaccineDto;
    }

    // Vaccine -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Vaccine vaccine, BindingResult bindingResult){
        return vaccineDto.add(vaccine,bindingResult);
    }

    // Vaccine -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return vaccineDto.list();
    }

    // Vaccine -> Delete
    @DeleteMapping("/delete/{vacid}")
    public Map<ERest,Object> delete(@PathVariable Integer vacid) {
        return vaccineDto.delete(vacid);
    }

    // Vaccine -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Vaccine vaccine) {
        return vaccineDto.update(vaccine);
    }

    // Vaccine -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return vaccineDto.pageList(pageNumber);
    }

    // Vaccine -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return vaccineDto.search(data,pageNo);
    }
}
