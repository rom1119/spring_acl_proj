package com.example.demo.main.validation;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

public class OldPasswordConstraintValidator implements ConstraintValidator<ValidOldPassword, Object> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    String name;
    String canName;
    String typeName;

    @Override
    public void initialize(final ValidOldPassword constraintAnnotation)
    {
        name = constraintAnnotation.annotationType().getName();
        canName = constraintAnnotation.annotationType().getCanonicalName();
        typeName = constraintAnnotation.annotationType().getTypeName();

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {

        String passwordChecked = (String) o;
        String id = null;

        try {
            id = BeanUtils.getProperty(o, "id");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (id.isEmpty()) {
            return true;
        }

        System.out.println(name);
        System.out.println(canName);
        System.out.println(typeName);

        return false;
    }
}
