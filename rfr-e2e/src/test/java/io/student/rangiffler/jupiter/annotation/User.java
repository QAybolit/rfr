package io.student.rangiffler.jupiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface User {

    String username() default "";

    int friends() default 0;

    int incomeInvitations() default 0;

    int outcomeInvitations() default 0;

    int otherPeoples() default 0;
}
