package io.student.rangiffler.model.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import io.student.rangiffler.model.types.User;
import org.springframework.data.domain.Page;

@DgsComponent
public class UsersDatafetcher {
  @DgsData(
      parentType = "Query",
      field = "users"
  )
  public Page<User> getUsers(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
