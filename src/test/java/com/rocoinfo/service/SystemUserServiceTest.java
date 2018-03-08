package cn.damei.service;

import cn.damei.entity.admin.SystemUser;
import cn.damei.service.admin.SystemUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


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
