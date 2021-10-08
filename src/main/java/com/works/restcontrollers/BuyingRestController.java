package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.BuyingDto;
import com.works.entities.Buying;
import com.works.entities.Sales;
import com.works.repositories._jpa.JpaBuyingRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/buying")
public class BuyingRestController {

    final JpaBuyingRepository jpaBuyingRepository;
    final UtilService utilService;
    final BuyingDto buyingDto;

    public BuyingRestController(JpaBuyingRepository jpaBuyingRepository, UtilService utilService, BuyingDto buyingDto) {
        this.jpaBuyingRepository = jpaBuyingRepository;
        this.utilService = utilService;
        this.buyingDto = buyingDto;
    }

    // Buying -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Buying buying, BindingResult bindingResult){
        return buyingDto.add(buying,bindingResult);
    }

    // Buying -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return buyingDto.list();
    }

    // Buying -> Delete
    @DeleteMapping("/delete/{buyId}")
    public Map<ERest,Object> delete(@PathVariable Integer buyId) {
        return buyingDto.delete(buyId);
    }

    // Buying -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Buying buying) {
        return buyingDto.update(buying);
    }

    // Buying -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return buyingDto.pageList(pageNumber);
    }

    // Buying -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return buyingDto.search(data,pageNo);
    }
}
