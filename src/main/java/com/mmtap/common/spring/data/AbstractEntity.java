package com.mmtap.common.spring.data;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;


/**
 * @author mmtap.com
 * @date 2019/1/8
 **/
@MappedSuperclass
@Getter
@Setter(AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class AbstractEntity<E extends AbstractEntity> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @LastModifiedDate
    private Date lastModifiedDate;
}
