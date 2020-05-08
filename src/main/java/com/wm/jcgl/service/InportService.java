package com.wm.jcgl.service;

import com.wm.jcgl.entity.Inport;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 库存信息 服务类
 * </p>
 *
 * @author WOM
 * @since 2020-05-07
 */
public interface InportService extends IService<Inport> {
    @Override
    boolean save(Inport entity);
}
