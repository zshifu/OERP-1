package com.juzi.oerp.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juzi.oerp.common.constant.CacheConstants;
import com.juzi.oerp.common.exception.AuthenticationException;
import com.juzi.oerp.mapper.UserInfoMapper;
import com.juzi.oerp.mapper.UserMapper;
import com.juzi.oerp.model.dto.ChangePasswordDTO;
import com.juzi.oerp.model.dto.RetrieveUserDTO;
import com.juzi.oerp.model.po.UserInfoPO;
import com.juzi.oerp.model.po.UserPO;
import com.juzi.oerp.model.vo.response.ResponseVO;
import com.juzi.oerp.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Juzi
 * @since 2020-07-14
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoPO> implements UserInfoService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private Cache smsCaptchaCache;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseVO<Object> updatePassword(ChangePasswordDTO changePasswordDTO) {
        UserPO userPO = new UserPO();
        userPO = userMapper.selectById(changePasswordDTO.getId());
        ResponseVO<Object> responseVO = null;
        String oldPassword = userPO.getPassword();
        //判断密码输入是否为空
        if (changePasswordDTO.getOldPassword().equals("") && changePasswordDTO.getNewPassword().equals("") && changePasswordDTO.getConfirmPassword().equals("")) {
            responseVO = new ResponseVO<>(6001, "密码输入为空");
            return responseVO;
        }
        //传过来的密码加密
        String dtoNewPassword= SecureUtil.md5(changePasswordDTO.getNewPassword());
        String dtoOldPassword=SecureUtil.md5(changePasswordDTO.getOldPassword());
        if (!oldPassword.equals(dtoOldPassword)) {
            responseVO = new ResponseVO<>(6002, "旧密码输入错误");
            return responseVO;
        }
        String dtoConfirmPassword=SecureUtil.md5(changePasswordDTO.getConfirmPassword());
        if (!dtoNewPassword.equals(dtoConfirmPassword)) {
            responseVO = new ResponseVO<>(6003, "两次密码输入不一致");
            return responseVO;
        } else {
            userPO.setPassword(dtoNewPassword);
            userPO.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(userPO);
            responseVO = new ResponseVO<>(6000, "密码修改成功");
        }
        return responseVO;
    }

    /**
     * 通过手机号找回用户
     *
     * @param retrieveUserDTO
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseVO<Object> retrieveUser(RetrieveUserDTO retrieveUserDTO) {
        String reallySMSCaptcha = smsCaptchaCache.get(retrieveUserDTO.getPhoneNumber(), String.class);
        if (StringUtils.isEmpty(reallySMSCaptcha) || CacheConstants.CAPTCHA_CHECKED.equals(reallySMSCaptcha)) {
            throw new AuthenticationException(40006);
        }
        // 删除短信验证码缓存
        smsCaptchaCache.evict(retrieveUserDTO.getPhoneNumber());
        ResponseVO<Object> responseVO=null;
        if (reallySMSCaptcha.equals(retrieveUserDTO.getSmsCaptcha())) {
            UserPO userPO = userMapper.selectOne(new LambdaQueryWrapper<UserPO>().eq(UserPO::getPhoneNumber, retrieveUserDTO.getPhoneNumber()));
            responseVO = new ResponseVO<>(userPO);
        }else {
            responseVO = new ResponseVO<>(7001, "验证码错误");
        }
        return responseVO;
    }
}
