package com.mmtap.modules.pat.service;

import com.mmtap.modules.pat.model.Patent;

import java.util.List;

public interface PatService {
    public void saveImport(List<Patent> pats);
}
