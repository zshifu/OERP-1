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
import sun.plugin.cache.OldCacheEntry;

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
        if (StringUtils.isEmpty(changePasswordDTO.getOldPassword())|| StringUtils.isEmpty(changePasswordDTO.getNewPassword()) || StringUtils.isEmpty(changePasswordDTO.getConfirmPassword())) {
            responseVO = new ResponseVO<>(60001, "密码框输入为空");
            return responseVO;
        }
        //传过来的密码加密
        String dtoNewPassword= SecureUtil.md5(changePasswordDTO.getNewPassword());
        String dtoOldPassword=SecureUtil.md5(changePasswordDTO.getOldPassword());
        if (!oldPassword.equals(dtoOldPassword)) {
            responseVO = new ResponseVO<>(60002, "旧密码输入错误");
            return responseVO;
        }
        String dtoConfirmPassword=SecureUtil.md5(changePasswordDTO.getConfirmPassword());
        if (!dtoNewPassword.equals(dtoConfirmPassword)) {
            responseVO = new ResponseVO<>(60003, "两次密码输入不一致");
            return responseVO;
        } else {
            userPO.setPassword(dtoNewPassword);
            userPO.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(userPO);
            responseVO = new ResponseVO<>(userPO);
        }
        return responseVO;
    }


    public ResponseVO<Object> resetPassword(RetrieveUserDTO retrieveUserDTO){
        //检测手机号是否注册
        String reallySMSCaptcha = smsCaptchaCache.get(retrieveUserDTO.getCheckSMSCaptchaParamDTO().getPhoneNumber(), String.class);
        if (StringUtils.isEmpty(reallySMSCaptcha) || CacheConstants.CAPTCHA_CHECKED.equals(reallySMSCaptcha)) {
            throw new AuthenticationException(40006);
        }

        String newPassword=retrieveUserDTO.getNewPassword();
        String confirmPassword=retrieveUserDTO.getConfirmPassword();
        ResponseVO<Object> responseVO=null;
        //判断密码输入是否为空
        if (StringUtils.isEmpty(newPassword) || StringUtils.isEmpty(confirmPassword)) {
            responseVO = new ResponseVO<>(60001, "密码框输入为空");
            return responseVO;
        }
        if (!newPassword.equals(confirmPassword)) {
            responseVO = new ResponseVO<>(60003, "两次密码输入不一致");
            return responseVO;
        } else {
            UserPO userPO=userMapper.selectOne(new LambdaQueryWrapper<UserPO>().eq(UserPO::getPhoneNumber,retrieveUserDTO.getCheckSMSCaptchaParamDTO().getPhoneNumber()));
            userPO.setPassword(SecureUtil.md5(newPassword));
            userPO.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(userPO);
            responseVO = new ResponseVO<>(userPO);
        }

        // 删除短信验证码缓存
        smsCaptchaCache.evict(retrieveUserDTO.getCheckSMSCaptchaParamDTO().getPhoneNumber());
        return responseVO;
    }
}
