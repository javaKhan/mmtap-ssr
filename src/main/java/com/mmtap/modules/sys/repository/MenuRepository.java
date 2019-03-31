package com.mmtap.modules.sys.repository;

import com.mmtap.modules.sys.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/
public interface MenuRepository extends JpaRepository<Menu,Long> {

    /**
     * 根据父菜单获取子菜单
     * @param parent parent
     * @return child
     */
    List<Menu> findByParentOrderBySortAsc(Menu parent);
}
