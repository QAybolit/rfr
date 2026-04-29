package io.student.rangiffler.jupiter.extention;

import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.service.UserClient;
import io.student.rangiffler.service.UserDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import static io.student.rangiffler.utils.DataUtils.getRandomName;
import static io.student.rangiffler.utils.DataUtils.getRandomPassword;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private final UserClient userClient = new UserDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(
                annotation -> {
                    UserJson user = new UserJson(
                            null,
                            getRandomName(),
                            getRandomPassword(),
                            true,
                            true,
                            true,
                            true
                    );
                    UserJson createdUser = userClient.registerUser(user);
                    context.getStore(NAMESPACE)
                            .put(context.getUniqueId(), createdUser);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), UserJson.class);
    }
}
