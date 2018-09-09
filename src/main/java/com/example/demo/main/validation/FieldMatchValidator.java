package com.example.demo.main.validation;


import org.apache.commons.beanutils.BeanUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    private String firstFieldName;
    private String secondFieldName;
    private String errorMessage;

    @Override
    public void initialize(final FieldMatch constraintAnnotation)
    {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        errorMessage = constraintAnnotation.message();

//        System.out.println(firstFieldName);
//        System.out.println(secondFieldName);
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        boolean toReturn = false;
//        System.out.println("isValid");
        try
        {
            final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

//            System.out.println(firstObj);
//            System.out.println(secondObj);
//            System.out.println(firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj));


            toReturn = firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception ignore)
        {
            // ignore
        }

        if(!toReturn) {
            context.disableDefaultConstraintViolation();
            //In the initialiaze method you get the errorMessage: constraintAnnotation.message();
            context.buildConstraintViolationWithTemplate(errorMessage).addNode(secondFieldName).addConstraintViolation();
        }
        return toReturn;
    }
}