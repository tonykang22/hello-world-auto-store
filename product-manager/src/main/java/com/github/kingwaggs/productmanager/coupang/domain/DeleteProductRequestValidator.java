package com.github.kingwaggs.productmanager.coupang.domain;

import com.github.kingwaggs.productmanager.coupang.domain.annotation.DeleteProductRequestConstraint;
import com.github.kingwaggs.productmanager.coupang.domain.dto.DeleteProductRequest;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DeleteProductRequestValidator
        implements ConstraintValidator<DeleteProductRequestConstraint, DeleteProductRequest> {

    @Override
    public boolean isValid(DeleteProductRequest value, ConstraintValidatorContext context) {
        if (CollectionUtils.isEmpty(value.getProductIdList()) && CollectionUtils.isEmpty(value.getKeywordList())) {
            return false;
        }
        return true;
    }
}
