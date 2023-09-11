package com.vmware.ibatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.vmware.ibatis.io.Resources;
import com.vmware.ibatis.model.Configuration;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {
    private Configuration configuration;

    public XMLConfigBuilder(){
        this.configuration=new Configuration();
    }
    /**
     * 使用dom4j+xpath解析配置文件
     */
    public Configuration parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            //get property
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }
        //创建数据源对象
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(dataSource);

        /**
         * 解析映射配置文件
         * 1.获取配置文件路径
         * 2.根据路径进行映射文件的加载解析
         * 3.封装到MappedStatement -> Configuration
         */
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream steam = Resources.getResourceAsSteam(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(steam);
        }
        return configuration;
    }
}
