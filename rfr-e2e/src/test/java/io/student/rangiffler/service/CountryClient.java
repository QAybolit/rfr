package io.student.rangiffler.service;

import io.student.rangiffler.model.types.Country;
import io.student.rangiffler.model.types.CountryInput;

import java.util.Optional;

public interface CountryClient {

    Optional<Country> findCountryByCode(CountryInput countryInput);
}
