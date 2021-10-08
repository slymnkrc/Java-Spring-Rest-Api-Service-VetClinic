package com.works.dto;

import com.works.Utils.ERest;
import com.works.Utils.Util;
import com.works.entities.PayIn;
import com.works.entities.Sales;
import com.works.entities.SalesPayment;
import com.works.models.ElasticPayIn;
import com.works.repositories._elastic.ElasticPayInRepository;
import com.works.repositories._elastic.ElasticPayOutRepository;
import com.works.repositories._jpa.JpaPayInRepository;
import com.works.repositories._jpa.JpaPayOutRepository;
import com.works.repositories._jpa.JpaSalesPaymentRepository;
import com.works.repositories._jpa.JpaSalesRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class PayInDto {

    final JpaPayInRepository jpaPayinRepository;
    final JpaPayOutRepository jpaPayoutRepository;
    final JpaSalesRepository jpaSalesRepository;
    final JpaSalesPaymentRepository jpaSalesPaymentRepository;
    final ElasticPayInRepository elasticPayInRepository;
    final ElasticPayOutRepository elasticPayOutRepository;

    public PayInDto(JpaPayInRepository jpaPayinRepository, JpaPayOutRepository jpaPayoutRepository, JpaSalesRepository jpaSalesRepository, JpaSalesPaymentRepository jpaSalesPaymentRepository, ElasticPayInRepository elasticPayInRepository, ElasticPayOutRepository elasticPayOutRepository) {
        this.jpaPayinRepository = jpaPayinRepository;
        this.jpaPayoutRepository = jpaPayoutRepository;
        this.jpaSalesRepository = jpaSalesRepository;
        this.jpaSalesPaymentRepository = jpaSalesPaymentRepository;
        this.elasticPayInRepository = elasticPayInRepository;
        this.elasticPayOutRepository = elasticPayOutRepository;
    }


    // PayIn -> Add
    public Map<ERest,Object> addPayIn(PayIn payIn, BindingResult bindingResult){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        if(!bindingResult.hasErrors()){
            try {
                SalesPayment salesPayment = jpaSalesPaymentRepository.findBySales_SaIdEquals(payIn.getSales().getSaId());
                if (salesPayment.getRemainDebt() == 0) {
                    hm.put(ERest.status,false);
                    hm.put(ERest.message,"Borç Kalmamıştır");
                }
                else {
                    //Kalan para ve Borç İşlemleri - Start
                    if (payIn.getPinId() != null){//update
                        PayIn payInEntity = jpaPayinRepository.getById(payIn.getPinId());
                        if (salesPayment.getRemainDebt() + payInEntity.getPinAmount() >= payIn.getPinAmount()) {
                            salesPayment.setRemainDebt(salesPayment.getRemainDebt() + payInEntity.getPinAmount() - payIn.getPinAmount());
                        }
                        payInEntity.setPinAmount(payIn.getPinAmount());
                        jpaPayinRepository.save(payInEntity);
                        hm.put(ERest.status,true);
                        hm.put(ERest.message,"Kasa giriş işlemi başarılı!");
                        hm.put(ERest.result,payInEntity);
                        //Elasticsearch save
                        ElasticPayIn elasticPayIn = new ElasticPayIn();
                        elasticPayIn.setPinId(Integer.toString(payInEntity.getPinId()));
                        elasticPayIn.setCuname(payInEntity.getSales().getCustomer().getCuName());
                        elasticPayIn.setInvoice(payInEntity.getSales().getSaReceiptNo());
                        elasticPayInRepository.save(elasticPayIn);
                    }else {//add
                        //kalan borç ödenen borçtan düşük veya eşitse:
                        if (salesPayment.getRemainDebt() >= payIn.getPinAmount()) {
                            salesPayment.setRemainDebt(salesPayment.getRemainDebt() - payIn.getPinAmount());
                            PayIn p = jpaPayinRepository.save(payIn);
                            hm.put(ERest.status,true);
                            hm.put(ERest.message,"Kasa giriş işlemi başarılı!");
                            hm.put(ERest.result,p);
                            //Elasticsearch save
                            ElasticPayIn elasticPayIn = new ElasticPayIn();
                            elasticPayIn.setPinId(Integer.toString(p.getPinId()));
                            elasticPayIn.setCuname(p.getSales().getCustomer().getCuName());
                            elasticPayIn.setInvoice(p.getSales().getSaReceiptNo());
                            elasticPayInRepository.save(elasticPayIn);
                        } else {
                            String error = "Ödenen toplam miktar borçtan daha fazla!";
                            hm.put(ERest.status,false);
                            hm.put(ERest.message,error);
                            Util.logger(error,PayIn.class);
                        }
                    }
                    //Kalan para ve Borç İşlemleri - End
                }
            } catch (Exception e) {
                hm.put(ERest.status,false);
                String error = "Kasa giriş işlemi sırasında bir hata oluştu!";
                Util.logger(error, PayIn.class);
                hm.put(ERest.message,error);
            }
        }else {
            hm.put(ERest.status,false);
            hm.put(ERest.errors, Util.errors(bindingResult));
        }
        return hm;
    }

    // PayIn -> List
    public Map<ERest,Object> listAll(){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<PayIn> payInList = new ArrayList<>();
        try {
            payInList = jpaPayinRepository.findAll();
            hm.put(ERest.status,true);
            hm.put(ERest.message,"Kasa giriş listeleme işlemi başarılı!");
            hm.put(ERest.result,payInList);
        } catch (Exception e) {
            String error = "Listeleme sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,PayIn.class);
        }
        return hm;
    }

    // PayInByCustomerID
    public Map<ERest,Object> invoiceList(String stId){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<Sales> salesList = new ArrayList<>();
        try {
            int id = Integer.parseInt(stId);
            salesList = jpaSalesRepository.findByCustomer_CuIdEquals(id);
            hm.put(ERest.status,true);
            hm.put(ERest.message,"Müşteri fatura listeleme işlemi başarılı!");
            hm.put(ERest.result,salesList);
        } catch (Exception e) {
            String error = "Listeleme sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error, Sales.class);
        }
        return hm;
    }

    // PayIn -> delete
    public Map<ERest,Object> delete(String stId){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<PayIn> optPayIn = jpaPayinRepository.findById(id);
            if(optPayIn.isPresent()){
                //Elasticsearch database and MySQL database delete data.
                ElasticPayIn epayin = elasticPayInRepository.findById(stId).get();
                jpaPayinRepository.deleteById(id);
                elasticPayInRepository.deleteById(epayin.getPinId());
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Silme işlemi başarılı!");
                hm.put(ERest.result,optPayIn.get());
            }else {
                String error = "Silmek istenen kasa giriş verisi bulunamadı!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                Util.logger(error,PayIn.class);
            }
        } catch (Exception e) {
            String error = "Silme işlemi sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,PayIn.class);
        }
        return hm;
    }

    // PayIn -> update
    public Map<ERest,Object> update(PayIn payIn,BindingResult bindingResult){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        SalesPayment salesPayment = jpaSalesPaymentRepository.findBySales_SaIdEquals(payIn.getSales().getSaId());
        if(payIn.getPinId() != null && !bindingResult.hasErrors()){
            Optional<PayIn> optPayIn = jpaPayinRepository.findById(payIn.getPinId());
            if(optPayIn.isPresent()){
                try {
                    PayIn payin = jpaPayinRepository.getById(payIn.getPinId());
                    if (salesPayment.getRemainDebt() + payin.getPinAmount() >= payIn.getPinAmount()) {
                        salesPayment.setRemainDebt(salesPayment.getRemainDebt() + payin.getPinAmount() - payIn.getPinAmount());
                    }
                    payin.setPinAmount(payIn.getPinAmount());
                    //ElasticSearch and SQL DB Update -Start
                    ElasticPayIn epayin = elasticPayInRepository.findById(Integer.toString(payIn.getPinId())).get();
                    elasticPayInRepository.deleteById(epayin.getPinId());
                    payin = jpaPayinRepository.saveAndFlush(payIn);
                    ElasticPayIn epayinNew = new ElasticPayIn();
                    epayinNew.setPinId(Integer.toString(payin.getPinId()));
                    epayinNew.setCuname(payin.getSales().getCustomer().getCuName());
                    epayinNew.setInvoice(payin.getSales().getSaReceiptNo());
                    elasticPayInRepository.save(epayinNew);
                    //ElasticSearch and SQL DB Update - End
                    hm.put(ERest.status,true);
                    hm.put(ERest.message,"Güncelleme işlemi başarılı!");
                    hm.put(ERest.result,payin);
                } catch (Exception e) {
                    String error = "Güncelleme işlemi sırasında bir hata oluştu! " + e + " ";
                    hm.put(ERest.status,false);
                    hm.put(ERest.message,error);
                    hm.put(ERest.result,payIn);
                    Util.logger(error,PayIn.class);
                }
            }else{
                String error = "Güncelleme işlemi yapılacak kasa giriş verisi bulunamadı!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                hm.put(ERest.result,payIn);
                Util.logger(error,PayIn.class);
            }
        }else {
            hm.put(ERest.status,false);
            hm.put(ERest.message,Util.errors(bindingResult));
        }
        return hm;
    }



}