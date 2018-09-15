package com.example.demo.acl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Autowired
    MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler;

//    @Autowired
//    PermissionEvaluator permissionEvaluator;

//    @Autowired
    private CustomMethodSecurityExpressionHandler customMethodSecurityExpressionHandler;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
//        ((DefaultMethodSecurityExpressionHandler)defaultMethodSecurityExpressionHandler).
//        System.out.println();
//        CustomMethodSecurityExpressionHandler expressionHandler =
//                new CustomMethodSecurityExpressionHandler();
//        customMethodSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        return defaultMethodSecurityExpressionHandler;
    }

}