package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.ProvincesMapper;
import com.pinyougou.pojo.Provinces;
import com.pinyougou.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName = "com.pinyougou.service.ProvincesService")
@Transactional
public class ProvincesServiceImpl implements ProvincesService {

    @Autowired
    private ProvincesMapper provincesMapper;

    @Override
    public void save(Provinces provinces) {

    }

    @Override
    public void update(Provinces provinces) {

    }

    @Override
    public void delete(Serializable serializable) {

    }

    @Override
    public void deleteAll(Serializable[] serializables) {

    }

    @Override
    public Provinces findOne(Serializable serializable) {
        return null;
    }

    @Override
    public List<Provinces> findAll() {
        return provincesMapper.selectAll();
    }

    @Override
    public List<Provinces> findByPage(Provinces provinces, int i, int i1) {
        return null;
    }
}
