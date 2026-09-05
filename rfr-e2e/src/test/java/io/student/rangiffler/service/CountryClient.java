package io.student.rangiffler.service;

import io.student.rangiffler.model.CountryJson;

import java.util.Optional;

public interface CountryClient {

    Optional<CountryJson> findCountryByCode(String countryCode);
}
