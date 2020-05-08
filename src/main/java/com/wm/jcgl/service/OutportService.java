package com.wm.jcgl.service;

import com.wm.jcgl.entity.Outport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 库存退货信息 服务类
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
public interface OutportService extends IService<Outport> {

    /**
     * 添加退货信息
     * @param id
     * @param number
     * @param remark
     */
    void addOutPort(Integer id, Integer number, String remark);
}
