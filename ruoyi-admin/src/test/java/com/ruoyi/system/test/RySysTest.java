package com.ruoyi.system.test;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RySysTest {

    @Resource
    private SysUserMapper userMapper;

    @Test
    public void testSelectUserById() {
        SysUser sysUser = userMapper.selectUserById(1L);
        log.info(sysUser.getUserName() + "-" + sysUser.getLoginIp());
    }
}
