package io.student.rangiffler.data.jpa;

import jakarta.persistence.CacheRetrieveMode;
import jakarta.persistence.CacheStoreMode;
import jakarta.persistence.ConnectionConsumer;
import jakarta.persistence.ConnectionFunction;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FindOption;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.LockOption;
import jakarta.persistence.Query;
import jakarta.persistence.RefreshOption;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.TypedQueryReference;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaSelect;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class ThreadSafeEntityManager implements EntityManager {

    private final ThreadLocal<EntityManager> threadEm = new ThreadLocal<>();
    private final EntityManagerFactory emf;

    public ThreadSafeEntityManager(EntityManager delegate) {
        threadEm.set(delegate);
        emf = delegate.getEntityManagerFactory();
    }

    private EntityManager threadEm() {
        if (threadEm.get() == null || !threadEm.get().isOpen()) {
            threadEm.set(emf.createEntityManager());
        }
        return threadEm.get();
    }

    @Override
    public void close() {
        if (threadEm.get() != null && threadEm.get().isOpen()) {
            threadEm.get().close();
            threadEm.remove();
        }
    }

    @Override
    public void persist(Object o) {
        threadEm().persist(o);
    }

    @Override
    public @Nonnull <T> T merge(T t) {
        return threadEm().merge(t);
    }

    @Override
    public void remove(Object o) {
        threadEm().remove(o);
    }

    @Override
    public @Nullable <T> T find(Class<T> aClass, Object o) {
        return threadEm().find(aClass, o);
    }

    @Override
    public @Nullable <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return threadEm().find(aClass, o, map);
    }

    @Override
    public @Nullable <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return threadEm().find(aClass, o, lockModeType);
    }

    @Override
    public @Nullable <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return threadEm().find(aClass, o, lockModeType, map);
    }

    @Override
    public @Nullable <T> T find(Class<T> aClass, Object o, FindOption... findOptions) {
        return threadEm().find(aClass, o, findOptions);
    }

    @Override
    public @Nullable <T> T find(EntityGraph<T> entityGraph, Object o, FindOption... findOptions) {
        return threadEm().find(entityGraph, o, findOptions);
    }

    @Override
    public @Nonnull <T> T getReference(Class<T> aClass, Object o) {
        return threadEm().getReference(aClass, o);
    }

    @Override
    public @Nonnull <T> T getReference(T t) {
        return threadEm().getReference(t);
    }

    @Override
    public void flush() {
        threadEm().flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        threadEm().setFlushMode(flushModeType);
    }

    @Override
    public @Nonnull FlushModeType getFlushMode() {
        return threadEm().getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        threadEm().lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        threadEm().lock(o, lockModeType, map);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, LockOption... lockOptions) {
        threadEm().lock(o, lockModeType, lockOptions);
    }

    @Override
    public void refresh(Object o) {
        threadEm().refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        threadEm().refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        threadEm().refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        threadEm().refresh(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o, RefreshOption... refreshOptions) {
        threadEm().refresh(o, refreshOptions);
    }

    @Override
    public void clear() {
        threadEm().clear();
    }

    @Override
    public void detach(Object o) {
        threadEm().detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return threadEm().contains(o);
    }

    @Override
    public @Nonnull LockModeType getLockMode(Object o) {
        return threadEm().getLockMode(o);
    }

    @Override
    public void setCacheRetrieveMode(CacheRetrieveMode cacheRetrieveMode) {
        threadEm().setCacheRetrieveMode(cacheRetrieveMode);
    }

    @Override
    public void setCacheStoreMode(CacheStoreMode cacheStoreMode) {
        threadEm().setCacheStoreMode(cacheStoreMode);
    }

    @Override
    public @Nonnull CacheRetrieveMode getCacheRetrieveMode() {
        return threadEm().getCacheRetrieveMode();
    }

    @Override
    public @Nonnull CacheStoreMode getCacheStoreMode() {
        return threadEm().getCacheStoreMode();
    }

    @Override
    public void setProperty(String s, Object o) {
        threadEm().setProperty(s, o);
    }

    @Override
    public @Nonnull Map<String, Object> getProperties() {
        return threadEm().getProperties();
    }

    @Override
    public @Nonnull Query createQuery(String s) {
        return threadEm().createQuery(s);
    }

    @Override
    public @Nonnull <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return threadEm().createQuery(criteriaQuery);
    }

    @Override
    public @Nonnull <T> TypedQuery<T> createQuery(CriteriaSelect<T> criteriaSelect) {
        return threadEm().createQuery(criteriaSelect);
    }

    @Override
    public @Nonnull Query createQuery(CriteriaUpdate<?> criteriaUpdate) {
        return threadEm().createQuery(criteriaUpdate);
    }

    @Override
    public @Nonnull Query createQuery(CriteriaDelete<?> criteriaDelete) {
        return threadEm().createQuery(criteriaDelete);
    }

    @Override
    public @Nonnull <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return threadEm().createQuery(s, aClass);
    }

    @Override
    public @Nonnull Query createNamedQuery(String s) {
        return threadEm().createNamedQuery(s);
    }

    @Override
    public @Nonnull <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return threadEm().createNamedQuery(s, aClass);
    }

    @Override
    public @Nonnull <T> TypedQuery<T> createQuery(TypedQueryReference<T> typedQueryReference) {
        return threadEm().createQuery(typedQueryReference);
    }

    @Override
    public @Nonnull Query createNativeQuery(String s) {
        return threadEm().createNativeQuery(s);
    }

    @Override
    public @Nonnull <T> Query createNativeQuery(String s, Class<T> aClass) {
        return threadEm().createNativeQuery(s, aClass);
    }

    @Override
    public @Nonnull Query createNativeQuery(String s, String s1) {
        return threadEm().createNativeQuery(s, s1);
    }

    @Override
    public @Nonnull StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return threadEm().createNamedStoredProcedureQuery(s);
    }

    @Override
    public @Nonnull StoredProcedureQuery createStoredProcedureQuery(String s) {
        return threadEm().createStoredProcedureQuery(s);
    }

    @Override
    public @Nonnull StoredProcedureQuery createStoredProcedureQuery(String s, Class<?>... classes) {
        return threadEm().createStoredProcedureQuery(s, classes);
    }

    @Override
    public @Nonnull StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return threadEm().createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        threadEm().joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return threadEm().isJoinedToTransaction();
    }

    @Override
    public @Nonnull <T> T unwrap(Class<T> aClass) {
        return threadEm().unwrap(aClass);
    }

    @Override
    public @Nonnull Object getDelegate() {
        return threadEm().getDelegate();
    }

    @Override
    public boolean isOpen() {
        return threadEm().isOpen();
    }

    @Override
    public @Nonnull EntityTransaction getTransaction() {
        return threadEm().getTransaction();
    }

    @Override
    public @Nonnull EntityManagerFactory getEntityManagerFactory() {
        return threadEm().getEntityManagerFactory();
    }

    @Override
    public @Nonnull CriteriaBuilder getCriteriaBuilder() {
        return threadEm().getCriteriaBuilder();
    }

    @Override
    public @Nonnull Metamodel getMetamodel() {
        return threadEm().getMetamodel();
    }

    @Override
    public @Nonnull <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return threadEm().createEntityGraph(aClass);
    }

    @Override
    public @Nonnull EntityGraph<?> createEntityGraph(String s) {
        return threadEm().createEntityGraph(s);
    }

    @Override
    public @Nonnull EntityGraph<?> getEntityGraph(String s) {
        return threadEm().getEntityGraph(s);
    }

    @Override
    public @Nonnull <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return threadEm().getEntityGraphs(aClass);
    }

    @Override
    public <C> void runWithConnection(ConnectionConsumer<C> connectionConsumer) {
        threadEm().runWithConnection(connectionConsumer);
    }

    @Override
    public @Nonnull <C, T> T callWithConnection(ConnectionFunction<C, T> connectionFunction) {
        return threadEm().callWithConnection(connectionFunction);
    }
}
