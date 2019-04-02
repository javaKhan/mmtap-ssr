package com.mmtap.modules.pat.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IPCvo {
    String code;
    String name;
    List child = new ArrayList<IPCvo>();
}
