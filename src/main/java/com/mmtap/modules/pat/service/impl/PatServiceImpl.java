package com.mmtap.modules.pat.service.impl;

import com.mmtap.modules.pat.dao.PatDao;
import com.mmtap.modules.pat.model.Patent;
import com.mmtap.modules.pat.service.PatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatServiceImpl implements PatService {
    @Autowired
    private PatDao patDao;

    @Transactional
    public void saveImport(List<Patent> pats){
        patDao.saveAll(pats);
    }
}
