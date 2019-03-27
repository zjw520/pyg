package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.AreasMapper;
import com.pinyougou.pojo.Areas;
import com.pinyougou.service.AreasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service(interfaceName ="com.pinyougou.service.AreasService" )
@Transactional
public class areaServiceImpl implements AreasService {

    @Autowired
    private AreasMapper areasMapper;
    /**
     * 添加方法
     *
     * @param areas
     */
    @Override
    public void save(Areas areas) {

    }

    /**
     * 修改方法
     *
     * @param areas
     */
    @Override
    public void update(Areas areas) {

    }

    /**
     * 根据主键id删除
     *
     * @param id
     */
    @Override
    public void delete(Serializable id) {

    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    public void deleteAll(Serializable[] ids) {

    }

    /**
     * 根据主键id查询
     *
     * @param id
     */
    @Override
    public Areas findOne(Serializable id) {
        return null;
    }

    /**
     * 查询全部
     */
    @Override
    public List<Areas> findAll() {
        return null;
    }

    /**
     * 多条件分页查询
     *
     * @param areas
     * @param page
     * @param rows
     */
    @Override
    public List<Areas> findByPage(Areas areas, int page, int rows) {
        return null;
    }

    @Override
    public List<Areas> findAreaidByCityid(String cityid) {
        Areas areas = new Areas();
        areas.setCityId(cityid);
        List<Areas> List = areasMapper.select(areas);
        return List;
    }
}
