package com.framework.oauth2.client.controller;

import com.alibaba.fastjson.JSONObject;
import com.framework.oauth2.client.util.URLEncodedUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;


/**
 * @author XiongFeiYang
 * @description GetHub授权测试
 * @createTime 2019-10-18 17:26
 **/
@Api(tags = "GetHub授权测试", value = "/getHub/oauth")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/github/oauth")
public class GitHubAuthController {
    /**
     * GitHub授权 获取code
     */
    private final static String GITHUB_OAUTH_URL = "https://github.com/login/oauth/authorize";
    /**
     * GitHub授权 获取access_token
     */
    private final static String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    /**
     * GitHub授权 获取user_info
     */
    private final static String USER_INFO_URL = "https://api.github.com/user";
    /**
     * GitHub授权 回调后台地址
     */
    private final static String REDIRECT_URI = "http://localhost:8080/github/oauth/back";
    /**
     * GitHub授权 client_id
     */
    private final static String CLIENT_ID = "6d5d0e334a3e114a2597";
    /**
     * GitHub授权 client_secret
     */
    private final static String CLIENT_SECRET = "2ac23d72dd0841fec1d9af93bf40b2f091734354";

    private final RestTemplate restTemplate;

    @GetMapping("/login")
    public void gitHubLogin(HttpServletResponse response) throws IOException {
        String state = "123456";
        Map<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("state", state);
        paramsMap.put("scope", "user:email");
        paramsMap.put("client_id", CLIENT_ID);
        paramsMap.put("response_type", "code");
        paramsMap.put("redirect_uri", URLEncodedUtils.urlEncode(REDIRECT_URI));
        String urlParams = URLEncodedUtils.toUrlParams(paramsMap);
        response.sendRedirect(GITHUB_OAUTH_URL + "?" + urlParams);
    }

    @GetMapping("/back")
    public String oauthBack(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (Strings.isNullOrEmpty(code)) {
            return "授权码为空！";
        }
        return getAccessToken(code);
    }

    /**
     * 获取access_token
     *
     * @param code 授权码
     * @return String
     */
    private String getAccessToken(String code) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 设置请求参数
        Map<String, String> paramsMap = Maps.newHashMap();
        paramsMap.put("code", code);
        paramsMap.put("client_id", CLIENT_ID);
        paramsMap.put("client_secret", CLIENT_SECRET);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(paramsMap, headers);
        // 发送请求
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(ACCESS_TOKEN_URL, entity, JSONObject.class);
        if (HttpStatus.OK.value() != responseEntity.getStatusCodeValue()) {
            return "获取ACCESS_TOKEN：请求响应错误！";
        }
        // 获取access_token
        JSONObject exchangeBody = responseEntity.getBody();
        if (Objects.isNull(exchangeBody) || exchangeBody.isEmpty()) {
            return "获取ACCESS_TOKEN：响应数据为空！";
        }
        log.info("获取ACCESS_TOKEN响应数据：" + exchangeBody.toJSONString());
        String accessToken = exchangeBody.getString("access_token");
        if (Strings.isNullOrEmpty(accessToken)) {
            return "获取ACCESS_TOKEN错误：access_token为空！";
        }
        return getUserInfo(accessToken);
    }

    /**
     * 获取用户信息
     *
     * @param accessToken 令牌
     * @return String
     */
    private String getUserInfo(String accessToken) {
        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(USER_INFO_URL, HttpMethod.GET, entity, JSONObject.class);
        if (HttpStatus.OK.value() != responseEntity.getStatusCodeValue()) {
            return "获取USER_INFO：请求响应错误！";
        }
        JSONObject exchangeBody = responseEntity.getBody();
        if (Objects.isNull(exchangeBody) || exchangeBody.isEmpty()) {
            return "获取USER_INFO：响应数据为空！";
        }
        log.info("获取USER_INFO响应数据：" + exchangeBody.toJSONString());
        String userName = exchangeBody.getString("name");
        if (Strings.isNullOrEmpty(userName)) {
            return "获取USER_INFO错误：用户名称为空！";
        }
        log.info("授权成功：" + userName);
        return "success";
    }
}
