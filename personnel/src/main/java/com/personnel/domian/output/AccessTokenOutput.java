package com.personnel.domian.output;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-03  12:32
 * @modified by:
 */
public class AccessTokenOutput {

    //返回标识
    private String rSign;
    //授权秘钥
    private String accessToken;
    //有效时间
    private Integer expiresIn;

    public String getrSign() {
        return rSign;
    }

    public void setrSign(String rSign) {
        this.rSign = rSign;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }
}
