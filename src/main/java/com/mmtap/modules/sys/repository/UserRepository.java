package com.mmtap.modules.sys.repository;

import com.mmtap.modules.sys.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    Optional<User> findByUsername(String username);
}