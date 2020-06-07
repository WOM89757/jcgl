package com.wm.sys.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.wm.sys.common.ActiverUser;
import com.wm.sys.common.ResultObj;
import com.wm.sys.common.WebUtils;
import com.wm.sys.entity.Loginfo;
import com.wm.sys.service.LoginfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 登录控制器
 * @author WOM
 */
@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private LoginfoService loginfoService;

	@RequestMapping("login")
	public ResultObj login(String loginname,String pwd) {
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken token=new UsernamePasswordToken(loginname, pwd);
		try {
			subject.login(token);
			ActiverUser activerUser=(ActiverUser) subject.getPrincipal();
			WebUtils.getSession().setAttribute("user", activerUser.getUser());
			//记录登陆日志
			Loginfo entity=new Loginfo();
			entity.setLoginname(activerUser.getUser().getName()+"-"+activerUser.getUser().getLoginname());
			entity.setLoginip(WebUtils.getRequest().getRemoteAddr());
			entity.setLogintime(new Date());
			loginfoService.save(entity);
			return ResultObj.LOGIN_SUCCESS;
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return ResultObj.LOGIN_ERROR_PASS;
		}
	}

	@RequestMapping("getCode")
	public void getCode(HttpServletResponse resp, HttpSession session) throws IOException {
		CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(116, 36, 4, 5);
		//得到code
		String code = captcha.getCode();
//		System.out.println(code);
		//放到session
		session.setAttribute("code", code);
		ServletOutputStream outputStream = resp.getOutputStream();
		captcha.write(outputStream);
		outputStream.close();
	}



}

