package com.vmware.ibatis.session;

import java.util.List;

public interface SqlSession {
    /**
     * 查询多个结果
     */
    <E> List<E> selectList(String statementId,Object param) throws Exception;
    /**
     * 查询单个结果
     */
    <T> T selectOne(String statementId,Object param) throws Exception;
    /**
     * 释放资源
     */
    void close();

    <T> T getMapper(Class<?> mapperClass);
}
