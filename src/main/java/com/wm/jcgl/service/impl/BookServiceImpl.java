package com.wm.jcgl.service.impl;

import com.wm.jcgl.entity.Book;
import com.wm.jcgl.mapper.BookMapper;
import com.wm.jcgl.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 在此期号内建立、修改、删除和向省店上报书目信息 服务实现类
 * </p>
 *
 * @author WOM
 * @since 2020-04-21
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Override
    public boolean save(Book entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(Book entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public Book getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }
}
