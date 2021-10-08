package com.works.repositories._jpa;

import com.works.entities.Laboratory;
import com.works.projection.LaboratoryInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaLaboratoryRepository extends JpaRepository<Laboratory,Integer> {

    List<Laboratory> findByOrderByLabIdAsc(Pageable pageable);

    @Query(value = "SELECT lab.lab_id as LabId, lab.lab_cu_id as LabCuId, lab.lab_pa_id as LabPaId, CONCAT(c.cu_name,' ',c.cu_surname) as CuNameSurname, p.pa_name as PaName, p.pa_air_tag_no as PaAirTagNo, lab.lab_type as LabType, lab.lab_note as LabNote, lab.lab_date as LabDate FROM laboratory as lab INNER JOIN customer as c on c.cu_id = lab_cu_id INNER JOIN patient as p on p.pa_id = lab_pa_id",nativeQuery = true)
    List<LaboratoryInfo> labInfo();

}
