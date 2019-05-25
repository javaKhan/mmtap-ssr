package com.mmtap.modules.pat.vo;

import lombok.Data;

import java.util.List;

@Data
public class DisplayVo {
    private String ipc;
    private int startYear;
    private int endYear;
    private Object data;
}
