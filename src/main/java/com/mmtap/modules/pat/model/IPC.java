package com.mmtap.modules.pat.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ipc_type")
@Data
public class IPC {
    private int level;
    private String name;
    @Id
    private String code;
    private String pc;
}
