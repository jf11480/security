/**
 * 
 */
package com.ginger.security.core.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * @Description:  默认的SocialUserDetailsService实现
 * 不做任何处理，只在控制台打印一句日志，然后抛出异常，提醒业务系统自己配置SocialUserDetailsService。
 * @author 姜锋
 * @date 2018年10月14日 下午4:00:56 
 * @version V1.0   
 *
 */
public class DefaultSocialUserDetailsService implements SocialUserDetailsService {
		
private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		logger.warn("请配置 SocialUserDetailsService 接口的实现.");
		throw new UsernameNotFoundException(userId);
	}
}
