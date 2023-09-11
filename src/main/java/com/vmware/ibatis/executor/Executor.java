package com.vmware.ibatis.executor;

import com.vmware.ibatis.model.Configuration;
import com.vmware.ibatis.model.MappedStatement;

import java.util.List;

public interface Executor {
    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception;

    void close();
}
