package com.qims.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qims.domain.entity.Role;
import com.qims.domain.mapper.RoleMapper;
import com.qims.service.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
