package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.ProvincesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/province")
public class ProvinceController {

    @Reference(timeout = 10000)
    private ProvincesService provincesService;

    @GetMapping("/findProvinces")
    public List<Provinces> findProvinceId(){
        try {
            return provincesService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
