package com.mmtap.modules.sys.repository;

import com.mmtap.modules.sys.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mmtap.com
 * @date 2019/1/11
 **/
public interface LogRepository extends JpaRepository<Log, Long> {
}
