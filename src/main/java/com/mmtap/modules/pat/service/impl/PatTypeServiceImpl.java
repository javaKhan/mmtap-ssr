package com.mmtap.modules.pat.service.impl;

import com.mmtap.modules.pat.dao.PatTypeDao;
import com.mmtap.modules.pat.service.PatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatTypeServiceImpl implements PatTypeService {
    @Autowired
    private PatTypeDao patTypeDao;
    @Override
    public void saveTypes(List patTypeList) {
        patTypeDao.saveAll(patTypeList);
    }
}
