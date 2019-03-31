package com.mmtap.modules.sys.model;

import com.mmtap.common.spring.data.AbstractEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mmtap.com
 * @date 2019/1/9
 **/
@Entity
@Table(name = "sys_user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractEntity<User> implements UserDetails {

    @Column(unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private UserStatus status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Role> roleSet;

    @Embedded
    private UserDetailsInfo details;

    {
        status = UserStatus.NORMAL;
        details = new UserDetailsInfo();
    }

    public enum UserStatus {
        /**
         * 正常
         */
        NORMAL,
        /**
         * 异常
         */
        ABNORMAL,
        /**
         * 冻结
         */
        FROZEN
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(this.roleSet);
        grantedAuthorities.addAll(this.roleSet.stream().map(Role::getMenuSet).flatMap(Set::stream).collect(Collectors.toList()));
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !UserStatus.FROZEN.equals(this.status);
    }

    public static Specification<User> specification(final User user) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotEmpty(user.getUsername())) {
                //根据登录名称模糊查询
                predicates.add(cb.like(root.get("username"), "%" + user.getUsername() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
