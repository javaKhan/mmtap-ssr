package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.vo.PatVo;
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

    @Query(value = "SELECT ipc,apply_person FROM (SELECT ipc,apply_person,count(*) AS cou FROM (" +
            " SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,apply_person " +
            " FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1) " +
            " WHERE apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR)) tmp GROUP BY ipc,apply_person ORDER BY cou DESC LIMIT 30) res ",nativeQuery = true)
    List analysis_categories_network();

    @Query(value = "SELECT ipc from ( SELECT ipc,count(*) AS cou FROM ( " +
            " SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,DATE_FORMAT(apply_date,'%Y') as nian " +
            " FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1) " +
            " WHERE apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR) ) tmp GROUP BY ipc ORDER BY cou desc LIMIT ?1 )res ",nativeQuery = true)
    List getTopIPC(int x);

    @Query(value = " select * from (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,DATE_FORMAT(apply_date,'%Y') as nian ,COUNT(*) as cou  FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1)  WHERE apply_date> DATE_SUB(NOW(),INTERVAL 10 YEAR) and ipctype_no is not null and ipctype_no<>''  GROUP BY ipc,nian  ) tmp where ipc in (?1) ",nativeQuery = true)
    List analysis_categories(List<String> s);



    @Query(value = "SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' AND apply_person_address LIKE ?1% AND apply_person_address LIKE %?2% GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou DESC LIMIT 1) aa ",nativeQuery = true)
    String findAreaMin(String province,String city);

    @Query(value = "SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' AND apply_person_address LIKE ?1% AND apply_person_address LIKE %?2% GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou DESC LIMIT 1) aa ",nativeQuery = true)
    String findAreaMax(String province,String city);

    @Query(value = "SELECT ye FROM (SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou LIMIT 1) aa",nativeQuery = true)
    String findAllAreaMin();
    @Query(value = "SELECT ye FROM (SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou DESC LIMIT 1) aa",nativeQuery = true)
    String findAllAreaMax();

    @Query(value = "SELECT apply_person,COUNT(*) as cou from patent where apply_person_address LIKE ?1% and apply_person_address like %?2% GROUP BY apply_person ORDER BY cou desc limit 3" ,nativeQuery = true)
    List findPartPersonTop3(String province, String city);
    @Query(value = "SELECT apply_person,COUNT(*) as cou from patent GROUP BY apply_person ORDER BY cou desc limit 3" ,nativeQuery = true)
    List findAllPersonTop3();

    @Query(value = "SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,COUNT(*) as cou FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1)  WHERE ipctype_no is not null and ipctype_no<>'' and apply_person_address like ?1% and apply_person_address like %?2% GROUP BY ipc ORDER BY cou desc limit 3",nativeQuery = true)
    List findPartIpcTop(String province, String city);
    @Query(value = "SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,COUNT(*) AS cou FROM patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1) WHERE ipctype_no IS NOT NULL AND ipctype_no<> '' GROUP BY ipc ORDER BY cou DESC LIMIT 3",nativeQuery = true)
    List findAllIpcTop();
}
