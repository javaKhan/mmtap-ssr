package com.mmtap.modules.pat.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Patent {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    private String pid;// int  primary key identity(1,1),
    private String name ;//varchar(100),
    private String industry ;//int,--行业
    private String applyNo;// varchar(50),--申请号码
    private String tongZu ;//int,--同族
    private String yinZheng;// int ,--引证
    private String beiYin ;// int ,--被引
    private Date applyDate ;//Datetime,--申请日,
    private String publicNo;// varchar(50),--公开(公告)号
    private Date publicDate;// Datetime,--公开(公告)日
    private String IPCTypeNo;// varchar(50) ,--IPC 分类号
    private String applyPerson ;//varchar(50),--申请人
    private String inventor;// varchar(100),--发明人
    private String priorityNumber;// varchar(50),--优先权号
    private String priorityDay ;//varchar(50),--优先权日
    private String applyPersonAddress ;//varchar(100),--申请人地址
    private String applyPersonZip ;//varchar(20),--申请人邮编
    private String legalEffectiveDate ;//varchar(50),--法律状态生效日
    private String legalEffectiveMeaning ;//varchar(500),--法律状态含义
}
