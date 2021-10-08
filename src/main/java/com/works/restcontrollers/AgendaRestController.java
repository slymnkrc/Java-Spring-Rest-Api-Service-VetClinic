package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.AgendaDto;
import com.works.entities.Agenda;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/agenda")
@Api(value = "AgendaRestController",authorizations = {@Authorization(value = "basicAuth")})
public class AgendaRestController {

    final AgendaDto agendaDto;

    public AgendaRestController(AgendaDto agendaDto) {
        this.agendaDto = agendaDto;
    }


    //Agenda add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Agenda agenda, BindingResult bindingResult){
        return agendaDto.add(agenda,bindingResult);
    }

    //Agenda -> List
    @GetMapping("/list")
    public Map<ERest,Object> list(){
        return agendaDto.list();
    }

    // Agenda -> delete
    @DeleteMapping("/delete/{stId}")
    public Map<ERest,Object> delete(@PathVariable String stId){
        return agendaDto.delete(stId);
    }

    // Agenda -> update
    @PutMapping("/update")
    public Map<ERest,Object> update(@RequestBody @Valid Agenda agenda,BindingResult bindingResult){
        return agendaDto.update(agenda,bindingResult);
    }

}

