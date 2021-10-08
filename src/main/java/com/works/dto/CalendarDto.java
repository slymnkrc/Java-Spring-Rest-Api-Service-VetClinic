package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.ScheduleCalendar;
import com.works.repositories._jpa.JpaCalendarInfoRepository;
import com.works.repositories._jpa.JpaScheduleCalendarRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CalendarDto {
    final JpaCalendarInfoRepository jpaCalendarInfoRepository;
    final JpaScheduleCalendarRepository jpaScheduleCalendarRepository;

    public CalendarDto(JpaCalendarInfoRepository jpaCalendarInfoRepository, JpaScheduleCalendarRepository jpaScheduleCalendarRepository) {
        this.jpaCalendarInfoRepository = jpaCalendarInfoRepository;
        this.jpaScheduleCalendarRepository = jpaScheduleCalendarRepository;
    }


    // CalendarDetail -> List
    public Map<String, Object> list() {
        Map<String, Object> hm = new LinkedHashMap<>();
        hm.put("calendarInfos", jpaCalendarInfoRepository.findAll());
        return hm;
    }

    public Map<String, Object> add(ScheduleCalendar scheduleCalendar) {
        Map<String, Object> hm = new LinkedHashMap<>();
        Optional<ScheduleCalendar> isThere = jpaScheduleCalendarRepository.findScheduleId(scheduleCalendar.getId());
        if (isThere.isPresent()) {
            scheduleCalendar.setSid(isThere.get().getSid());
        }
        ScheduleCalendar s = jpaScheduleCalendarRepository.saveAndFlush(scheduleCalendar);
        hm.put("scheduleCalendar", s);
        return hm;
    }

    // Calendar -> Pagination
    public Map<String, Object> pageList(String calendarId) {
        Map<String, Object> hm = new LinkedHashMap<>();
        List<ScheduleCalendar> s = jpaScheduleCalendarRepository.findByCalendarIdEquals(calendarId);
        hm.put("listSchedule", s);
        return hm;
    }

    // Calendar -> Delete
    public Map<ERest, Object> delete(String stSid) {
        Map<ERest, Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stSid);
            Optional<ScheduleCalendar> optSchedule = jpaScheduleCalendarRepository.findById(id);
            if (optSchedule.isPresent()) {
                ScheduleCalendar sc = optSchedule.get();
                jpaScheduleCalendarRepository.deleteById(sc.getSid());
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Silme işlemi başarılı!");
                hm.put(ERest.result,sc);
            }else {
                hm.put(ERest.status,false);
                hm.put(ERest.message,"Silmek istenen randevu bulunamadı!");
            }
        } catch (NumberFormatException e) {
            hm.put(ERest.status,false);
            hm.put(ERest.message,"Silme işlemi sırasında bir hata oluştu!");
        }
        return hm;
    }

}
