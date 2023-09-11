package com.vmware.test;

import com.vmware.ibatis.io.Resources;
import com.vmware.ibatis.session.SqlSession;
import com.vmware.ibatis.session.SqlSessionFactory;
import com.vmware.ibatis.session.SqlSessionFactoryBuilder;
import com.vmware.mapper.UserMapper;
import com.vmware.model.User;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class UserMapperTest {
    @Test
    public void testQuery() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        System.out.println(resourceAsSteam);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.findAll();
        System.out.println(users);
    }
}
