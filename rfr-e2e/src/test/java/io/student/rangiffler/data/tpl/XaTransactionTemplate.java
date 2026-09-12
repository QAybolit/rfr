package io.student.rangiffler.data.tpl;

import com.atomikos.icatch.jta.UserTransactionImp;
import jakarta.transaction.Status;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

@Slf4j
public class XaTransactionTemplate {

    private final JdbcConnectionHolders connectionHolders;
    private final AtomicBoolean closeAfterAction = new AtomicBoolean(true);

    public XaTransactionTemplate(String... jdbcUrls) {
        this.connectionHolders = Connections.holders(jdbcUrls);
    }

    public XaTransactionTemplate holdConnectionAfterAction() {
        this.closeAfterAction.set(false);
        return this;
    }

    public <T> T execute(Supplier<T>... actions) {
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            T result = null;
            for (Supplier<T> action : actions) {
                result = action.get();
            }
            ut.commit();
            return result;
        } catch (Exception | AssertionError e) {
            try {
                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    ut.rollback();
                }
                log.error("XaTransactionTemplate execute failed", e);
                log.error("XaTransactionTemplate execute rollback: {}", e.getMessage());
//                ut.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (this.closeAfterAction.get()) {
                this.connectionHolders.close();
            }
        }
    }

    public void execute(Runnable... actions) {
        UserTransaction ut = new UserTransactionImp();
        try {
            ut.begin();
            for (Runnable action : actions) {
                action.run();
            }
            ut.commit();
        } catch (Exception | AssertionError e) {
            try {
                if (ut.getStatus() == Status.STATUS_ACTIVE) {
                    ut.rollback();
                }
                log.error("XaTransactionTemplate execute failed", e);
                log.error("XaTransactionTemplate execute rollback: {}", e.getMessage());
//                ut.rollback();
            } catch (SystemException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        } finally {
            if (this.closeAfterAction.get()) {
                this.connectionHolders.close();
            }
        }
    }
}
