package io.student.rangiffler.jupiter.annotation;

import io.student.rangiffler.jupiter.extension.UserExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(UserExtension.class)
public @interface User {
}
