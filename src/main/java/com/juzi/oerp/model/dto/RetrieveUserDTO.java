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
     * 检验短信验证码需要的参数
     */
    private CheckSMSCaptchaParamDTO checkSMSCaptchaParamDTO;
}
