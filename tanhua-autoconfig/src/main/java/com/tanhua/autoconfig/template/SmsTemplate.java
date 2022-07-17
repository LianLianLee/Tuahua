package com.tanhua.autoconfig.template;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.tanhua.autoconfig.properties.SmsProperties;

/**
 * @author Administrator
 */
public class SmsTemplate {

    private SmsProperties smsProperties;

    public SmsTemplate(SmsProperties smsProperties) {
        this.smsProperties = smsProperties;
    }

    /**
     * 发送验证码短信
     *
     * @param mobile
     * @param code
     */
    public void sendSms(String mobile, String code) {
        try {
            Config config = new Config()
                    .setAccessKeyId(smsProperties.getAccessKey())
                    .setAccessKeySecret(smsProperties.getSecret());
            // 访问的域名
            config.endpoint = "dysmsapi.aliyuncs.com";
            Client client = new Client(config);

            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(mobile)
                    .setSignName(smsProperties.getSignName())
                    .setTemplateCode(smsProperties.getTemplateCode())
                    .setTemplateParam("{\"code\":\"" + code + "\"}");

            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse response = client.sendSms(sendSmsRequest);
            if ("OK".equals(response.getBody().getCode())) {
                System.out.println("验证码发送成功");
            }
            System.out.println(response.getBody().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
