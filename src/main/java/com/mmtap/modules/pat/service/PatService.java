package com.mmtap.modules.pat.service;

import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.vo.PatVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatService {
    public void saveImport(List<Patent> pats);

    Page list(Pageable pageable, PatVo patVo);
}
