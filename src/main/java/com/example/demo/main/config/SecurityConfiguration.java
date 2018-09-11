package com.example.demo.main.config;

import com.example.demo.acl.config.UserOwnerVoter;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.main.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private CustomPermissionEvaluator customPermissionEvaluator;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        return userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http
                .authorizeRequests()

                    .antMatchers("/").permitAll()
//                    .antMatchers("/static/*").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/user/**").hasRole("ADMIN")
//                    .accessDecisionManager(accessDecisionManager())

                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .loginProcessingUrl("/processlogin")
                    .permitAll()
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/logout_success")
                        .permitAll();


//        http.anonymous().authorities()
//        http.csrf().disable();
    }


//    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(),
//                new RoleVoter(),
                new RoleHierarchyVoter(roleHierarchy()),
                new UserOwnerVoter(),
                new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }

//    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl r = new RoleHierarchyImpl();
        r.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return r;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//        JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth.jdbcAuthentication();
//        builder.dataSource(dataSource);
//        builder.passwordEncoder(passwordEncoder());

//        User user = new User();
//        user.setEmail("rom@wp.pl");
//        user.setPassword(passwordEncoder().encode("asdasd"));
//        user.setEnabled(true);
//
////        userRepository.save(user);
//
//        System.out.println(user.getId());
//        System.out.println("ID");


//        JdbcUserDetailsManager userDetailsService = builder.getUserDetailsService();
//
//        userDetailsService.setUsersByUsernameQuery("select email,password,enabled from user where email = ?");
//        userDetailsService.setAuthoritiesByUsernameQuery("select u.email, r.name from role as r " +
//                "join user_has_role as uhr " +
//                "join user as u " +
//                " where u.email = ?");
//        userDetailsService.setGroupAuthoritiesByUsernameQuery("select r.id, r.name, p.name from role as r " +
//                "join role_has_privilege as rhp" +
//                "join privilege as p" +
//                " where r.name = ?");
//        userDetailsService.setAuthoritiesByUsernameQuery("select name from role where name = ?")
//        userDetailsService.setCreateUserSql("Hibernate: insert into user (email, enabled, first_name, last_name, password, token_expired, id) values (?, ?, ?, ?, ?, ?, ?)");
//        userDetailsService.createUser(new UserDetailsCustom(user));
//        userDetailsService.setCreateAuthoritySql("insert into role (id, name) values (?, ?)");


//        builder.withUser("asdasd").password("pass").roles("USER").accountLocked(false)
        ;

        auth.userDetailsService(userDetailsService);

    }

//    @Bean
//    public MethodSecurityExpressionHandler expressionHandler() {
//        DefaultMethodSecurityExpressionHandler dmseh = new DefaultMethodSecurityExpressionHandler();
//        dmseh.setPermissionEvaluator(customPermissionEvaluator);
//
//
//        return dmseh;
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }


}
