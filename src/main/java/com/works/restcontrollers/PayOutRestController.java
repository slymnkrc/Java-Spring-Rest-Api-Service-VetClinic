package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.PayOutDto;
import com.works.entities.Buying;
import com.works.entities.PayOut;
import com.works.repositories._jpa.JpaPayOutRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/payout")
public class PayOutRestController {

    final JpaPayOutRepository jpaPayOutRepository;
    final UtilService utilService;
    final PayOutDto payOutDto;

    public PayOutRestController(JpaPayOutRepository jpaPayOutRepository, UtilService utilService, PayOutDto payOutDto) {
        this.jpaPayOutRepository = jpaPayOutRepository;
        this.utilService = utilService;
        this.payOutDto = payOutDto;
    }

    // PayOut -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid PayOut payOut, BindingResult bindingResult){
        return payOutDto.add(payOut,bindingResult);
    }

    // PayOut -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return payOutDto.list();
    }

    // PayOut -> Delete
    @DeleteMapping("/delete/{poId}")
    public Map<ERest,Object> delete(@PathVariable Integer poId) {
        return payOutDto.delete(poId);
    }

    // PayOut -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody PayOut payOut) {
        return payOutDto.update(payOut);
    }

    // PayOut -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return payOutDto.pageList(pageNumber);
    }

    // PayOut -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return payOutDto.search(data,pageNo);
    }
}
