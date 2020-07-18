package com.juzi.oerp.model.dto;

import lombok.Data;

@Data
public class RetrieveUserDTO {

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 短信验证码
     */
    private String smsCaptcha;
}
