package com.works.repositories._jpa;


import com.works.entities.ScheduleCalendar;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaScheduleCalendarRepository extends JpaRepository<ScheduleCalendar, Integer> {

    List<ScheduleCalendar> findByCalendarIdEquals(String calendarId);

    List<ScheduleCalendar> findByStartContainingAllIgnoreCase(String start, Pageable pageable);

    int countByStartContainingAllIgnoreCase(String start);

    @Query(value = "SELECT * from Schedule_Calendar where id = ?1", nativeQuery = true)
    Optional<ScheduleCalendar> findScheduleId(String id);

}
