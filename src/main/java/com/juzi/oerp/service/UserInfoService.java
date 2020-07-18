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
     *手机号找回用户
     * @param retrieveUserDTO
     */
    ResponseVO<Object> retrieveUser(RetrieveUserDTO retrieveUserDTO);
}
