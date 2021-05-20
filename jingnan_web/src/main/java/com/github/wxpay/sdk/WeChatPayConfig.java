package com.github.wxpay.sdk;

import java.io.InputStream;

/**
 * 微信 支付 Java配置com.github.wxpay.sdk.WeChatPayConfig
 *
 */
public class WeChatPayConfig extends WXPayConfig {


    /**
     * 微信公众账号或开放平台APP的唯一标识
     *
     */
    @Override
    public String getAppID() {
        return "*******************";
    }
    /**
     *商户号
     *
     * 申请商户号：https://developers.weixin.qq.com/community/develop/doc/000ec8fd22c738e9ff5abf5d15d800?_at=1620978901335
     */
    @Override
    public String getMchID() {
        return "*******************";
    }
    /**
     * 商户密钥
     */
    @Override
    public String getKey() {
        return "*******************";
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }
    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("api.mch.weixin.qq.com",true);
            }
        };
    }

}
