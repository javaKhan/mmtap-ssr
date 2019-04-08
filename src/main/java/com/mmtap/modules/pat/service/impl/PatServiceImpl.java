package com.mmtap.modules.pat.service.impl;

import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.dao.PatFrontDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.PatVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
