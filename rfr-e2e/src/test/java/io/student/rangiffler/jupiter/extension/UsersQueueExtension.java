package io.student.rangiffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.util.StopWatch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create("UsersQueueExtension");

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome
    ) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("Oleg", "12345", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("Egor", "12345", "Ivan", null, null));
        WITH_FRIEND_USERS.add(new StaticUser("Ivan", "12345", "Egor", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("Polina", "12345", null, "Kirill", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("Kirill", "12345", null, null, "Polina"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIENDS, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void beforeEach(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(
                        ut -> {
                            Optional<StaticUser> user = Optional.empty();
                            StopWatch stopWatch = new StopWatch();
                            stopWatch.start();
                            while (user.isEmpty() && stopWatch.getTotalTime(TimeUnit.SECONDS) < 30) {
                                user = getUserFromQueue(ut);
                            }
                            stopWatch.stop();
                            Allure.getLifecycle().updateTestCase(
                                    testCase -> testCase.setStart(new Date().getTime())
                            );
                            user.ifPresentOrElse(u -> {
                                        Map<UserType, StaticUser> map = (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                                .computeIfAbsent(
                                                        context.getUniqueId(),
                                                        key -> new HashMap<>()
                                                );
                                        map.put(ut, u);
                                        context.getStore(NAMESPACE).put(context.getUniqueId(), map);
                                    },
                                    () -> {
                                        throw new IllegalStateException("Can't find user after 30 sec");
                                    }
                            );
                        }
                );
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterEach(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).remove(context.getUniqueId(), Map.class);
        if (map != null) {
            for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
                addUserToQueue(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public @Nullable Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class)
                .get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get());
    }

    private Optional<StaticUser> getUserFromQueue(UserType userType) {
        return switch (userType.value()) {
            case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
            case WITH_FRIENDS -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
            case WITH_INCOME_REQUEST -> Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
            case WITH_OUTCOME_REQUEST -> Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
        };
    }

    private void addUserToQueue(UserType type, StaticUser user) {
        switch (type.value()) {
            case UserType.Type.EMPTY -> EMPTY_USERS.add(user);
            case UserType.Type.WITH_FRIENDS -> WITH_FRIEND_USERS.add(user);
            case UserType.Type.WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS.add(user);
            case UserType.Type.WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS.add(user);
            default -> throw new IllegalStateException("Unexpected value: " + type.value());
        }
    }
}
