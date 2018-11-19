/**
 * 
 */
package com.ginger.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.ginger.security.browser.logout.GingerLogoutSuccessHandler;
import com.ginger.security.browser.session.GingerExpiredSessionStrategy;
import com.ginger.security.browser.session.GingerInvalidSessionStrategy;
import com.ginger.security.core.properties.SecurityProperties;

/**
 * @Description: 浏览器环境下扩展点配置，配置在这里的bean，业务系统都可以通过声明同类型或同名的bean来覆盖安全
 * 模块默认的配置。
 * @author 姜锋
 * @date 2018年10月4日 下午6:40:17 
 * @version V1.0   
 *
 */
@Configuration
public class BrowserSecurityBeanConfig {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	/**
	 * session失效时的处理策略配置
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(InvalidSessionStrategy.class)
	public InvalidSessionStrategy invalidSessionStrategy(){
		return new GingerInvalidSessionStrategy(securityProperties);
	}
	
	/**
	 * 并发登录导致前一个session失效时的处理策略配置
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
		return new GingerExpiredSessionStrategy(securityProperties);
	}
	
	
	/**
	 * 退出时的处理策略配置
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(LogoutSuccessHandler.class)
	public LogoutSuccessHandler logoutSuccessHandler(){
		return new GingerLogoutSuccessHandler(securityProperties.getBrowser().getSignOutUrl());
	}
	
}
