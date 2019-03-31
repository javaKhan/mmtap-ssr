package com.mmtap;

import com.mmtap.modules.sys.model.Menu;
import com.mmtap.modules.sys.model.User;
import com.mmtap.modules.sys.repository.MenuRepository;
import com.mmtap.modules.sys.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SanJiBootApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void initUser(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("123456"));
//        user.setNickname("hello");
//        user.setPhone("1885");
//        user.setEmail("123@qq.com");
        userRepository.save(user);
    }


    @Test
    public void initMenu(){
        Menu root = new Menu();
        root.setName("root");
        root.setDepth(Menu.Type.ROOT);
        root.setSort(0L);
        menuRepository.save(root);
        Menu child = new Menu();
        child.setName("demo");
        child.setDepth(Menu.Type.SYSTEM);
        child.setSort(1L);
        child.setParent(root);
        menuRepository.save(child);
    }

    @Test
    public void contextLoads() {
    }

}

