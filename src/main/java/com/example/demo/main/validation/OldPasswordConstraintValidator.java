package com.example.demo.main.validation;

import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.UserRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Optional;

public class OldPasswordConstraintValidator implements ConstraintValidator<ValidChangePassword, Object> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public static HashMap<String, String> errors = new HashMap<>();

    String oldPasswordField;
    String passwordField;
    String confirmPasswordField;

    private void initErrors()
    {
        errors.put("oldPass", "Hasło niepoprawne");
        errors.put("confPass", "Wpisz dwa razy to samo hasło");
    }

    @Override
    public void initialize(final ValidChangePassword constraintAnnotation)
    {
        oldPasswordField = constraintAnnotation.oldPasswordField();
        passwordField = constraintAnnotation.passwordField();
        confirmPasswordField = constraintAnnotation.confirmPasswordField();

        initErrors();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {

        String id = null;
        String oldPass = null;
        String pass = null;
        String confPass = null;

        try {
            id = BeanUtils.getProperty(o, "id");

             oldPass = BeanUtils.getProperty(o, oldPasswordField);
             pass = BeanUtils.getProperty(o, passwordField);
             confPass = BeanUtils.getProperty(o, confirmPasswordField);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
//
        if (id.isEmpty()) {
            return true;
        }

        User user = userRepository.findById(Long.valueOf(id)).get();

        if ( !passwordEncoder.matches(oldPass, user.getPassword())) {
//                context.disableDefaultConstraintViolation();
                //In the initialiaze method you get the errorMessage: constraintAnnotation.message();
                context.buildConstraintViolationWithTemplate(errors.get("oldPass")).addNode(oldPasswordField).addConstraintViolation();
                return false;
        }


        return true;
    }
}
