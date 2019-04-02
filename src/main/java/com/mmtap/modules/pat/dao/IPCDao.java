package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.model.IPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPCDao extends JpaRepository<IPC,String>{
}
