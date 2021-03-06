/**
 * 
 */
package com.ginger.security.browser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.ginger.security.core.properties.SecurityConstants;
import com.ginger.security.core.properties.SecurityProperties;
import com.ginger.security.core.social.support.SocialUserInfo;
import com.ginger.security.core.support.SimpleResponse;

/**
 * @Description: 处理多终端转发
 * @author 姜锋
 * @date 2018年4月12日 下午12:01:22 
 * @version V1.0   
 *
 */
@RestController
public class BrowserSecurityController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//主要用于获取请求信息
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	/**
	 * @throws IOException 
	 * 
	 * @Description: 当需要身份认证时跳转到此处
	 * @author 姜锋
	 * @date 2018年4月12日 下午12:04:46
	 * @param @param request
	 * @param @param response
	 * @param @return
	 * @return String
	 * @throws
	 */
	@RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		
		if (savedRequest != null) {
			String targetUrl = savedRequest.getRedirectUrl();
			logger.info("引发跳转的请求是:" + targetUrl);
			if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getSignInPage());
			}
		}
		return new SimpleResponse("访问的服务需要身份认证,请引导用户到登录页");
	}
	
	/**
	 * 
	 * @Description: 获取社交用户信息
	 * @author 姜锋
	 * @date 2018年10月3日 下午6:55:03
	 * @param @param request
	 * @param @return
	 * @return SocialUserInfo
	 * @throws
	 */
	@GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
	public SocialUserInfo getSocialuserinfo(HttpServletRequest request) {

		SocialUserInfo userInfo = new SocialUserInfo();
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		
		userInfo.setProviderId(connection.getKey().getProviderId());
		userInfo.setProviderUserId(connection.getKey().getProviderUserId());
		userInfo.setNickname(connection.getDisplayName());
		userInfo.setHeadimg(connection.getImageUrl());
		
		return userInfo;
	}
	
}
