package com.vmware.ibatis.io;

import java.io.InputStream;

public class Resources {
    /**
     * 根据配置文件路径，加载配置文件成字节输入流，存入到内存中
     */
    public static InputStream getResourceAsSteam(String path){
       return Resources.class.getClassLoader().getResourceAsStream(path);
    }
}
