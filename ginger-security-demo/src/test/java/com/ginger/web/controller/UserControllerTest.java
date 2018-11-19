/**
 * 
 */
package com.ginger.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Description: 用户测试用例
 * @author 姜锋
 * @date 2018年4月10日 上午8:05:49
 * @version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	@Test
	public void whenQuerySuccess() throws Exception {
		String content = mockMvc
				.perform(get("/user").param("username", "ginger").param("age", "18").param("ageTo", "23")
						.param("size", "20").param("page", "5").param("sort", "age,desc")
						.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(3)).andReturn().getResponse()
				.getContentAsString();
		System.out.println(content);
	}
	@Test
	public void whenGetInfoSuccess() throws Exception {
		String content = mockMvc.perform(get("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andExpect(jsonPath("$.username").value("ginger")).andReturn().getResponse()
				.getContentAsString();
		System.out.println(content);
	}
	@Test
	public void whenGetInfoFail() throws Exception {
		mockMvc.perform(get("/user/a").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void whenCreateSuccess() throws Exception {
		Date date = new Date();
		String content = "{\"username\":\"ginger\",\"password\":123465,\"birthday\":"+date.getTime()+"}";
		String result = mockMvc.perform(post("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andReturn().getResponse()
		.getContentAsString();
		
		System.out.println("返回结果: "+result);
	}
	
	@Test
	public void whenUpdateSuccess() throws Exception {
		
		Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		String content = "{\"id\":\"1\",\"username\":\"ginger\",\"birthday\":"+date.getTime()+"}";
		String result = mockMvc.perform(put("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(1))
		.andReturn().getResponse()
		.getContentAsString();
		
		System.out.println("返回结果: "+result);
	}
	
	@Test
	public void whenDeleteSuccess() throws Exception {
		
		String result = mockMvc.perform(delete("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andReturn().getResponse()
		.getContentAsString();
		System.out.println("返回结果: "+result);
	}
	@Test
	public void whenUploadSuccess()  throws Exception{
		String result = mockMvc.perform(fileUpload("/file")
				 .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "get hello".getBytes("UTF-8"))))
		 		.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		System.out.println("文件服务" + result);
	}
}
