package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.jupiter.annotation.User;
import io.student.rangiffler.model.TestData;
import io.student.rangiffler.model.UserJson;
import io.student.rangiffler.service.UsersClient;
import io.student.rangiffler.service.UsersDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.List;
import java.util.Optional;

import static io.student.rangiffler.jupiter.extension.TestMethodContextExtension.context;
import static io.student.rangiffler.utils.FakeDataUtils.randomUsername;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private final UsersClient userClient = new UsersDbClient();
    private static final String DEFAULT_PASSWORD = "12345";

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                User.class
        ).ifPresent(userAnno -> {
                    final String username = "".equals(userAnno.username()) ? randomUsername() : userAnno.username();
                    UserJson createdUser = userClient.createUser(username, DEFAULT_PASSWORD);
                    List<UserJson> incomeInvitations = userClient.createIncomeInvitation(createdUser, userAnno.incomeInvitations());
                    List<UserJson> outcomeInvitations = userClient.createOutcomeInvitation(createdUser, userAnno.outcomeInvitations());
                    List<UserJson> friends = userClient.createFriends(createdUser, userAnno.friends());
                    createdUser = createdUser.addTestData(new TestData(
                            DEFAULT_PASSWORD,
                            incomeInvitations,
                            outcomeInvitations,
                            friends
                    ));

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createdUser);
                }
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return UserJson.class.isAssignableFrom(parameterContext.getParameter().getType())
                && AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), UserJson.class);
    }

    public static Optional<UserJson> createdUser() {
        final ExtensionContext methodContext = context();
        return Optional.ofNullable(methodContext.getStore(NAMESPACE)
                .get(methodContext.getUniqueId(), UserJson.class));
    }
}
