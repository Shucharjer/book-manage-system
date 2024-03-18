package com.example.controller;

import com.example.pojo.RestBean;
import com.example.pojo.dto.ReaderType;
import com.example.service.ReaderTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("readerType")
@CrossOrigin
public class ReaderTypeController {

    @Autowired
    private ReaderTypeService readerTypeService;

    @PostMapping("/addReaderType")
    public RestBean<Void> addReaderType(@RequestBody ReaderType readerType){
        readerTypeService.addReaderType(readerType);
        return RestBean.success();
    }

    @GetMapping("/findReaderType")
    public RestBean<ReaderType> findReaderType(String typeName){
        ReaderType readerType = readerTypeService.findReaderType(typeName);
        return RestBean.success(readerType);
    }

    @DeleteMapping("/deleteByName")
    public RestBean<Void> deleteByName(String tyName){
        readerTypeService.deleteByName(tyName);
        return RestBean.success();
    }

    @PutMapping("/update")
    public RestBean<Void> updateByTypeId(@RequestBody ReaderType readerType){
        readerTypeService.updateByTypeId(readerType);
        return RestBean.success();
    }
}
