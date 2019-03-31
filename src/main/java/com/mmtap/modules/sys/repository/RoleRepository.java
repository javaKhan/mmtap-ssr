package com.mmtap.modules.sys.repository;

import com.mmtap.modules.sys.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * 根据名称模糊查询
     * @param name name
     * @param pageable pageable
     * @return Page<Role>
     */
    Page<Role> findAllByNameContains(String name, Pageable pageable);
}
