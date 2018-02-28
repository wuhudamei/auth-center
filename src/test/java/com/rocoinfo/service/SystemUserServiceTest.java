package com.rocoinfo.service;

import com.rocoinfo.entity.admin.SystemUser;
import com.rocoinfo.service.admin.SystemUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/5/31 下午6:05</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemUserServiceTest {

    @Autowired
    private SystemUserService service;

    @Test
    public void testGetAllInfoByUsername() {
        SystemUser user = this.service.getAllInfoByUsername("test12");
        System.out.println(user);
    }
}
