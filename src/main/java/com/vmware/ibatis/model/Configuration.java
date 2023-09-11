package com.vmware.ibatis.model;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局配置类:存放核心配置文件解析出来的内容
 */
public class Configuration {
    //数据源对象
    private DataSource dataSource;

    //key：statementId
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();


    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }

    public void setMappedStatementMap(Map<String, MappedStatement> mappedStatementMap) {
        this.mappedStatementMap = mappedStatementMap;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
