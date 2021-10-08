package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.TreatmentDto;
import com.works.entities.Treatment;
import com.works.repositories._jpa.JpaTreatmentRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/treatment")
public class TreatmentRestController {

    final JpaTreatmentRepository jpaTreatmentRepository;
    final UtilService utilService;
    final TreatmentDto treatmentDto;

    public TreatmentRestController(JpaTreatmentRepository jpaTreatmentRepository, UtilService utilService, TreatmentDto treatmentDto) {
        this.jpaTreatmentRepository = jpaTreatmentRepository;
        this.utilService = utilService;
        this.treatmentDto = treatmentDto;
    }

    // Treatment -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Treatment treatment, BindingResult bindingResult){
        return treatmentDto.add(treatment,bindingResult);
    }

    // Treatment -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return treatmentDto.list();
    }

    // Treatment -> Delete
    @DeleteMapping("/delete/{treId}")
    public Map<ERest,Object> delete(@PathVariable Integer treId) {
        return treatmentDto.delete(treId);
    }

    // Treatment -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Treatment treatment) {
        return treatmentDto.update(treatment);
    }

    // Treatment -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return treatmentDto.pageList(pageNumber);
    }

    // Treatment -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return treatmentDto.search(data,pageNo);
    }

}
