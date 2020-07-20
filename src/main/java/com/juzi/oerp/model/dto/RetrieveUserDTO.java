package com.juzi.oerp.model.dto;

import com.juzi.oerp.model.dto.param.CheckImageCaptchaParamDTO;
import com.juzi.oerp.model.dto.param.CheckSMSCaptchaParamDTO;
import lombok.Data;

@Data
public class RetrieveUserDTO {

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 确认密码
     */
    private String confirmPassword;

    /**
     * 验证码 id
     */
    private String imageCaptchaId;

    /**
     * 图形验证码值
     */
    private String imageCaptcha;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 手机验证码值
     */
    private String captcha;
}
