package com.wm.sys.cache;



import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Provider;
import com.wm.jcgl.mapper.BookMapper;
import com.wm.jcgl.mapper.ProviderMapper;
import com.wm.sys.common.SpringUtil;
import com.wm.sys.entity.Dept;
import com.wm.sys.entity.User;
import com.wm.sys.mapper.DeptMapper;
import com.wm.sys.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存
 * @author WOM
 *
 */
public class CachePool {
	
	/**
	 * 所有的缓存数据放到这个CACHE_CONTAINER类似于redis
	 */
	public static volatile Map<String, Object> CACHE_CONTAINER = new HashMap<>();
	
	
	/**
	 * 根据KEY删除缓存
	 * @param key
	 */
	public static void removeCacheByKey(String key) {
		if(CACHE_CONTAINER.containsKey(key)) {
			CACHE_CONTAINER.remove(key);
		}
	}
	/**
	 * 清空所有缓存
	 */
	public static void removeAll() {
		CACHE_CONTAINER.clear();
	}
	
	/**
	 * 同步缓存
	 */
	public static void syncData() {
		//同步部门数据
		DeptMapper deptMapper = SpringUtil.getBean(DeptMapper.class);
		List<Dept> deptList = deptMapper.selectList(null);
		for (Dept dept : deptList) {
			CACHE_CONTAINER.put("dept:"+dept.getId(), dept);
		}
		//同步用户数据
		UserMapper userMapper = SpringUtil.getBean(UserMapper.class);
		List<User> userList = userMapper.selectList(null);
		for (User user : userList) {
			CACHE_CONTAINER.put("user:"+user.getId(), user);
		}
		//同步供应商数据
		ProviderMapper providerMapper = SpringUtil.getBean(ProviderMapper.class);
		List<Provider> providerList = providerMapper.selectList(null);
		for (Provider provider : providerList) {
			CACHE_CONTAINER.put("provider:"+provider.getId(), provider);
		}
		//同步自编书目数据
		BookMapper goodsMapper=SpringUtil.getBean(BookMapper.class);
		List<Book> goodsList = goodsMapper.selectList(null);
		for (Book book : goodsList) {
			CACHE_CONTAINER.put("book:"+book.getId(), book);
		}
	}
	
}
