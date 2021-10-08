package com.works.repositories._jpa;


import com.works.entities.CalendarInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCalendarInfoRepository extends JpaRepository<CalendarInfo, Integer> {
}
