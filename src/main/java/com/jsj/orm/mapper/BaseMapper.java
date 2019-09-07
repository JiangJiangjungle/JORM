package com.jsj.orm.mapper;

import com.jsj.orm.executor.BaseExecutor;
import com.jsj.orm.executor.Executor;
import com.jsj.orm.transaction.DefaultTransactionFactory;
import com.jsj.orm.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangshenjie
 */
public abstract class BaseMapper {
    private DefaultTransactionFactory transactionFactory = new DefaultTransactionFactory();
    private DataSource dataSource;
    private boolean autoCommit;
    private Executor executor;

    public BaseMapper(DataSource dataSource, boolean autoCommit) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    protected <E> List<E> selectList(String sql, ResultMapper<E> resultMapper, Object... params) {
        checkExecutor();
        try {
            return executor.query(sql, resultMapper, params);
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return new ArrayList<>(0);
    }

    protected <E> E selectOne(String sql, ResultMapper<E> resultMapper, Object... params) {
        List<E> result = selectList(sql, resultMapper, params);
        return result.size() > 0 ? result.get(0) : null;
    }

    protected boolean update(String sql, Object... params) {
        checkExecutor();
        boolean updated = false;
        try {
            updated = executor.update(sql, params);
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return updated;
    }

    public void commit() {
        if (executor == null) return;
        try {
            executor.commit();
        } catch (SQLException s) {
        } finally {
            executor = null;
        }
    }

    public void rollback() {
        if (executor == null) return;
        try {
            executor.rollback();
        } catch (SQLException s) {
        } finally {
            executor = null;
        }
    }

    private void checkExecutor() {
        if (executor == null) {
            Transaction transaction = transactionFactory.newTransaction(dataSource, autoCommit);
            executor = new BaseExecutor(transaction);
        }
    }
}
