package com.mmtap.modules.pat.service;

import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.vo.PatVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatService {
    void saveImport(List<Patent> pats);

    Page list(Pageable pageable, PatVo patVo);

    Object annual(PatVo vo);

    Object patentee(PatVo vo);

    Object category(PatVo vo);

    Object network(PatVo vo);
}
