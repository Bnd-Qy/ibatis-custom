package com.vmware.ibatis.utils;

import java.util.ArrayList;
import java.util.List;

public class ParameterMappingTokenHandler implements TokenHandler {

    /**
     * 存放SQL参数名列表
     */
    private List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    @Override
    public String handleToken(String content) {
        //先构建参数映射
        parameterMappings.add(buildParameterMapping(content));
        //如何替换很简单，永远是一个问号，但是参数的信息要记录在parameterMappings里面供后续使用
        return "?";
    }

    //构建参数映射
    private ParameterMapping buildParameterMapping(String content) {
        return new ParameterMapping(content);
    }

}
