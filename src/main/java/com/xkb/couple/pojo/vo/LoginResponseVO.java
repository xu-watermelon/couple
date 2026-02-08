package com.xkb.couple.pojo.vo;

import com.xkb.couple.pojo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseVO {
    private String token;
    private UserVO userVO;
}