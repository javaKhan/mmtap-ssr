package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.vo.PatVo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatDao extends PagingAndSortingRepository<Patent,String>,JpaSpecificationExecutor<Patent> {

//    @Query(value = "SELECT DATE_FORMAT(apply_date,'%Y') as year,count(*) as sum from patent where 1=1 ?1 GROUP BY DATE_FORMAT(apply_date,'%Y') ",
//            nativeQuery = true)
//    List annual(String condition);

//    @Query(value = "SELECT ipc,apply_person FROM (SELECT ipc,apply_person,count(*) AS cou FROM (" +
//            " SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,apply_person " +
//            " FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1) " +
//            " WHERE apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR)) tmp GROUP BY ipc,apply_person ORDER BY cou DESC LIMIT 30) res ",nativeQuery = true)
    @Query(nativeQuery = true,
    value = "SELECT t.ipc_type AS ipc,apply_person FROM pat_type t,patent p WHERE p.apply_no=t.pat AND p.apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR) GROUP BY ipc,apply_person ORDER BY count(*) DESC LIMIT 30 ")
    List analysis_categories_network();

    @Query(nativeQuery = true,
    value = " SELECT t.ipc_type as ipc from pat_type t ,patent p where p.apply_no= t.pat and p.apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR) GROUP BY t.ipc_type order by COUNT(*) desc LIMIT ?1 ")
    List getTopIPC(int x);




//    @Query(value = " select * from (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,DATE_FORMAT(apply_date,'%Y') as nian ,COUNT(*) as cou  FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1)  WHERE apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR) and ipctype_no is not null and ipctype_no<>''  GROUP BY ipc,nian  ) tmp where ipc in (?1) ",nativeQuery = true)
    @Query(value = " SELECT t.ipc_type AS ipc,DATE_FORMAT(apply_date,'%Y') AS nian,count(*) AS cou FROM pat_type t,patent p WHERE p.apply_no=t.pat AND p.apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR) AND t.ipc_type IN (?1) GROUP BY ipc,nian ",nativeQuery = true)
    List analysis_categories(List<String> s);



    @Query(value = "select nian from (SELECT DATE_FORMAT(apply_date,'%Y') AS nian,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' AND apply_person_address LIKE CONCAT(?1,'%')  AND apply_person_address LIKE CONCAT('%',?2,'%') and ipctype_no like  CONCAT('%',?3,'%') and ipctype_no like  CONCAT('%',?4,'%') GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou LIMIT 1) aa ",nativeQuery = true)
    String findAreaMin(String province,String city,String level1,String level2);

    @Query(value = "select nian from (SELECT DATE_FORMAT(apply_date,'%Y') AS nian,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' AND apply_person_address LIKE CONCAT(?1,'%') AND apply_person_address LIKE CONCAT('%',?2,'%') and ipctype_no like  CONCAT('%',?3,'%') and ipctype_no like  CONCAT('%',?4,'%') GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou DESC LIMIT 1 ) aa ",nativeQuery = true)
    String findAreaMax(String province,String city,String level1,String level2);

    @Query(value = "SELECT ye FROM (SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou LIMIT 1) aa",nativeQuery = true)
    String findAllAreaMin();
    @Query(value = "SELECT ye FROM (SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou DESC LIMIT 1) aa",nativeQuery = true)
    String findAllAreaMax();

    @Query(value = "SELECT apply_person,COUNT(*) as cou from patent where apply_person_address LIKE CONCAT(?1,'%') and apply_person_address like CONCAT('%',?2,'%') GROUP BY apply_person ORDER BY cou desc limit 3" ,nativeQuery = true)
    List findPartPersonTop3(String province, String city);
    @Query(value = "SELECT apply_person,COUNT(*) as cou from patent GROUP BY apply_person ORDER BY cou desc limit 3" ,nativeQuery = true)
    List findAllPersonTop3();


    @Query(value = " SELECT t.ipc_type AS ipc,count(*) as cou FROM pat_type t,patent p WHERE p.apply_no=t.pat and ipctype_no is not null and ipctype_no<>'' and apply_person_address like CONCAT(?1,'%') and apply_person_address LIKE CONCAT('%',?2,'%') GROUP BY ipc ORDER BY count(*) desc limit 3",nativeQuery = true)
    List findPartIpcTop(String province, String city);
    @Query(value = " SELECT t.ipc_type AS ipc,count(*) as cou FROM pat_type t,patent p WHERE p.apply_no=t.pat and ipctype_no IS NOT NULL AND ipctype_no<> '' GROUP BY ipc ORDER BY count(*) DESC LIMIT 3",nativeQuery = true)
    List findAllIpcTop();

//    @Query(value = "",nativeQuery = true)
//    Page findFulu(String city, String level2);
}
