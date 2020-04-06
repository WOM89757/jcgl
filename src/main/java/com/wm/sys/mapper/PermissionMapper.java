package com.wm.sys.mapper;

import com.wm.sys.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author WOM
 * @since 2020-04-01
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * /根据权限或菜单ID删除权限表各和角色的关系表里面的数据
     * @param id
     */
    void deleteRolePermissionByPid(@Param("id") Serializable id);

}
