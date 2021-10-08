package com.works.dto;

import com.works.Utils.ERest;
import com.works.entities.*;
import com.works.repositories._jpa.*;
import com.works.services.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomeDto {

    final UserService userService;
    final JpaScheduleCalendarRepository jpaScheduleCalendarRepository;
    final JpaPayInRepository jpaPayInRepository;
    final JpaPayOutRepository jpaPayOutRepository;
    final JpaSalesRepository jpaSalesRepository;
    final JpaCustomerRepository jpaCustomerRepository;
    final JpaProductRepository jpaProductRepository;
    final JpaPatientRepository jpaPatientRepository;

    public HomeDto(UserService userService, JpaScheduleCalendarRepository jpaScheduleCalendarRepository, JpaPayInRepository jpaPayInRepository, JpaPayOutRepository jpaPayOutRepository, JpaSalesRepository jpaSalesRepository, JpaCustomerRepository jpaCustomerRepository, JpaProductRepository jpaProductRepository, JpaPatientRepository jpaPatientRepository) {
        this.userService = userService;
        this.jpaScheduleCalendarRepository = jpaScheduleCalendarRepository;
        this.jpaPayInRepository = jpaPayInRepository;
        this.jpaPayOutRepository = jpaPayOutRepository;
        this.jpaSalesRepository = jpaSalesRepository;
        this.jpaCustomerRepository = jpaCustomerRepository;
        this.jpaProductRepository = jpaProductRepository;
        this.jpaPatientRepository = jpaPatientRepository;
    }


    //--------------------------- Charts - Table - Statistics - Start ---------------------------//

    //Kazançlar Grafiği
    public Map<ERest,Object> priceChart(){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<PayIn> payInList = jpaPayInRepository.findAll();
        List<PayOut> payOutList = jpaPayOutRepository.findAll();
        if(payInList != null && payOutList != null){
            Integer payInTotal = payInList.stream().mapToInt(PayIn::getPinAmount).sum(); //Kasa giriş toplam
            Integer payOutTotal = payOutList.stream().mapToInt(PayOut::getPoutAmount).sum(); //Kasa çıkış toplam
            Integer netEarning = payInTotal - payOutTotal; //Net kar
            hm.put(ERest.status,true);
            hm.put(ERest.message,"Grafik verileri başarılı bir şekilde getirildi!");
            hm.put(ERest.payInTotal,payInTotal);
            hm.put(ERest.payOutTotal,payOutTotal);
            hm.put(ERest.netEarning,netEarning);
        }else{
            hm.put(ERest.status,false);
            hm.put(ERest.message,"Grafik verileri getirilirken bir hata oluştu!");
        }
        return hm;
    }

    //Genel İstatistikler Bilgisi
    public Map<ERest,Object> generalStatics(){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<Sales> salesList = jpaSalesRepository.findAll();
        List<Customer> cuList = jpaCustomerRepository.findAll();
        List<Product> productList = jpaProductRepository.findAll();
        if(salesList != null && cuList != null && productList != null){
            hm.put(ERest.status,true);
            hm.put(ERest.message,"İstatistik verileri başarılı bir şekilde getirildi!");
            hm.put(ERest.salesCount,salesList.size());
            hm.put(ERest.customerCount,cuList.size());
            hm.put(ERest.productCount,productList.size());
        }else{
            hm.put(ERest.status,false);
            hm.put(ERest.message,"İstatistik verileri getirilirken bir hata oluştu!");
        }
        return hm;
    }

    //Günlük Giriş Yapan Hasta Bilgileri Tablosu
    public  Map<ERest,Object> patientList(){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        Date date = new Date();
        String dateFormat = new SimpleDateFormat("dd-MM-yyyy").format(date);
        List<Patient> patientList = jpaPatientRepository.findBySaveDateEqualsIgnoreCase(dateFormat);
        if(patientList != null){
            if(patientList.size() > 0){
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Bugün girişi yapılan hastalar başarıyla getirildi!");
                hm.put(ERest.result,patientList);
            }else{
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Bugün giriş yapan hasta bulunmamaktadır!");
            }
        }else{
            hm.put(ERest.status,false);
            hm.put(ERest.message,"Bugün girişi yapılan hastalar getirilirken bir hata oluştu!");
        }
        return hm;
    }

    //--------------------------- Charts - Table - Statistics - End -----------------------------//

    //Günlük Randevu Bilgilendirme Kartı
    public  Map<ERest,Object> scheduleAppointment(String stPageNo){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        try {
            //Date Format
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy ", Locale.ENGLISH);
            String strDate = formatter.format(date);

            int pageNo = Integer.parseInt(stPageNo);
            int totalPage = jpaScheduleCalendarRepository.countByStartContainingAllIgnoreCase(strDate);
            Pageable pageable = PageRequest.of(pageNo-1,1);
            List<ScheduleCalendar> lsCalendar = jpaScheduleCalendarRepository.findByStartContainingAllIgnoreCase(strDate,pageable);
            if(lsCalendar.size() > 0){
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Günlük randevular başarıyla getirildi!");
                hm.put(ERest.totalPage,totalPage);
                hm.put(ERest.result,lsCalendar);
            }else if(pageNo > totalPage){
                hm.put(ERest.status,false);
                hm.put(ERest.totalPage,totalPage);
                hm.put(ERest.message,"Lütfen geçerli bir sayfa numarası giriniz!");
            }
            else{
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Bugüne ait randevu bulunmamaktadır!");
            }
        } catch (Exception e) {
            String error = "Randevular getirilirken bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
        }
        return hm;
    }


}