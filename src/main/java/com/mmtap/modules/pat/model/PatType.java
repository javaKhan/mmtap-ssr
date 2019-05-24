package com.mmtap.modules.pat.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "pat_type")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class PatType {

    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(name = "ptid")
    private String ptid;
    @Column(name = "pat")
    private String pat;
    @Column(name = "ipc_type")
    private String ipcType;
}
