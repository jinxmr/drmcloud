package com.ddl.web.system.user.mapper;

import com.ddl.web.system.user.domain.SysUserRole;
import com.ddl.web.system.user.domain.SysUserRoleCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRoleMapper {
    long countByExample(SysUserRoleCriteria example);

    int deleteByExample(SysUserRoleCriteria example);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    List<SysUserRole> selectByExample(SysUserRoleCriteria example);

    int updateByExampleSelective(@Param("record") SysUserRole record, @Param("example") SysUserRoleCriteria example);

    int updateByExample(@Param("record") SysUserRole record, @Param("example") SysUserRoleCriteria example);
}