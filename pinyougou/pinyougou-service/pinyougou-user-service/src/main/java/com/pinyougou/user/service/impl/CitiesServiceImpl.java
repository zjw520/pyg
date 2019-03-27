package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.CitiesMapper;
import com.pinyougou.pojo.Cities;
import com.pinyougou.service.CitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName ="com.pinyougou.service.CitiesService" )
@Transactional
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesMapper citiesMapper;

    @Override
    public void save(Cities cities) {

    }

    @Override
    public void update(Cities cities) {

    }

    @Override
    public void delete(Serializable serializable) {

    }

    @Override
    public void deleteAll(Serializable[] serializables) {

    }

    @Override
    public Cities findOne(Serializable serializable) {

        return null;
    }

    @Override
    public List<Cities> findAll() {
        return null;
    }

    @Override
    public List<Cities> findByPage(Cities cities, int i, int i1) {
        return null;
    }

    @Override
    public List<Cities> findCityIdbyProinceid(String provinceid) {

        try {
            Cities cities = new Cities();
            cities.setProvinceId(provinceid);
            List<Cities> list = citiesMapper.select(cities);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
