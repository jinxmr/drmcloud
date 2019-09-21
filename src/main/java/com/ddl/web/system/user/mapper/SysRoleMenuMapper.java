package com.ddl.web.system.user.mapper;

import com.ddl.web.system.user.domain.SysRoleMenu;
import com.ddl.web.system.user.domain.SysRoleMenuCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleMenuMapper {
    long countByExample(SysRoleMenuCriteria example);

    int deleteByExample(SysRoleMenuCriteria example);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);

    List<SysRoleMenu> selectByExample(SysRoleMenuCriteria example);

    int updateByExampleSelective(@Param("record") SysRoleMenu record, @Param("example") SysRoleMenuCriteria example);

    int updateByExample(@Param("record") SysRoleMenu record, @Param("example") SysRoleMenuCriteria example);

    int batchInsert(List<SysRoleMenu> list);
}