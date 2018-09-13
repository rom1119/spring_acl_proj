package com.example.demo.acl.validation;


import com.example.demo.acl.service.CustomAclService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AclPermissionConstraintValidator implements ConstraintValidator<ValidPermission, Integer> {

    private int mask;

    @Autowired
    private CustomAclService aclService;

//    @Override
//    public void initialize(final ValidPermission constraintAnnotation)
//    {
//        oldPasswordField = constraintAnnotation.oldPasswordField();
//        passwordField = constraintAnnotation.passwordField();
//        confirmPasswordField = constraintAnnotation.confirmPasswordField();
//
//        initErrors();
//    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
