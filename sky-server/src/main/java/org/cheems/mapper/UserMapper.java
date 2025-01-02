package org.cheems.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.cheems.entity.User;

@Mapper
public interface UserMapper {


    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openid);

    void insert(User user);
}
