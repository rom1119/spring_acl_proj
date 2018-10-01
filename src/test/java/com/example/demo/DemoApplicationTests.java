package com.example.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	@WithMockUser(roles="SUPER_ADMIN")
	public void testGetUserForSuperAdmin() throws Exception {
//		givenAuth("john", "123").get("http://localhost:8081/organizations/1");
//		assertEquals(200, response.getStatusCode());

		mvc.perform(

				get("/user/1").contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk());
//				.andExpect((ResultMatcher) content()
//						.contentTypeCompatibleWith(MediaType.TEXT_HTML));
//				.andExpect(jsonPath("$[0].name", is("bob")));
	}

	@Test
//	@WithMockUser(username = "adm", password = "test")
	public void contextLoads() throws Exception {
//		givenAuth("john", "123").get("http://localhost:8081/organizations/1");
//		assertEquals(200, response.getStatusCode());

		LinkedMultiValueMap<String, String> userData = new LinkedMultiValueMap<>();
		userData.add("password", "asdasd");
		userData.add("confirmPassword", "asdasd");
		userData.add("email", "asdasd");

		String[] authoritiesString = {"ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_USER", "WRITE_PRIVILEGE", "READ_PRIVILEGE"};
		List<GrantedAuthority> authorities = new ArrayList<>();
		Arrays.stream(authoritiesString).forEach(el ->  authorities.add(new SimpleGrantedAuthority(el)));
//		MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get("/register");
//		mockHttpServletRequestBuilder
		mvc
			.perform(
					formLogin("/processlogin").user("sadm").password("test")
			)
				.andExpect(authenticated().withAuthorities(authorities))

		;
		mvc.perform(

				post("/register").with(csrf())
						.contentType(MediaType.TEXT_HTML))
				.andExpect(status().isOk());
//				.andExpect((ResultMatcher) content()
//						.contentTypeCompatibleWith(MediaType.TEXT_HTML));
//				.andExpect(jsonPath("$[0].name", is("bob")));
	}

}
