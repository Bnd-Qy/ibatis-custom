package com.vmware.ibatis.session;

import com.vmware.ibatis.executor.Executor;
import com.vmware.ibatis.model.Configuration;
import com.vmware.ibatis.model.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object param) throws Exception {
        //将操作委派给底层的执行器 [1]数据库配置信息  [2]sql配置信息 [3]请求参数
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
        return executor.query(configuration, mappedStatement, param);
    }

    @Override
    public <T> T selectOne(String statementId, Object param) throws Exception {
        List<Object> list = selectList(statementId, param);
        if (list.size() == 1) {
            return (T) list;
        } else if (list.size() > 1) {
            throw new RuntimeException("返回多个结果");
        } else {
            return null;
        }
    }

    @Override
    public void close() {
        //委派给执行器
        executor.close();
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        //生成Mapper的代理对象
        Object proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            /**
             * 通过调用sqlSession里的方法完成方法调用
             * 规范:接口全路径要和namespace的值一致
             *     接口中的方法名要和id的值一致
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //确认statementId
                String namespace = mapperClass.getName();
                String id = method.getName();
                String statementId = namespace + "." + id;
                //调用方法确认
                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                String sqlCommandType = mappedStatement.getSqlCommandType();
                switch (sqlCommandType){
                    case "select":
                        //获取返回值类型，对返回值类型进行判断
                        Type genericReturnType = method.getGenericReturnType();
                        //判断是否为泛型类型
                        if (genericReturnType instanceof ParameterizedType){
                            if (args!=null){
                                return selectList(statementId,args[0]);
                            }
                            return selectList(statementId,null);
                        }
                        return selectOne(statementId,args[0]);
                    case "update":
                        //TODO
                        break;
                    case "delete":
                        //TODO
                        break;
                    case "insert":
                        //TODO
                        break;
                }
                return null;
            }
        });
        return (T) proxy;
    }
}
