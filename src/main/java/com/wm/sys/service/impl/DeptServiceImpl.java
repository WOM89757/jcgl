package com.wm.sys.service.impl;

import com.wm.sys.entity.Dept;
import com.wm.sys.mapper.DeptMapper;
import com.wm.sys.service.DeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-04-01
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {


    @Override
    public Dept getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean updateById(Dept entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean save(Dept entity) {
        return super.save(entity);
    }
}
