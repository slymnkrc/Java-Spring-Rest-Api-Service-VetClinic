package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.PayInDto;
import com.works.dto.PayOutDto;
import com.works.entities.PayIn;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/payIn")
@Api(value = "PayInRestController",authorizations = {@Authorization(value = "basicAuth")})
public class PayInRestController {

    final PayInDto payInDto;
    final PayOutDto payOutDto;

    public PayInRestController(PayInDto payInDto, PayOutDto payOutDto) {
        this.payInDto = payInDto;
        this.payOutDto = payOutDto;
    }

    //PayIn -> add
    @PostMapping("/add")
    public Map<ERest, Object> addPayIn(@RequestBody @Valid PayIn payIn, BindingResult bindingResult) {
        return payInDto.addPayIn(payIn, bindingResult);
    }

    // PayIn -> List
    @GetMapping("/list")
    public Map<ERest, Object> listAllPayIn() {
        return payInDto.listAll();
    }

    //PayInByCustomerId
    @GetMapping("/invoiceList/{stId}")
    public Map<ERest, Object> invoiceListPayIn(@PathVariable String stId) {
        return payInDto.invoiceList(stId);
    }

    // PayIn -> delete
    @DeleteMapping("/delete/{stId}")
    public Map<ERest, Object> deletePayIn(@PathVariable String stId) {
        return payInDto.delete(stId);
    }

    // PayIn -> update
    @PutMapping("/update")
    public Map<ERest, Object> updatePayIn(@RequestBody @Valid PayIn payIn, BindingResult bindingResult) {
        return payInDto.update(payIn, bindingResult);
    }
}
