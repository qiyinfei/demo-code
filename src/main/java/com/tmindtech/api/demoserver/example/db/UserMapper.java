package com.tmindtech.api.demoserver.example.db;

import com.tmindtech.api.demoserver.example.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {

    List<User> getUserByName(@Param("name") String name);

}
