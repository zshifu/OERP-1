package com.juzi.oerp.service;

import com.juzi.oerp.model.dto.ChangePasswordDTO;
import com.juzi.oerp.model.dto.RetrieveUserDTO;
import com.juzi.oerp.model.po.UserInfoPO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juzi.oerp.model.vo.response.ResponseVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Juzi
 * @since 2020-07-14
 */
public interface UserInfoService extends IService<UserInfoPO> {

    /**
     * 修改用户密码
     * @param changePasswordDTO 用户修改密码
     */
    ResponseVO<Object> updatePassword(ChangePasswordDTO changePasswordDTO);

    /**
     * 检测图形验证码
     * @return
     */
    void checkImageCaptcha(String imageCaptchaId,String imageCaptcha);

    /**
     * 检测短信验证码
     * @return
     */
    void checkPhoneCaptcha(String  phoneNum,String captcha);

    /**
     * 重置密码
     * @param retrieveUserDTO
     * @return
     */
    ResponseVO<Object> resetPassword(RetrieveUserDTO retrieveUserDTO);
}
