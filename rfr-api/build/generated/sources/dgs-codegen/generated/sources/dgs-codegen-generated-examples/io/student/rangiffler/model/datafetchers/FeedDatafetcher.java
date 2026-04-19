package io.student.rangiffler.model.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import io.student.rangiffler.model.types.Feed;

@DgsComponent
public class FeedDatafetcher {
  @DgsData(
      parentType = "Query",
      field = "feed"
  )
  public Feed getFeed(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
