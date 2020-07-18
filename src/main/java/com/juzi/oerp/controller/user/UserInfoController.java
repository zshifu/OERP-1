package com.juzi.oerp.controller.user;

import com.juzi.oerp.model.dto.ChangePasswordDTO;
import com.juzi.oerp.model.dto.RetrieveUserDTO;
import com.juzi.oerp.model.vo.response.ResponseVO;
import com.juzi.oerp.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/info")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;
    /**
     * 密码修改
     *
     */
    @PutMapping("/change")
    public ResponseVO<Object> passwordChange(@RequestBody ChangePasswordDTO changePasswordDTO){
        return userInfoService.updatePassword(changePasswordDTO);
    }


    /**
     * 通过手机号找回用户
     */
    @GetMapping("/retrieve/user")
    public ResponseVO<Object> retrieveUser(@RequestBody RetrieveUserDTO retrieveUserDTO){
        return userInfoService.retrieveUser(retrieveUserDTO);
    }

}
