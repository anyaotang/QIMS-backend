package com.qims.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qims.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询 ID
     */
    @Select("SELECT id FROM sys_user WHERE username = #{username} AND deleted = 0 LIMIT 1")
    Long selectIdByUsername(@Param("username") String username);
}
