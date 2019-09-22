package com.ddl.sys.web.system.user.mapper;

import com.ddl.sys.web.system.user.domain.SysUserRole;
import com.ddl.sys.web.system.user.domain.SysUserRoleCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleMapper {
    long countByExample(SysUserRoleCriteria example);

    int deleteByExample(SysUserRoleCriteria example);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    List<SysUserRole> selectByExample(SysUserRoleCriteria example);

    int updateByExampleSelective(@Param("record") SysUserRole record, @Param("example") SysUserRoleCriteria example);

    int updateByExample(@Param("record") SysUserRole record, @Param("example") SysUserRoleCriteria example);

    int batchInsert(List<SysUserRole> list);
}