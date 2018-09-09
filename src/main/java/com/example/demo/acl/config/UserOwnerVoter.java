package com.example.demo.acl.config;

import com.example.demo.user.model.User;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public class UserOwnerVoter implements AccessDecisionVoter<Object> {


    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        System.out.println("supports(ConfigAttribute configAttribute)");
        System.out.println(configAttribute.getAttribute());


        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        System.out.println("supports(Class<?> aClass)");
        System.out.println(aClass.toString());

        return true;
    }

    @Override
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {

        if (o instanceof User) {
            System.out.println("vote123456u7yiy765432");
            User a = (User) o;
            System.out.println(a.getFirstName());

        }

        System.out.println("vote");
//        System.out.println(a);

        for (ConfigAttribute attribute: collection) {
            System.out.println("vote1");
            System.out.println(attribute);
            System.out.println("vote2");

        }
//        if (user == authentication.getPrincipal()) {
//            return ACCESS_GRANTED;
//        }

        return ACCESS_GRANTED;
    }
}
