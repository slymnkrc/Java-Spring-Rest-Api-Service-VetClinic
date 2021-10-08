package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.PatientDto;
import com.works.entities.Customer;
import com.works.entities.Patient;
import com.works.repositories._jpa.JpaPatientRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatinetRestController {

    final JpaPatientRepository jpaPatientRepository;
    final PatientDto patientDto;
    final UtilService utilService;

    public PatinetRestController(JpaPatientRepository jpaPatientRepository, PatientDto patientDto, UtilService utilService) {
        this.jpaPatientRepository = jpaPatientRepository;
        this.patientDto = patientDto;
        this.utilService = utilService;
    }

    // Patient -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Patient patient, BindingResult bindingResult){
        return patientDto.add(patient,bindingResult);
    }

    // Patient -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return patientDto.list();
    }

    // Patient -> Delete
    @DeleteMapping("/delete/{paId}")
    public Map<ERest,Object> delete(@PathVariable Integer paId) {
        return patientDto.delete(paId);
    }

    // Patient -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Patient patient) {
        return patientDto.update(patient);
    }

    // Patient -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return patientDto.pageList(pageNumber);
    }

    // Patient -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return patientDto.search(data,pageNo);
    }

    // PatientByCustomerId -> List
    @GetMapping("/list/patientsbycustomerid/{cuId}")
    public Map<ERest,Object> listByCustomer(@PathVariable Integer cuId) {
        return patientDto.listByCustomer(cuId);
    }

}
