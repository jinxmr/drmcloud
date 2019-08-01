package com.ddl;

import com.ddl.web.system.generater.domain.TableInfo;
import com.ddl.web.system.generater.mapper.GenMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DrmApplicationTests {

	@Autowired
	private GenMapper genMapper;
	@Test
	public void contextLoads() {
		PageHelper.startPage(0, 10);
		TableInfo tableInfo = new TableInfo();
		List<TableInfo> tableInfoList = genMapper.selectTableList(tableInfo);
		System.out.println(tableInfoList.size());
	}

}
