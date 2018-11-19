/**
 * 
 */
package com.ginger.security.core.validate.code;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.ginger.security.core.properties.SecurityConstants;

/**
 * @Description: 生成校验码的请求处理器
 * @author 姜锋
 * @date 2018年4月12日 下午11:13:54 
 * @version V1.0   
 *
 */
@RestController
public class ValidateCodeController {
	
	@Autowired
	private ValidateCodeProcessorHolder validateCodeProcessorHolder;

	
	/**
	 * 创建验证码，根据验证码类型不同，调用不同的 {@link ValidateCodeProcessor}接口实现
	 * @param request
	 * @param response
	 * @param type
	 * @throws Exception 
	 */
	@GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
	public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
		validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
	}


}