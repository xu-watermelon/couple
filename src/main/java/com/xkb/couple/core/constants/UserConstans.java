package com.xkb.couple.core.constants;

import lombok.Getter;

/**
 * 用户常量类
 * @author xuwatermelon
 * @date 2026/02/010
 */
public class UserConstans {


    public enum UserEnum {
        /**
         * 女
         */
        FEMALE(0),
        /**
         * 男
         */
        MALE(1);
        @Getter
        private final Integer value;

        UserEnum(int value) {
            this.value = value;
        }
    }

}
