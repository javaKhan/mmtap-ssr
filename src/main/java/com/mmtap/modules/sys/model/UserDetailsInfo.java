package com.mmtap.modules.sys.model;

import lombok.Data;

/**
 * @author mmtap.com
 * @date 2019/1/15
 **/
@Data
public class UserDetailsInfo {

    private Gender gender;

    private String nickname;

    private String avatar;

    private String email;

    private String phone;

    enum Gender {
        /**
         * 男
         */
        MAN,
        /**
         * 女
         */
        WO_MAN
    }
}
