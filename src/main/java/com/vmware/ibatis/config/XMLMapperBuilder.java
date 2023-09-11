package com.vmware.ibatis.config;

import com.vmware.ibatis.model.Configuration;
import com.vmware.ibatis.model.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {
    private Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析配置文件为mappedStatement,封装到Configuration里的集合对象中
     */
    @SuppressWarnings("unchecked")
    public void parse(InputStream resourceAsSteam) throws DocumentException {
        Document document = new SAXReader().read(resourceAsSteam);
        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");
        List<Element> selectNodes = rootElement.selectNodes("//select");
        for (Element element : selectNodes) {
            String id = element.attributeValue("id");
            String resultType = element.attributeValue("resultType");
            String parameterType = element.attributeValue("parameterType");
            String sql = element.getTextTrim();
            String statementId = namespace + "." + id;
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setStatementId(statementId);
            mappedStatement.setResultType(resultType);
            mappedStatement.setParameterType(parameterType);
            mappedStatement.setSql(sql);
            mappedStatement.setSqlCommandType("select");
            configuration.getMappedStatementMap().put(statementId,mappedStatement);
        }
    }
}
