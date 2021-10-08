package com.works.restcontrollers;


import com.works.Utils.ERest;
import com.works.dto.CalendarDto;
import com.works.entities.ScheduleCalendar;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/calendar")
@Api(value = "CalendarRestController",authorizations = {@Authorization(value = "basicAuth")})
public class CalendarRestController {

    final CalendarDto calendarDto;

    public CalendarRestController(CalendarDto calendarDto) {
        this.calendarDto = calendarDto;
    }

    // CalendarDetail -> List
    @GetMapping("/list")
    public Map<String, Object> list(){
        return calendarDto.list();
    }

    // Calendar -> Add
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ScheduleCalendar scheduleCalendar) {
        return calendarDto.add(scheduleCalendar);
    }

    // Calendar -> Pagination
    @GetMapping("/list/{cId}")
    public Map<String, Object> pageList(@PathVariable String cId) {
        return calendarDto.pageList(cId);
    }

    // Calendar -> Delete
    @DeleteMapping("/delete/{cId}")
    public Map<ERest, Object> delete(@PathVariable String cId) {
        return calendarDto.delete(cId);
    }

}
