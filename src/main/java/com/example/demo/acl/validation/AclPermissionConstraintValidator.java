package com.example.demo.acl.validation;


import com.example.demo.acl.service.CustomAclService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AclPermissionConstraintValidator implements ConstraintValidator<ValidPermission, Integer> {

    private int mask;

    @Autowired
    private CustomAclService aclService;

    private String errorMessage;

    @Override
    public void initialize(final ValidPermission constraintAnnotation)
    {
        errorMessage = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext context) {

        int mask = integer;
        try {
            if(aclService.getPermissionFromMask(mask) == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(errorMessage).addConstraintViolation();
                return false;
            }
        } catch (IllegalAccessException e) {
            return false;
        }
        return true;
    }
}
