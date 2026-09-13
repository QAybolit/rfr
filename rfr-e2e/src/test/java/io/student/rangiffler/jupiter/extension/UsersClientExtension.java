package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.service.UsersClient;
import io.student.rangiffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

/**
 * Класс, который инициализирует поле UserClient в тестовом классе за счет иньекции.
 * Тестовый класс должен быть помечен вннотацией ExtendWith(UsersClientExtension.class)
 */
public class UsersClientExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.getType().isAssignableFrom(UsersClient.class)) {
                field.setAccessible(true);
                field.set(testInstance, new UsersDbClient());
            }
        }
    }
}
