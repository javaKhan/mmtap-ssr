package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.model.Patent;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatDao extends PagingAndSortingRepository<Patent,String>,JpaSpecificationExecutor<Patent> {

    @Query(value = "SELECT DATE_FORMAT(apply_date,'%Y') as year,count(*) as sum from patent where 1=1 ?1 GROUP BY DATE_FORMAT(apply_date,'%Y') ",
            nativeQuery = true)
    List annual(String condition);
}
