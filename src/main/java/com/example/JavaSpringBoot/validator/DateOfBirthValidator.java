package com.example.JavaSpringBoot.validator;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirthConstraint, LocalDate> {

  private int min;

  @Override
  public boolean isValid(
      LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
    if (localDate == null) {
      return true;
    }
    return localDate.plusYears(min).isBefore(LocalDate.now())
        || localDate.plusYears(min).isEqual(LocalDate.now());
  }

  @Override
  public void initialize(DateOfBirthConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    min = constraintAnnotation.min();
  }
}
