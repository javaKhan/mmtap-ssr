package com.mmtap.modules.pat.dao;

import com.mmtap.modules.pat.vo.PatVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Component
public class PatFrontDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List annual(PatVo vo) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT DATE_FORMAT(apply_date,'%Y') as year,count(*) as sum from patent where 1=1 ");
        if (StringUtils.isNotEmpty(vo.getLevel1())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel1().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getLevel2())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel2().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getProvince())) {
            sb.append(" and apply_person_address like  '" + vo.getProvince().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getCity())) {
            sb.append(" and apply_person_address like  '%" + vo.getCity().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getStartYear())) {
            sb.append(" and apply_date >='" + vo.getStartYear().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndYear())) {
            sb.append(" and apply_date <='" + vo.getEndYear().trim() + "' ");
        }
        sb.append(" GROUP BY DATE_FORMAT(apply_date,'%Y') ");
        Query query = entityManager.createNativeQuery(sb.toString());
        List list = query.getResultList();
        return list;
    }

    public List patentee(PatVo vo) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select apply_person as person,count(*) as sum from patent where 1=1 " +
                " and apply_person is not NULL and apply_person<>''");
        if (StringUtils.isNotEmpty(vo.getLevel1())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel1().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getLevel2())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel2().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getProvince())) {
            sb.append(" and apply_person_address like  '" + vo.getProvince().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getCity())) {
            sb.append(" and apply_person_address like  '%" + vo.getCity().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getStartYear())) {
            sb.append(" and apply_date >='" + vo.getStartYear().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndYear())) {
            sb.append(" and apply_date <='" + vo.getEndYear().trim() + "' ");
        }
        sb.append(" group by apply_person ");
        sb.append(" LIMIT 50");
        Query query = entityManager.createNativeQuery(sb.toString());
        List list = query.getResultList();
        return list;
    }

    public List category(PatVo vo) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select ipctype_no as ipc ,COUNT(*) as sum from patent where 1=1 " +
                " and ipctype_no is not null and ipctype_no<>'' ");
        if (StringUtils.isNotEmpty(vo.getLevel1())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel1().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getLevel2())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel2().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getProvince())) {
            sb.append(" and apply_person_address like  '" + vo.getProvince().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getCity())) {
            sb.append(" and apply_person_address like  '%" + vo.getCity().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getStartYear())) {
            sb.append(" and apply_date >='" + vo.getStartYear().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndYear())) {
            sb.append(" and apply_date <='" + vo.getEndYear().trim() + "' ");
        }
        sb.append(" GROUP BY ipctype_no ");
        sb.append(" LIMIT 100");
        Query query = entityManager.createNativeQuery(sb.toString());
        List list = query.getResultList();
        return list;
    }

    public List network(PatVo vo) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT apply_person,ipctype_no from patent WHERE 1=1 ");
        if (StringUtils.isNotEmpty(vo.getLevel1())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel1().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getLevel2())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel2().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getProvince())) {
            sb.append(" and apply_person_address like  '" + vo.getProvince().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getCity())) {
            sb.append(" and apply_person_address like  '%" + vo.getCity().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getStartYear())) {
            sb.append(" and apply_date >='" + vo.getStartYear().trim() + "' ");
        }
        if (StringUtils.isNotEmpty(vo.getEndYear())) {
            sb.append(" and apply_date <='" + vo.getEndYear().trim() + "' ");
        }
        sb.append(" LIMIT 300");
        Query query = entityManager.createNativeQuery(sb.toString());
        List list = query.getResultList();
        return list;
    }

    public List ana_ipc(PatVo vo) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(p.ipctype_no,';',b.help_topic_id+1),';',-1) AS ipc,count(*)+'' as cou from patent p JOIN mysql.help_topic b ON b.help_topic_id< (length(p.ipctype_no)-length(REPLACE (p.ipctype_no,';',''))+1) where ipctype_no is not NULL and ipctype_no<>'' ");
        if (StringUtils.isNotEmpty(vo.getLevel1())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel1().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getLevel2())) {
            sb.append(" and ipctype_no like  '%" + vo.getLevel2().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getProvince())) {
            sb.append(" and apply_person_address like  '" + vo.getProvince().trim() + "%' ");
        }
        if (StringUtils.isNotEmpty(vo.getCity())) {
            sb.append(" and apply_person_address like  '%" + vo.getCity().trim() + "%' ");
        }
        sb.append(" group by ipc ");
        sb.append(" LIMIT 30");
        Query query = entityManager.createNativeQuery(sb.toString());
        List list = query.getResultList();
        return list;
    }

//    public String findAllAreaMin() {
//        String sql = " SELECT ye FROM (SELECT DATE_FORMAT(apply_date,'%Y') AS ye,count(*) AS cou FROM patent WHERE apply_date IS NOT NULL AND apply_date<> '' GROUP BY DATE_FORMAT(apply_date,'%Y') ORDER BY cou DESC LIMIT 1) aa ";
//        Query query = entityManager.createNativeQuery(sql);
//        Object obj = query.getSingleResult();
//        return obj.toString();
//    }


}
