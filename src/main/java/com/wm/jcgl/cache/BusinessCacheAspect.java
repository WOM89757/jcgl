package com.wm.jcgl.cache;


import com.wm.jcgl.entity.Book;
import com.wm.jcgl.entity.Provider;
import com.wm.sys.cache.CachePool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

@Aspect
@Component
@EnableAspectJAutoProxy
public class BusinessCacheAspect {

	/**
	 * 日志出处
	 */
	private Log log = LogFactory.getLog(BusinessCacheAspect.class);

	// 声明一个缓存容器
	private static Map<String, Object> CACHE_CONTAINER = CachePool.CACHE_CONTAINER;

	



	// 声明切面表达式
	private static final String POINTCUT_PROVIDER_ADD = "execution(* com.wm.jcgl.service.impl.ProviderServiceImpl.save(..))";
	private static final String POINTCUT_PROVIDER_UPDATE = "execution(* com.wm.jcgl.service.impl.ProviderServiceImpl.updateById(..))";
	private static final String POINTCUT_PROVIDER_GET = "execution(* com.wm.jcgl.service.impl.ProviderServiceImpl.getById(..))";
	private static final String POINTCUT_PROVIDER_DELETE = "execution(* com.wm.jcgl.service.impl.ProviderServiceImpl.removeById(..))";
	private static final String POINTCUT_PROVIDER_BATCHDELETE = "execution(* com.wm.jcgl.service.impl.ProviderServiceImpl.removeByIds(..))";

	private static final String CACHE_PROVIDER_PROFIX = "provider:";

	/**
	 * 供应商添加切入
	 * 
	 * @throws Throwable
	 */
	@Around(value = POINTCUT_PROVIDER_ADD)
	public Object cacheProviderAdd(ProceedingJoinPoint joinPoint) throws Throwable {
		// 取出第一个参数
		Provider object = (Provider) joinPoint.getArgs()[0];
		Boolean res = (Boolean) joinPoint.proceed();
		if (res) {
			CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX + object.getId(), object);
		}
		return res;
	}

	/**
	 * 查询切入
	 * 
	 * @throws Throwable
	 */
	@Around(value = POINTCUT_PROVIDER_GET)
	public Object cacheProviderGet(ProceedingJoinPoint joinPoint) throws Throwable {
		// 取出第一个参数
		Integer object = (Integer) joinPoint.getArgs()[0];
		// 从缓存里面取
		Object res1 = CACHE_CONTAINER.get(CACHE_PROVIDER_PROFIX + object);
		if (res1 != null) {
			log.info("已从缓存里面找到供应商对象" + CACHE_PROVIDER_PROFIX + object);
			return res1;
		} else {
			Provider res2 = (Provider) joinPoint.proceed();
			CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX + res2.getId(), res2);
			log.info("未从缓存里面找到供应商对象，去数据库查询并放到缓存" + CACHE_PROVIDER_PROFIX + res2.getId());
			return res2;
		}
	}

	/**
	 * 更新切入
	 * 
	 * @throws Throwable
	 */
	@Around(value = POINTCUT_PROVIDER_UPDATE)
	public Object cacheProviderUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
		// 取出第一个参数
		Provider providerVo = (Provider) joinPoint.getArgs()[0];
		Boolean isSuccess = (Boolean) joinPoint.proceed();
		if (isSuccess) {
			Provider provider = (Provider) CACHE_CONTAINER.get(CACHE_PROVIDER_PROFIX + providerVo.getId());
			if (null == provider) {
				provider = new Provider();
			}
			BeanUtils.copyProperties(providerVo, provider);
			log.info("供应商对象缓存已更新" + CACHE_PROVIDER_PROFIX + providerVo.getId());
			CACHE_CONTAINER.put(CACHE_PROVIDER_PROFIX + provider.getId(), provider);
		}
		return isSuccess;
	}

	/**
	 * 删除切入
	 * 
	 * @throws Throwable
	 */
	@Around(value = POINTCUT_PROVIDER_DELETE)
	public Object cacheProviderDelete(ProceedingJoinPoint joinPoint) throws Throwable {
		// 取出第一个参数
		Integer id = (Integer) joinPoint.getArgs()[0];
		Boolean isSuccess = (Boolean) joinPoint.proceed();
		if (isSuccess) {
			// 删除缓存
			CACHE_CONTAINER.remove(CACHE_PROVIDER_PROFIX + id);
			log.info("供应商对象缓存已删除" + CACHE_PROVIDER_PROFIX + id);
		}
		return isSuccess;
	}

	/**
	 * 批量删除切入
	 * 
	 * @throws Throwable
	 */
	@Around(value = POINTCUT_PROVIDER_BATCHDELETE)
	public Object cacheProviderBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
		// 取出第一个参数
		@SuppressWarnings("unchecked")
		Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
		Boolean isSuccess = (Boolean) joinPoint.proceed();
		if (isSuccess) {
			for (Serializable id : idList) {
				// 删除缓存
				CACHE_CONTAINER.remove(CACHE_PROVIDER_PROFIX + id);
				log.info("供应商对象缓存已删除" + CACHE_PROVIDER_PROFIX + id);
			}
		}
		return isSuccess;
	}
	
	
	//自编书目数据的缓存 声明切面表达式
		private static final String POINTCUT_BOOKS_ADD = "execution(* com.wm.jcgl.service.impl.BookServiceImpl.save(..))";
		private static final String POINTCUT_BOOKS_UPDATE = "execution(* com.wm.jcgl.service.impl.BookServiceImpl.updateById(..))";
		private static final String POINTCUT_BOOKS_GET = "execution(* com.wm.jcgl.service.impl.BookServiceImpl.getById(..))";
		private static final String POINTCUT_BOOKS_DELETE = "execution(* com.wm.jcgl.service.impl.BookServiceImpl.removeById(..))";
		private static final String POINTCUT_BOOKS_BATCHDELETE = "execution(* com.wm.jcgl.service.impl.BookServiceImpl.removeByIds(..))";

		private static final String CACHE_BOOKS_PROFIX = "books:";

		/**
		 * 自编书目添加切入
		 * 
		 * @throws Throwable
		 */
		@Around(value = POINTCUT_BOOKS_ADD)
		public Object cacheBookAdd(ProceedingJoinPoint joinPoint) throws Throwable {
			// 取出第一个参数
			Book object = (Book) joinPoint.getArgs()[0];
			Boolean res = (Boolean) joinPoint.proceed();
			if (res) {
				CACHE_CONTAINER.put(CACHE_BOOKS_PROFIX + object.getId(), object);
			}
			return res;
		}

		/**
		 * 查询切入
		 * 
		 * @throws Throwable
		 */
		@Around(value = POINTCUT_BOOKS_GET)
		public Object cacheBookGet(ProceedingJoinPoint joinPoint) throws Throwable {
			// 取出第一个参数
			Integer object = (Integer) joinPoint.getArgs()[0];
			// 从缓存里面取
			Object res1 = CACHE_CONTAINER.get(CACHE_BOOKS_PROFIX + object);
			if (res1 != null) {
				log.info("已从缓存里面找到自编书目对象" + CACHE_BOOKS_PROFIX + object);
				return res1;
			} else {
				Book res2 = (Book) joinPoint.proceed();
				CACHE_CONTAINER.put(CACHE_BOOKS_PROFIX + res2.getId(), res2);
				log.info("未从缓存里面找到自编书目对象，去数据库查询并放到缓存" + CACHE_BOOKS_PROFIX + res2.getId());
				return res2;
			}
		}

		/**
		 * 更新切入
		 * 
		 * @throws Throwable
		 */
		@Around(value = POINTCUT_BOOKS_UPDATE)
		public Object cacheBookUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
			// 取出第一个参数
			Book bookVo = (Book) joinPoint.getArgs()[0];
			Boolean isSuccess = (Boolean) joinPoint.proceed();
			if (isSuccess) {
				Book book = (Book) CACHE_CONTAINER.get(CACHE_BOOKS_PROFIX + bookVo.getId());
				if (null == book) {
					book = new Book();
				}
				BeanUtils.copyProperties(bookVo, book);
				log.info("自编书目对象缓存已更新" + CACHE_BOOKS_PROFIX + bookVo.getId());
				CACHE_CONTAINER.put(CACHE_BOOKS_PROFIX + book.getId(), book);
			}
			return isSuccess;
		}

		/**
		 * 删除切入
		 * 
		 * @throws Throwable
		 */
		@Around(value = POINTCUT_BOOKS_DELETE)
		public Object cacheBookDelete(ProceedingJoinPoint joinPoint) throws Throwable {
			// 取出第一个参数
			Integer id = (Integer) joinPoint.getArgs()[0];
			Boolean isSuccess = (Boolean) joinPoint.proceed();
			if (isSuccess) {
				// 删除缓存
				CACHE_CONTAINER.remove(CACHE_BOOKS_PROFIX + id);
				log.info("自编书目对象缓存已删除" + CACHE_BOOKS_PROFIX + id);
			}
			return isSuccess;
		}
		/**
		 * 批量删除切入
		 *
		 * @throws Throwable
		 */
		@Around(value = POINTCUT_BOOKS_BATCHDELETE)
		public Object cacheBooksBatchDelete(ProceedingJoinPoint joinPoint) throws Throwable {
			// 取出第一个参数
			@SuppressWarnings("unchecked")
			Collection<Serializable> idList = (Collection<Serializable>) joinPoint.getArgs()[0];
			Boolean isSuccess = (Boolean) joinPoint.proceed();
			if (isSuccess) {
				for (Serializable id : idList) {
					// 删除缓存
					CACHE_CONTAINER.remove(CACHE_BOOKS_PROFIX + id);
					log.info("供应商对象缓存已删除" + CACHE_BOOKS_PROFIX + id);
				}
			}
			return isSuccess;
		}
}
