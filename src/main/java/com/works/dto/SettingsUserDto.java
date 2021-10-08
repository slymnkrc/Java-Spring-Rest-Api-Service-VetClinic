package com.works.dto;

import com.works.Utils.ERest;
import com.works.Utils.Util;
import com.works.entities.Role;
import com.works.entities.User;
import com.works.models.ElasticUser;
import com.works.repositories._elastic.ElasticUserRepository;
import com.works.repositories._jpa.JpaRoleRepository;
import com.works.repositories._jpa.JpaUserRepository;
import com.works.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;


@Service
public class SettingsUserDto {

    final JpaUserRepository jpaUserRepository;
    final JpaRoleRepository jpaRoleRepository;
    final UserService userService;
    final ElasticUserRepository elasticUserRepository;

    public SettingsUserDto(JpaUserRepository jpaUserRepository, JpaRoleRepository jpaRoleRepository, UserService userService, ElasticUserRepository elasticUserRepository, ElasticsearchOperations elasticsearchOperations) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaRoleRepository = jpaRoleRepository;
        this.userService = userService;
        this.elasticUserRepository = elasticUserRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    final ElasticsearchOperations elasticsearchOperations;


    // User -> Add
    public Map<ERest,Object> add(User user, BindingResult bindingResult){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        if(!bindingResult.hasErrors()){
            try {
                int roleId = user.getURoleStatus();
                Role role = jpaRoleRepository.findById(roleId).get();
                List<Role> roles = new ArrayList<>();
                roles.add(role);
                user.setRoles(roles);
                User user1 = userService.register(user);
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Kullanıcı ekleme işlemi başarılı!");
                hm.put(ERest.result,user1);
                ElasticUser elasticUser = new ElasticUser();
                elasticUser.setUId(user1.getUId());
                elasticUser.setName(user1.getUName());
                elasticUser.setSurname(user1.getUSurname());
                elasticUser.setEmail(user1.getUEmail());
                elasticUser.setPhone(user1.getUPhone());
                elasticUserRepository.save(elasticUser);
            } catch (Exception e) {
                hm.put(ERest.status,false);
                if(e.toString().contains("constraint")){
                    String error = "Bu e-mail ("+user.getUEmail()+") adresi ile daha önce kayıt yapılmış!";
                    Util.logger(error, User.class);
                    hm.put(ERest.message,error);
                }
            }
        }else {
            hm.put(ERest.status,false);
            hm.put(ERest.errors, Util.errors(bindingResult));
        }
        return hm;
    }

    // User -> Pagination
    public Map<ERest,Object> list(String stPage){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        List<User> userList = new ArrayList<>();
        try {
            int page = Integer.parseInt(stPage);
            Pageable pageable = PageRequest.of(page-1,Util.pageSize);
            Page<User> pageList = jpaUserRepository.findAllUsersWithPagination(pageable);
            hm.put(ERest.status,true);
            hm.put(ERest.message,(page) + " sayfadaki kullanıcı listeleme işlemi başarılı!");
            hm.put(ERest.result,pageList);
        } catch (Exception e) {
            String error = "Listeleme sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,User.class);
        }
        return hm;
    }

    //User -> Delete
    public Map<ERest,Object> delete(String stId){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        try {
            int id = Integer.parseInt(stId);
            Optional<User> optUser = jpaUserRepository.findById(id);
            if(optUser.isPresent()){
                System.out.println("id " + id + " " + jpaUserRepository.findById(id).get());
                ElasticUser elasticUser  = elasticUserRepository.findById(id).get();
                jpaUserRepository.deleteById(id);
                elasticUserRepository.deleteById(elasticUser.getId());
                hm.put(ERest.status,true);
                hm.put(ERest.message,"Silme işlemi başarılı!");
                hm.put(ERest.result,optUser.get());
            }else {
                String error = "Silmek istenen kullanıcı bulunamadı!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                Util.logger(error,User.class);
            }
        } catch (Exception e) {
            String error = "Silme işlemi sırasında bir hata oluştu!" + e;
            hm.put(ERest.status,false);
            hm.put(ERest.message,error);
            Util.logger(error,User.class);
        }
        return hm;
    }

    //User -> Update
    public Map<ERest,Object> update(User user,BindingResult bindingResult){
        Map<ERest,Object> hm = new LinkedHashMap<>();
        if(user.getUId() != null && !bindingResult.hasErrors()){
            Optional<User> optUser = jpaUserRepository.findById(user.getUId());
            if(optUser.isPresent()){
                try {
                    int roleId = user.getURoleStatus();
                    Role role = jpaRoleRepository.findById(roleId).get();
                    List<Role> roles = new ArrayList<>();
                    roles.add(role);
                    user.setRoles(roles);
                    user.setUPassword(optUser.get().getUPassword());
                    ElasticUser eu = elasticUserRepository.findById(user.getUId()).get();
                    elasticUserRepository.deleteById(eu.getId());
                    User us = jpaUserRepository.saveAndFlush(user);
                    ElasticUser elasticUser = new ElasticUser();
                    elasticUser.setUId(us.getUId());
                    elasticUser.setName(us.getUName());
                    elasticUser.setSurname(us.getUSurname());
                    elasticUser.setEmail(us.getUEmail());
                    elasticUser.setPhone(us.getUPhone());
                    elasticUserRepository.save(elasticUser);
                    hm.put(ERest.status,true);
                    hm.put(ERest.message,"Güncelleme işlemi başarılı!");
                    hm.put(ERest.result,us);
                } catch (Exception e) {
                    String error = "Güncelleme işlemi sırasında bir hata oluştu! " + e + " ";
                    hm.put(ERest.status,false);
                    if(e.toString().contains("constraint")){
                        error += "Bu e-mail ("+user.getUEmail()+") adresi ile daha önce kayıt yapılmış";
                        hm.put(ERest.message,error);
                    }
                    hm.put(ERest.result,user);
                    Util.logger(error,User.class);
                }
            }else{
                String error = "Güncelleme işlemi yapılacak kullanıcı bulunamadı!";
                hm.put(ERest.status,false);
                hm.put(ERest.message,error);
                hm.put(ERest.result,user);
                Util.logger(error,User.class);
            }
        }else {
            hm.put(ERest.status,false);
            hm.put(ERest.message,Util.errors(bindingResult));
        }
        return hm;
    }


    // User -> Search
//    public Map<ERest,Object> elasticSearch(String data){
//        Map<ERest,Object> hm = new LinkedHashMap<>();
//        final NativeSearchQuery query = new NativeSearchQueryBuilder()
//                //Birden fazla aram kriteri eklemek için multiMatchQuery yapısı kullanılır.
//                .withQuery(multiMatchQuery(data,"name")
//                        .field("surname")
//                        .field("email")
//                        .field("phone")
//                        .fuzziness(Fuzziness.AUTO))
//                .build();
//        List<SearchHit<ElasticUser>> list = operations.search(query,ElasticUser.class).getSearchHits();
//        if(list.size() > 0 ){
//            hm.put(ERest.status,true);
//            hm.put(ERest.message,"Arama işlemi başarılı!");
//            hm.put(ERest.result,list);
//        }else{
//            hm.put(ERest.status,false);
//            hm.put(ERest.message,"Arama kriterlerinize uygun sonuç bulunamadı!");
//        }
//        return hm;
//    }
//
//    public Map<ERest,Object> elasticInsertData(){
//        Map<ERest,Object> hm = new LinkedHashMap<>();
//        List<User> userList = uRepo.findAll();
//        try {
//            if(userList.size() > 0){
//                userList.forEach(item -> {
//                    //ElasticSearch Save
//                    ElasticUser eu = new ElasticUser();
//                    eu.setUId(item.getUId());
//                    eu.setName(item.getUName());
//                    eu.setSurname(item.getUSurname());
//                    eu.setEmail(item.getUEmail());
//                    eu.setPhone(item.getUPhone());
//                    euRepo.save(eu);
//                });
//                hm.put(ERest.status,true);
//                hm.put(ERest.message,"Elasticsearch veri ekleme işlemi başarılı!");
//                hm.put(ERest.result,euRepo.findAll());
//            }else {
//                String error = "Sisteme kayıtlı kullanıcı bulunmamaktadır!";
//                hm.put(ERest.status,false);
//                hm.put(ERest.message,error);
//                Util.logger(error,User.class);
//            }
//        } catch (Exception e) {
//            String error = "Elasticsearch veri tabanına ekleme yapılırken bir hata oluştu!" + e;
//            hm.put(ERest.status,false);
//            hm.put(ERest.message,error);
//            Util.logger(error,User.class);
//        }
//        return hm;
//    }
//
//    //------------------------------------- ElasticSearch - End ---------------------------------------//

}