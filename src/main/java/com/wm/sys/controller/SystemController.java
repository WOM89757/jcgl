package com.wm.sys.controller;

import com.wm.sys.common.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sys")
public class SystemController {


	/**
	 * 跳转到修改个人密码
	 */
	@RequestMapping("toChangePwd")
	public String toChangePwd() {
		return "system/user/changePwd";
	}

	/**
	 * 注销当前用户，返回登录界面
	 * @return
	 */
	@RequestMapping("loginout")
	public String loginout() {

		WebUtils.getSession().invalidate();
		return "system/index/login";

	}


	/**
	 * 跳转到登陆页面
	 */
	@RequestMapping("toLogin")
	public String toLogin() {
		return "system/index/login";
	}
	
	/**
	 * 跳转到首页
	 */
	@RequestMapping("index")
	public String index() {
		return "system/index/index";
	}
	
	/**
	 * 跳转到工作台
	 */
	@RequestMapping("toDeskManager")
	public String toDeskManager() {
		return "system/index/deskManager";
	}

	/**
	 * 跳转到个人信息
	 */
	@RequestMapping("toUserInfo")
	public String toUserInfo() {
		return "system/user/userInfo";
	}

	/**
	 * 跳转到日志管理
	 * 
	 */
	@RequestMapping("toLoginfoManager")
	public String toLoginfoManager() {
		return "system/loginfo/loginfoManager";
	}
	
	/**
	 * 跳转到公告管理
	 * 
	 */
	@RequestMapping("toNoticeManager")
	public String toNoticeManager() {
		return "system/notice/noticeManager";
	}
	
	
	/**
	 * 跳转到部门管理
	 * 
	 */
	@RequestMapping("toDeptManager")
	public String toDeptManager() {
		return "system/dept/deptManager";
	}
	
	/**
	 * 跳转到部门管理-left
	 * 
	 */
	@RequestMapping("toDeptLeft")
	public String toDeptLeft() {
		return "system/dept/deptLeft";
	}
	
	
	/**
	 * 跳转到部门管理--right
	 * 
	 */
	@RequestMapping("toDeptRight")
	public String toDeptRight() {
		return "system/dept/deptRight";
	}
	
	/**
	 * 跳转到菜单管理
	 * 
	 */
	@RequestMapping("toMenuManager")
	public String toMenuManager() {
		return "system/menu/menuManager";
	}
	
	/**
	 * 跳转到菜单管理-left
	 * 
	 */
	@RequestMapping("toMenuLeft")
	public String toMenuLeft() {
		return "system/menu/menuLeft";
	}
	
	
	/**
	 * 跳转到菜单管理--right
	 * 
	 */
	@RequestMapping("toMenuRight")
	public String toMenuRight() {
		return "system/menu/menuRight";
	}
	
	/**
	 * 跳转到权限管理
	 * 
	 */
	@RequestMapping("toPermissionManager")
	public String toPermissionManager() {
		return "system/permission/permissionManager";
	}
	
	/**
	 * 跳转到权限管理-left
	 * 
	 */
	@RequestMapping("toPermissionLeft")
	public String toPermissionLeft() {
		return "system/permission/permissionLeft";
	}
	
	
	/**
	 * 跳转到权限管理--right
	 * 
	 */
	@RequestMapping("toPermissionRight")
	public String toPermissionRight() {
		return "system/permission/permissionRight";
	}
	
	/**
	 * 跳转到角色管理
	 * 
	 */
	@RequestMapping("toRoleManager")
	public String toRoleManager() {
		return "system/role/roleManager";
	}
	

	/**
	 * 跳转到用户管理
	 * 
	 */
	@RequestMapping("toUserManager")
	public String toUserManager() {
		return "system/user/userManager";
	}
	/**
	 * 跳转到缓存管理
	 * 
	 */
	@RequestMapping("toCacheManager")
	public String toCacheManager() {
		return "system/cache/cacheManager";
	}
	
	
	
}
