package io.student.rangiffler.model.datafetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import graphql.schema.DataFetchingEnvironment;
import io.student.rangiffler.model.types.Country;
import java.util.List;

@DgsComponent
public class CountriesDatafetcher {
  @DgsData(
      parentType = "Query",
      field = "countries"
  )
  public List<Country> getCountries(DataFetchingEnvironment dataFetchingEnvironment) {
    return null;
  }
}
