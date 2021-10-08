package com.works.repositories._jpa;

import com.works.entities.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaAgendaRepository extends JpaRepository<Agenda,Integer> {

    List<Agenda> findByUidEquals(Integer uid);

    List<Agenda> findByUid(Integer uid);

}

