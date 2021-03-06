package com.mmtap.modules.pat.service;

import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.vo.PatVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatService {
    void saveImport(List<Patent> pats);

    Page list(Pageable pageable, PatVo patVo);

    List annual(PatVo vo);

    List patentee(PatVo vo);

    List category(PatVo vo);

    List network(PatVo vo);

    List exportExcel(PatVo patVo);

    List analysis_categories_network();

    List getTop(int i);

    List analysis_categories(List cat);

    List ana_ipc(PatVo patVo);


    String findAreaMin(String province,String city,String level1,String level2);
    String findAreaMax(String province,String city,String level1,String level2);
    String findAllAreaMin();
    String findAllAreaMax();

    List findPartPersonTop3(String province, String city);

    List findAllPersonTop3();

    List findPartIpcTop(String province, String city);

    List findAllIpcTop();

    Page findFulu(String province, String city, String level1, String level2);

//    Page findFuluNew(String province, String city, String level1, String level2);
}
