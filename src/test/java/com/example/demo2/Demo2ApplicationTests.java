package com.example.demo2;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebAppConfiguration
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Demo2ApplicationTests {

	private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	//mockMvc 是基於 WebApplicationContext 所建立的物件，可用來編寫web應用的整合測試
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	@Test
	void contextLoads() {
	}

	@Test
	public void courseControllerTest() throws Exception {
		
		//set request_body
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("student_id", "003");
		map.put("student_name", "Joe");
				
		//map to string
		ObjectMapper objectMapper = new ObjectMapper();
		String mapString = objectMapper.writeValueAsString(map);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/api/create_student").contentType(CONTENT_TYPE).
//				headers(headers).
				content(mapString));
				
		//get response && to string
		String resultStr = result.andReturn().getResponse().getContentAsString();
				
		//response string to Json(map)
		JacksonJsonParser jsonParser = new JacksonJsonParser();
		Map<String, Object> resData = jsonParser.parseMap(resultStr);
		
		//trans type
		String rtnMessage = (String) resData.get("message");
		System.out.println(rtnMessage);
	}
	
	@BeforeAll
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
}
