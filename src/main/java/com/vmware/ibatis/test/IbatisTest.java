package com.vmware.ibatis.test;

import com.vmware.ibatis.io.Resources;

import java.io.InputStream;

public class IbatisTest {
    public static void main(String[] args) {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        System.out.println(resourceAsSteam);
    }
}
