package com.mmtap.modules.pat.service.impl;

import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import com.mmtap.modules.pat.vo.PatVo;
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
                if (null!=patVo.getCity()){

                }
                return null;
            }
        };

//        Specification<PatVo> specification = new Specification<PatVo>() {
//            @Override
//            public Predicate toPredicate(Root<PatVo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return null;
//            }
//
//            @Override
//            public Specification<PatVo> and(Specification<PatVo> other) {
//                return null;
//            }
//
//            @Override
//            public Specification<PatVo> or(Specification<PatVo> other) {
//                return null;
//            }
//        };
        Page page = patDao.findAll(specification,pageable);
        return page;
    }
}
