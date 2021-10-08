package com.works.restcontrollers;

import com.works.Utils.ERest;
import com.works.dto.StorageDto;
import com.works.entities.Storage;
import com.works.repositories._jpa.JpaStorageRepository;
import com.works.services.UtilService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/storage")
public class StorageRestController {

    final JpaStorageRepository jpaStorageRepository;
    final UtilService utilService;
    final StorageDto storageDto;

    public StorageRestController(JpaStorageRepository jpaStorageRepository, UtilService utilService, StorageDto storageDto) {
        this.jpaStorageRepository = jpaStorageRepository;
        this.utilService = utilService;
        this.storageDto = storageDto;
    }

    // Storage -> Add
    @PostMapping("/add")
    public Map<ERest,Object> add(@RequestBody @Valid Storage storage, BindingResult bindingResult){
        return storageDto.add(storage,bindingResult);
    }

    // Storage -> List
    @GetMapping("/list")
    public Map<ERest,Object> list() {
        return storageDto.list();
    }

    // Storage -> Delete
    @DeleteMapping("/delete/{storId}")
    public Map<ERest,Object> delete(@PathVariable Integer storId) {
        return storageDto.delete(storId);
    }

    // Storage -> Update
    @PutMapping("/update")
    public  Map<ERest,Object> update(@RequestBody Storage storage) {
        return storageDto.update(storage);
    }

    // Storage -> Pagination
    @GetMapping("/list/{pageNumber}")
    public Map<ERest,Object> pageList(@PathVariable Integer pageNumber) {
        return storageDto.pageList(pageNumber);
    }

    // Storage -> ElasticSearch
    @GetMapping("/search/{pageNo}/{data}")
    public Map<String, Object> search(@PathVariable String data, @PathVariable int pageNo){
        return storageDto.search(data,pageNo);
    }
}


