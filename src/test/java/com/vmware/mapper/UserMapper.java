package com.vmware.mapper;

import com.vmware.model.User;

import java.util.List;

public interface UserMapper {
    List<User> findAll();
}
