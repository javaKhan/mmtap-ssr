package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.model.Patent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatDao extends PagingAndSortingRepository<Patent,String>,JpaSpecificationExecutor {


}
