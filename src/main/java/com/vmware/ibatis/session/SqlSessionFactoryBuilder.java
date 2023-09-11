package com.vmware.ibatis.session;

import com.vmware.ibatis.config.XMLConfigBuilder;
import com.vmware.ibatis.model.Configuration;
import org.dom4j.DocumentException;

import java.io.InputStream;

/**
 * 构造器模式
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream inputStream) throws DocumentException {
        //1.解析配置文件
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse(inputStream);
        //2.创建SqlSessionFactory工厂对象
        return new DefaultSqlSessionFactory(configuration);
    }
}
