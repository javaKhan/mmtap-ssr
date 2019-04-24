package com.mmtap.modules.pat.service.impl;

import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.dao.PatFrontDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.CatVo;
import com.mmtap.modules.pat.vo.PatVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatServiceImpl implements PatService {
    @Autowired
    private PatDao patDao;

    @Autowired
    private PatFrontDao patFrontDao;

    @Transactional
    public void saveImport(List<Patent> pats){
        patDao.saveAll(pats);
    }

    @Override
    public Page list(Pageable pageable, PatVo patVo) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.isNotEmpty(patVo.getLevel1())){
                    predicates.add(criteriaBuilder.like(root.<String>get("IPCTypeNo"), "%" + patVo.getLevel1() + "%"));
                }
                if (StringUtils.isNotEmpty(patVo.getLevel2())){
                    predicates.add(criteriaBuilder.like(root.<String>get("IPCTypeNo"), "%" + patVo.getLevel2() + "%"));
                }
                if (StringUtils.isNotEmpty(patVo.getProvince())){
                    predicates.add(criteriaBuilder.like(root.<String>get("applyPersonAddress"), "%" + patVo.getProvince() + "%"));
                }
                if (StringUtils.isNotEmpty(patVo.getCity())){
                    predicates.add(criteriaBuilder.like(root.<String>get("applyPersonAddress"), "%" + patVo.getCity() + "%"));
                }
                 query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                return null;
            }
        };
        Page page = patDao.findAll(specification,pageable);
        return page;
    }


    @Override
    public List annual(PatVo vo) {
        List list =  patFrontDao.annual(vo);
        return list ;
    }

    @Override
    public List patentee(PatVo vo) {
        return patFrontDao.patentee(vo);
    }

    @Override
    public List category(PatVo vo) {
        return patFrontDao.category(vo);
    }

    @Override
    public List network(PatVo vo) {
        return patFrontDao.network(vo);
    }

    @Override
    public List exportExcel(PatVo patVo) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (StringUtils.isNotEmpty(patVo.getLevel1())){
                    predicates.add(criteriaBuilder.like(root.<String>get("IPCTypeNo"), "%" + patVo.getLevel1() + "%"));
                }
                if (StringUtils.isNotEmpty(patVo.getLevel2())){
                    predicates.add(criteriaBuilder.like(root.<String>get("IPCTypeNo"), "%" + patVo.getLevel2() + "%"));
                }
                if (StringUtils.isNotEmpty(patVo.getProvince())){
                    predicates.add(criteriaBuilder.like(root.<String>get("applyPersonAddress"), "%" + patVo.getProvince() + "%"));
                }
                if (StringUtils.isNotEmpty(patVo.getCity())){
                    predicates.add(criteriaBuilder.like(root.<String>get("applyPersonAddress"), "%" + patVo.getCity() + "%"));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
                return null;
            }
        };
        return patDao.findAll(specification);
    }


    @Override
    public List analysis_categories_network() {
        return patDao.analysis_categories_network();
    }

    @Override
    public List getTop(int i) {
        return patDao.getTopIPC(i);
    }

    @Override
    public List analysis_categories(List cat) {
        List res = patDao.analysis_categories(cat);
        return res;
    }
    @Override
    public List ana_ipc(PatVo patVo) {
        return patFrontDao.ana_ipc(patVo);
    }

    //导出部分
    @Override
    public String findAreaMin(String province,String city,String level1,String level2) {
        return patDao.findAreaMin(province,city,level1,level2);
    }
    @Override
    public String findAreaMax(String province,String city,String level1,String level2) {
        return patDao.findAreaMax(province,city,level1,level2);
    }
    @Override
    public String findAllAreaMin() {
        return patDao.findAllAreaMin();
    }
    @Override
    public String findAllAreaMax() {
        return patDao.findAllAreaMax();
    }

    @Override
    public List findPartPersonTop3(String province, String city) {
        return patDao.findPartPersonTop3(province,city);
    }

    @Override
    public List findAllPersonTop3() {
        return patDao.findAllPersonTop3();
    }

    @Override
    public List findPartIpcTop(String province, String city) {
        return patDao.findPartIpcTop(province,city);
    }

    @Override
    public List findAllIpcTop() {
        return patDao.findAllIpcTop();
    }
}
