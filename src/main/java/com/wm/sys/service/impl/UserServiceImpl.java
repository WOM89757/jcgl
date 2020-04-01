package com.wm.sys.service.impl;

import com.wm.sys.entity.User;
import com.wm.sys.mapper.UserMapper;
import com.wm.sys.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-04-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
