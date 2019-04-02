//package com.mmtap.modules.pat.dao;
//
//import com.mmtap.modules.pat.model.Patent;
//import com.mmtap.modules.pat.vo.PatVo;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.data.jpa.domain.Specification;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//public class PatSpecification {
//
//    public static Specification<Patent> where(PatVo patVo){
//        return  new Specification<Patent>() {
//            @Override
//            public Predicate toPredicate(Root<Patent> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                Predicate finalConditions = criteriaBuilder.conjunction();
//                //1 类型处理
//                if(StringUtils.isNotEmpty(patVo.getLevel1())){
//                    Predicate datePredicate = null;
//
//                    finalConditions = criteriaBuilder.and(finalConditions, datePredicate);
//
//                }
//                if(StringUtils.isNotEmpty(patVo.getLevel2())){
//                    Predicate datePredicate = null;
//
//                    finalConditions = criteriaBuilder.and(finalConditions, datePredicate);
//
//                }
//                //2 地区处理
//                if(StringUtils.isNotEmpty(patVo.getProvince())){
//                    Predicate datePredicate = null;
//
//                    finalConditions = criteriaBuilder.and(finalConditions, datePredicate);
//
//                }
//                if(StringUtils.isNotEmpty(patVo.getCity())){
//                    Predicate datePredicate = null;
//
//                    finalConditions = criteriaBuilder.and(finalConditions, datePredicate);
//
//                }
//                //3 时间处理
//                if(StringUtils.isNotEmpty(patVo.getStartYear())){
//                    Predicate datePredicate = null;
//
//                    finalConditions = criteriaBuilder.and(finalConditions, datePredicate);
//                }
//                if(StringUtils.isNotEmpty(patVo.getEndYear())){
//                    Predicate datePredicate = null;
//
//                    finalConditions = criteriaBuilder.and(finalConditions, datePredicate);
//                }
//
//                return query.where(finalConditions).getRestriction();
//            }
//        };
//    }
//}
