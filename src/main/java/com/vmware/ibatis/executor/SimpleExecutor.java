package com.vmware.ibatis.executor;

import com.vmware.ibatis.config.BoundSql;
import com.vmware.ibatis.model.Configuration;
import com.vmware.ibatis.model.MappedStatement;
import com.vmware.ibatis.utils.GenericTokenParser;
import com.vmware.ibatis.utils.ParameterMapping;
import com.vmware.ibatis.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class SimpleExecutor implements Executor {
    private final String OPEN_TOKEN = "#{";
    private final String CLOSE_TOKEN = "}";
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception {
        //1.获取数据库连接
        connection = configuration.getDataSource().getConnection();
        //2.获取preparedStatement预编译对象
        String sql = mappedStatement.getSql();
        //3.对sql语句进行处理
        BoundSql boundSql = getBoundSql(sql);
        String finalSql = boundSql.getFinalSql();
        preparedStatement = connection.prepareStatement(finalSql);
        //设置参数
        String parameterType = mappedStatement.getParameterType();
        if (parameterType != null) {
            Class<?> parameterTypeClass = Class.forName(parameterType);
            //对参数列表进行迭代，从param中反射获取属性为Statement赋值
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String paramName = parameterMapping.getContent();
                Field declaredField = parameterTypeClass.getDeclaredField(paramName);
                //set visit 强制访问
                declaredField.setAccessible(true);
                Object value = declaredField.get(param);
                //赋值占位符
                preparedStatement.setObject(i + 1, value);
            }
        }
        //4.执行sql
        resultSet = preparedStatement.executeQuery();
        //5.处理返回结果集
        ArrayList<E> list = new ArrayList<>();
        while (resultSet.next()) {
            //MetaData中包含字段名和属性信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            //获取要封装的结果类型
            String resultType = mappedStatement.getResultType();
            Class<?> resultTypeClass = Class.forName(resultType);
            Object o = resultTypeClass.newInstance();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                //获取字段名
                String columnName = metaData.getColumnName(i);
                //获取字段值
                Object value = resultSet.getObject(columnName);
                //通过属性描述器获取结果属性的设置方法,对属性进行设置
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(o, value);
            }
            list.add((E) o);
        }
        return list;
    }

    /**
     * 1.替换#{}占位符换成?
     * 2.将参数名保留
     */
    private BoundSql getBoundSql(String sql) {
        //1.创建标记处理器:配合标记解析器完成标记处理
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        //2.创建标记解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser(OPEN_TOKEN, CLOSE_TOKEN, parameterMappingTokenHandler);
        String finalSql = genericTokenParser.parse(sql);
        //#{}参数集合
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        //封装BoundSql
        return new BoundSql(finalSql, parameterMappings);
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
