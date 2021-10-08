package com.works.dto;

import com.works.Utils.ERest;
import com.works.Utils.Util;
import com.works.entities.Customer;
import com.works.entities.Laboratory;
import com.works.entities.Patient;
import com.works.models.ElasticLaboratory;
import com.works.repositories._elastic.ElasticLaboratoryRepository;
import com.works.repositories._jpa.JpaCustomerRepository;
import com.works.repositories._jpa.JpaLaboratoryRepository;
import com.works.repositories._jpa.JpaPatientRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class LaboratoryDto {

    final JpaLaboratoryRepository jpaLaboratoryRepository;
    final JpaCustomerRepository jpaCustomerRepository;
    final JpaPatientRepository jpaPatientRepository;
    final ElasticLaboratoryRepository elasticLaboratoryRepository;

    public LaboratoryDto(JpaLaboratoryRepository jpaLaboratoryRepository, JpaCustomerRepository jpaCustomerRepository, JpaPatientRepository jpaPatientRepository, ElasticLaboratoryRepository elasticLaboratoryRepository) {
        this.jpaLaboratoryRepository = jpaLaboratoryRepository;
        this.jpaCustomerRepository = jpaCustomerRepository;
        this.jpaPatientRepository = jpaPatientRepository;
        this.elasticLaboratoryRepository = elasticLaboratoryRepository;
    }


    // Laboratory -> Add
    public Map<ERest,Object> add(MultipartFile file, Laboratory laboratory, BindingResult bindingResult){
        long maxFileUploadSize = 2048;
        int sendSuccessCount = 0;
        String errorMessage = "";
        Map<ERest,Object> hm = new LinkedHashMap<>();
        if(!bindingResult.hasErrors()){
            if (!file.isEmpty() ){
                long fileSizeMB = file.getSize() / 1024;
                if ( fileSizeMB > maxFileUploadSize ) {
                    System.err.println("Dosya boyutu çok büyük Max 2MB");
                    errorMessage = "Dosya boyutu çok büyük Max "+ (maxFileUploadSize / 1024) +"MB olmalıdır";
                }else {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String ext = fileName.substring(fileName.length()-5, fileName.length());
                    String uui = UUID.randomUUID().toString();
                    fileName = uui + ext;
                    try {
                        Path path = Paths.get(Util.UPLOAD_DIR + fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        sendSuccessCount += 1;
                        laboratory.setLabFileName(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Customer customer = jpaCustomerRepository.findById(laboratory.getLabCuId()).get();
                    Patient patient = jpaPatientRepository.findById(laboratory.getLabPaId()).get();
                    laboratory.setCustomer(customer);
                    laboratory.setPatient(patient);
                    Laboratory lab = jpaLaboratoryRepository.save(laboratory);
                    hm.put(ERest.status,true);
                    hm.put(ERest.message,"Laboratuvar sonucu ekleme işlemi başarılı!");
                    hm.put(ERest.result,lab);
                    ElasticLaboratory elasticLaboratory = new ElasticLaboratory();
                    elasticLaboratory.setLabId(lab.getLabId());
                    elasticLaboratory.setCuname(lab.getCustomer().getCuName());
                    elasticLaboratory.setPaname(lab.getPatient().getPaName());
                    elasticLaboratory.setPaAirTagNo(lab.getPatient().getPaAirTagNo());
                    elasticLaboratoryRepository.save(elasticLaboratory);
                } catch (Exception e) {
                    hm.put(ERest.status,false);
                    String error = "Laboratuvar sonucu eklerken bir hata oluştu!";
                    Util.logger(error, Laboratory.class);
                    hm.put(ERest.message,error);
                }
            }else{
                String error = "Lütfen bir dosya seçiniz!";
                hm.put(ERest.status,false);
                Util.logger(error, Laboratory.class);
                hm.put(ERest.message,error);
            }
        }else {
            hm.put(ERest.status,false);
            hm.put(ERest.errors, Util.errors(bindingResult));
        }
        return hm;
    }

    // Laboratory -> Pagination
    public Map<ERest,Object> list(String stPage){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<Laboratory> laboratoryList = new ArrayList<>();
        try {
            int page = Integer.parseInt(stPage);
            Pageable pageable = PageRequest.of(page-1,Util.pageSize);
            laboratoryList = jpaLaboratoryRepository.findByOrderByLabIdAsc(pageable);
            hm.put(ERest.status,true);
            hm.put(ERest.message,(page) + " sayfadaki laboratuvar sonucu listeleme işlemi başarılı!");
            hm.put(ERest.result,laboratoryList);
        } catch (Exception e) {
            String error = "Listeleme sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,Laboratory.class);
        }
        return hm;
    }

    // Laboratory -> result detail
    public Map<ERest,Object> detail(String stId){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Laboratory> optDetail = jpaLaboratoryRepository.findById(id);
            if(optDetail.isPresent()){
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Laboratuvar sonucu detayı başarılı bir şekilde getirildi.");
                hm.put(ERest.result,optDetail.get());
            }else{
                String error = "Laboratuvar sonucu bulunamadı!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                Util.logger(error,Laboratory.class);
            }
        } catch (Exception e) {
            String error = "Laboratuvar sonuç detayı getirilirken bir hata oluştu!";
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,Laboratory.class);
        }
        return hm;
    }

    //Laboratory delete
    public Map<ERest,Object> delete(String stId){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<Laboratory> optLaboratory = jpaLaboratoryRepository.findById(id);
            if(optLaboratory.isPresent()){
                //Elasticsearch database and MySQL database delete data.
                ElasticLaboratory elasticLaboratory = elasticLaboratoryRepository.findById(id).get();
                jpaLaboratoryRepository.deleteById(id);
                elasticLaboratoryRepository.deleteById(elasticLaboratory.getId());
                //File Delete
                String deleteFilePath = optLaboratory.get().getLabFileName();
                File file = new File(Util.UPLOAD_DIR + deleteFilePath);
                file.delete();
                //File Delete
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Silme işlemi başarılı!");
                hm.put(ERest.result,optLaboratory.get());
            }else {
                String error = "Silmek istenen laboratuvar sonucu bulunamadı!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                Util.logger(error,Laboratory.class);
            }
        } catch (Exception e) {
            String error = "Silme işlemi sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,Laboratory.class);
        }
        return hm;
    }

}