package com.github.kingwaggs.productmanager.coupang.domain.annotation;

import com.github.kingwaggs.productmanager.coupang.domain.DeleteProductRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeleteProductRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DeleteProductRequestConstraint {
    String message() default "Invalid delete product request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
