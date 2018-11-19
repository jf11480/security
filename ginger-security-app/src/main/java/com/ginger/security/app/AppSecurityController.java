/**
 * 
 */
package com.ginger.security.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.ginger.security.app.social.AppSignUpUtils;
import com.ginger.security.core.social.SocialController;
import com.ginger.security.core.social.support.SocialUserInfo;
import com.ginger.security.core.properties.SecurityConstants;

/**
 * @Description: App请求处理 
 * @author 姜锋
 * @date 2018年10月7日 下午3:59:20 
 * @version V1.0   
 *
 */
@RestController
public class AppSecurityController extends SocialController{
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	
	@Autowired
	private AppSignUpUtils appSingUpUtils;
	
	@GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		appSingUpUtils.saveConnectionData(new ServletWebRequest(request), connection.createData());
		return buildSocialUserInfo(connection);
	}
}
