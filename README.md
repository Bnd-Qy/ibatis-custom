# ibatis-custom
自定义ORM Framework:Ibatis

## 设计流程

1.加载配置文件
* 创建Resource类：负责加载配置文件，加载成字节输入流，存到内存中
* 方法
```
public static InputStream getResourceAsSteam(String path){
}
```

2.创建两个JavaBean(容器对象)
* Configuration：存放sqlMapConfig.xml配置文件解析出来的内容
* MappedStatement:映射配置类，存储mapper.xml配置文件解析出的内容

3.解析配置文件，填充容器对象
* 创建SqlSessionFactoryBuild   
* 方法
```
public SqlSessionFactory build(InputStream inputStream){
    //解析配置文件(dom4j+xpath),封装Configuration
    //创建SqlSessionFactory
}
```
4.创建SqlSessionFactory接口及DefaultSqlSessionFactory
* 方法:SqlSession openSession(); [工厂模式]

5.创建SqlSession接口和DefaultSqlSession实现类
* selectList:查询所有
* selectOne: 查询单个
* delete:    删除
* update:    更新

6.创建Executor接口和实现类SimpleExecutor
* query(Configuration,MappedStatement,Param):执行就是底层的JDBC代码(数据库配置信息、sql配置信息)


## 如何使用?
* 引入pom依赖
* 创建SqlMapConfig.xml配置文件：数据库配置信息，存放mapper.xml
* 创建mapper.xml配置文件:存放sql信息、参数类型、返回值类型