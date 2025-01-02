package org.cheems.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.cheems.constant.MessageConstant;
import org.cheems.dto.UserLoginDTO;
import org.cheems.entity.User;
import org.cheems.exception.LoginFailedException;
import org.cheems.mapper.UserMapper;
import org.cheems.properties.WeChatProperties;
import org.cheems.service.UserService;
import org.cheems.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    private String getOpenid(UserLoginDTO userLoginDTO){
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);

        return jsonObject.getString("openid");

    }

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 通过微信接口 获取当前微信用户的openid
        String openid = getOpenid(userLoginDTO);

        // 异常判断
        if(openid == null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 是否是新用户选择保存与注册
        User user = userMapper.getByOpenId(openid);
        if (user == null) {
            user = User.builder()
                    .openid(openid).createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }
}
