package io.student.rangiffler.data.repository.impl;

import io.student.rangiffler.config.Config;
import io.student.rangiffler.data.entity.user.CountryEntity;
import io.student.rangiffler.data.jpa.EntityManagers;
import io.student.rangiffler.data.repository.CountryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CountryRepositoryHibernate implements CountryRepository {

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.apiJdbcUrl());

    @Override
    public Optional<CountryEntity> findByCode(@NotNull String code) {
        try {
            return Optional.of(entityManager.createQuery("select c from CountryEntity c where c.code = :code",
                            CountryEntity.class)
                    .setParameter("code", code)
                    .getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }
}
