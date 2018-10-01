package com.example.demo;

import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.model.*;
import com.example.demo.user.repository.PrivilegeRepository;
import com.example.demo.user.repository.RoleRepository;
import com.example.demo.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS,
        value=MyMockUserTestExecutionListener.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    WebApplicationContext wac; // cached

    @Autowired
    MockServletContext servletContext; // cached

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpServletResponse response;

    @Autowired
    ServletWebRequest webRequest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAclService customAclService;

    @Autowired
    private AclSecurityIDRepository aclSecurityIDRepository;

    Role userRole = new Role("ROLE_USER");
    Role adminRole = new Role("ROLE_ADMIN");
    Role superAdminRole = new Role("ROLE_SUPER_ADMIN");

    Set<Role> userRoles = new HashSet<>();
    Set<Role> adminRoles = new HashSet<>();
    Set<Role> superAdminRoles = new HashSet<>();

    Privilege read = new Privilege("READ_PRIVILEGE");
    Privilege write = new Privilege("WRITE_PRIVILEGE");

    User user = new User("user", "test", true, new UserDetails("UserFirstName", "UserLastName"), userRoles);
    User userOff = new User("userDisable", "test", false, new UserDetails("UserDisableFirstName", "UserDisableLastName"), userRoles);
    User admin = new User("admin", "test", true, new UserDetails("AdminDisableFirstName", "AdminDisableLastName"), adminRoles);
    User adminOff = new User("adminDisable", "test", false, new UserDetails("UserDisableFirstName", "UserDisableLastName"), adminRoles);
    User superAdmin = new User("superAdmin", "test", true, new UserDetails("SuperAdminDisableFirstName", "SuperAdminDisableLastName"), superAdminRoles);
    User superAdminOff = new User("superAdminDisable", "test", false, new UserDetails("SuperAdminDisableFirstName", "SuperAdminDisableLastName"), superAdminRoles);
    User notSavedUser = new User("notSavedUser", "test", false, new UserDetails("NotSavedUserFirstName", "notSavedUserLastName"), userRoles);

    public static org.springframework.security.core.userdetails.UserDetails loggedUser;
    public static org.springframework.security.core.userdetails.UserDetails loggedUserOff;
    public static org.springframework.security.core.userdetails.UserDetails loggedAdmin;
    public static org.springframework.security.core.userdetails.UserDetails loggedAdminOff;
    public static org.springframework.security.core.userdetails.UserDetails loggedSuperAdmin;
    public static org.springframework.security.core.userdetails.UserDetails loggedSuperAdminOff;

    Map<String, User> users = new HashMap<>();
    private MockMvc mockMvc;

    {
        users.put("user", user);
        users.put("userOff", userOff);
        users.put("admin", admin);
        users.put("adminOff", adminOff);
        users.put("superAdmin", superAdmin);
        users.put("superAdminOff", superAdminOff);
        users.put("notSavedUser", notSavedUser);

    }

    @Before
    public void setup() throws Exception {
        notSavedUser.setId((long) 999999999);

        for (Map.Entry<String, User> el: users.entrySet() ) {

            User user = userRepository.findByEmail(((User)el.getValue()).getEmail());
            if (user == null) {
               continue;
            }
            User olduser =  users.get(el.getKey());
            olduser.setId(user.getId());
//            olduser.setPassword(user.getPassword());
            users.replace(el.getKey(), olduser);
        }

        initLoggedUsers();


//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private void initLoggedUsers()
    {
        loggedUser = createFromUser(users.get("user"));
        loggedUserOff = createFromUser(users.get("userOff"));
        loggedAdmin = createFromUser(users.get("admin"));
        loggedAdminOff = createFromUser(users.get("adminOff"));
        loggedSuperAdmin = createFromUser(users.get("superAdmin"));
        loggedSuperAdminOff = createFromUser(users.get("superAdminOff"));
    }


    private org.springframework.security.core.userdetails.UserDetails createFromUser(User user)
    {
        User byEmail = userRepository.findByEmail(user.getEmail());
        return new CustomUserDetails(byEmail);
    }



    @Test
    @WithUserDetails(value = "superAdmin", userDetailsServiceBeanName = "myUserDetailsService")
    public void getOneForSuperAdmin() throws Exception {


        mvc.perform(
            get("/user/" + users.get("user").getId())
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isOk()
        );
        mvc.perform(
            get("/user/" + users.get("superAdmin").getId())
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isOk()
        );
        mvc.perform(
            get("/user/" + users.get("notSavedUser").getId())
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isNotFound()
        );

//
    }


    @Test
    @WithUserDetails(value = "superAdmin", userDetailsServiceBeanName = "myUserDetailsService")
    public void getAllForSuperAdmin() throws Exception {

        mvc.perform(
            get("/user")
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isOk()
        );
//
    }


    @Test
    @WithUserDetails(value = "superAdmin", userDetailsServiceBeanName = "myUserDetailsService")
    public void editViewForSuperAdmin() throws Exception {


        mvc.perform(
            get("/user/" + users.get("user").getId() + "/edit")
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isForbidden()
        );

//        Authentication a = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails principal = (CustomUserDetails)a.getPrincipal();

        AclSecurityID superAdminSid = aclSecurityIDRepository.findBySid("superAdmin");

        LinkedMultiValueMap<String, String> aclEntry = new LinkedMultiValueMap<>();
        aclEntry.add("granting", "true");
        aclEntry.add("securityID", String.valueOf(superAdminSid.getId()));
        aclEntry.add("mask", String.valueOf(BasePermission.WRITE.getMask()));

        mvc.perform(
                post("/user/" + users.get("user").getId() + "/acl_entry/new")
                        .with(user(loggedUser)).params(aclEntry).with(csrf())
                .contentType(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection()
                );

        mvc.perform(
                get("/user/" + users.get("user").getId() + "/edit")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk()
                );

        mvc.perform(
            get("/user/" + users.get("superAdmin").getId() + "/edit")
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isOk()
        );
        mvc.perform(
            get("/user/" + users.get("notSavedUser").getId() + "/edit")
                    .contentType(MediaType.TEXT_HTML))
            .andExpect(status().isNotFound()
        );

//
    }
}
