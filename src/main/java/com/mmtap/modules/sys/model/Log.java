package com.mmtap.modules.sys.model;

import com.mmtap.common.spring.data.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/
@Entity
@Table(name = "sys_log")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Log extends AbstractEntity<Log> {

    private String title;

    private String reqUri;

    private String reqMethod;

    private String controller;

    private String methodName;

    private String username;

    private String addressIp;

    private String driverName;

    private String browserName;

    private String exceptionInfo;

    private Long executeUseMillisecond;
}
