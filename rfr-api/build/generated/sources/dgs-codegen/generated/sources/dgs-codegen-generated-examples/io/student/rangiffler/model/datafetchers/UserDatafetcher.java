package io.student.rangiffler.model.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import io.student.rangiffler.model.types.User;

@DgsComponent
public class UserDatafetcher {
  @DgsData(
      parentType = "Query",
      field = "user"
  )
  public User getUser(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
