package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cityid")
public class CitiesController {

    @Reference(timeout = 10000)
    private CitiesService citiesService;

    @GetMapping("/findCityIdbyProinceid")
    private List<Cities> findCityIdbyProinceid(String provinceid){
        try {
            List<Cities> list = citiesService.findCityIdbyProinceid(provinceid);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
