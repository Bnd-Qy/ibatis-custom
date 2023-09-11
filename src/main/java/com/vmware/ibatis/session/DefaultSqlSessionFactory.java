package com.vmware.ibatis.session;

import com.vmware.ibatis.executor.Executor;
import com.vmware.ibatis.executor.SimpleExecutor;
import com.vmware.ibatis.model.Configuration;

/**
 * SqlSessionFactory默认实现类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        //1.创建执行器对象
        Executor executor = new SimpleExecutor();
        //2.生产sqlSession对象
        return new DefaultSqlSession(configuration,executor);
    }
}
