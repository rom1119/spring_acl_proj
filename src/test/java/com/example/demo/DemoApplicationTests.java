package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
//@ContextConfiguration(classes = {DemoApplication.class})
//@WebAppConfiguration
public class DemoApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Before
	public void setup() throws Exception {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@WithMockUser(roles="ADMIN")
	public void contextLoads() throws Exception {
//		givenAuth("john", "123").get("http://localhost:8081/organizations/1");
//		assertEquals(200, response.getStatusCode());
		System.out.println("dziala !!!!!!!!!!!!!!!!");

		post("/processlogin").param("username","adm").param("password", "test");
//		mvc.perform(
//
//				get("/user").contentType(MediaType.TEXT_HTML))
//				.andExpect(status().isOk());
//				.andExpect((ResultMatcher) content()
//						.contentTypeCompatibleWith(MediaType.TEXT_HTML));
//				.andExpect(jsonPath("$[0].name", is("bob")));
	}

}
