package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.model.PatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatTypeDao extends JpaRepository<PatType,String> {

}
