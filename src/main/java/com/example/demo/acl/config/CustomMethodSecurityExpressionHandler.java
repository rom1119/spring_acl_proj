package com.example.demo.acl.config;

import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.repository.UserRepository;
import org.aopalliance.intercept.MethodInvocation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler
{
    private AuthenticationTrustResolver trustResolver =
                new AuthenticationTrustResolverImpl();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomAclService aclService;

    private CustomMethodSecurityExpressionRoot root;


    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {

         root = new CustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        root.setUserRepository(userRepository);
        root.setModelMapper(modelMapper);
        root.setAclService(aclService);

//            System.out.println(userRepository);

        return root;
    }

}
