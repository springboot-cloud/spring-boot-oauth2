package com.framework.oauth2.client.vo.api;

/**
 * Api常量定义类
 *
 * @author Admin
 * @version 1.0.0
 */
public class ApiConst {

    /**
     * 返回CODE码定义, 按照httpstatus的code码定义, 系统自定义的为四位数
     *
     * @version 1.0.0
     */
    public static enum Code {

        /**
         * 成功返回码
         */
        CODE_SUCCESS(200),

        /**
         * 返回内容为空
         */
        CODE_CONTENT_EMPTY(204),

        /**
         * 访问被拒绝
         */
        CODE_REJECT(403),

        /**
         * 无权限访问
         */
        CODE_NO_AUTH(401),

        /**
         * 请求方法错误
         */
        CODE_NOT_EXIST(404),

        /**
         * 请求版本错误
         */
        CODE_VERSION_ERROR(406),

        /**
         * 获取锁错误
         */
        CODE_GET_LOCK_ERROR(423),

        /**
         * 请求参数错误
         */
        CODE_PARAM_ERROR(4001),

        /**
         * 服务器错误
         */
        CODE_SERVER_ERROR(500),

        /**
         * 其他业务码定义
         */
        CODE_COMMON_ERROR(5001),

        /**
         * 自动完成登录状态
         */
        CODE_LOGIN_RETRY(5002),

        /**
         * 用户过期或处在无登录状态
         */
        CODE_NO_SESSION(5003),

        /**
         * 请求第三方错误
         */
        CODE_THIRDPARTY_ERROR(5004);

        private Code(int intCode) {
            this.intCode = intCode;
        }

        private int intCode;

        public int code() {
            return intCode;
        }

    }
}
