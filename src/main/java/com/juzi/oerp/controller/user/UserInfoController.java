package com.juzi.oerp.controller.user;

import com.juzi.oerp.model.dto.ChangePasswordDTO;
import com.juzi.oerp.model.dto.RetrieveUserDTO;
import com.juzi.oerp.model.vo.response.ResponseVO;
import com.juzi.oerp.service.UserInfoService;
import com.juzi.oerp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    UserService userService;

    @Autowired
    UserInfoService userInfoService;

    /**
     * 普通修改密码
     *
     */
    @PutMapping("/change")
    public ResponseVO<Object> passwordChange(@RequestBody ChangePasswordDTO changePasswordDTO){
        return userInfoService.updatePassword(changePasswordDTO);
    }

    /**
     * 通过手机号验证身份
     */
    @GetMapping("/retrieve/{phoneNumber}")
    public ResponseVO<Object> retrieveUserByPhone(@PathVariable String phoneNumber){
        return userService.getUserByUserPhone(phoneNumber);
    }


    /**
     *找回密码修改密码
     */
    @GetMapping("/retrieve")
    public ResponseVO<Object> retrieveUser(@RequestBody RetrieveUserDTO retrieveUserDTO){
        userInfoService.checkImageCaptcha(retrieveUserDTO.getImageCaptchaId(),retrieveUserDTO.getImageCaptcha());
        userInfoService.checkPhoneCaptcha(retrieveUserDTO.getPhoneNumber(),retrieveUserDTO.getCaptcha());
        return userInfoService.resetPassword(retrieveUserDTO);
    }
}
