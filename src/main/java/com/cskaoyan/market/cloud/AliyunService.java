package com.cskaoyan.market.cloud;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.cskaoyan.market.aliyun.AliyunProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.UUID;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/21 10:20
 * @Version 1.0
 */
@Component
public class AliyunService implements CloudService{

    @Autowired
    AliyunProperties aliyunProperties;

    @Override
    public String oss(InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");
        OSS ossClient = new OSSClientBuilder().build(aliyunProperties.getOss().getEndPoint(), aliyunProperties.getAccessKeyId(), aliyunProperties.getAccessKeySecret());
        // 123123312312.png
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + ".png";
        objectMetadata.setContentDisposition("inline;filename=" + fileName);
        PutObjectResult putObjectResult = ossClient.putObject(aliyunProperties.getOss().getBucketName(), fileName, inputStream,objectMetadata);
        System.out.println(putObjectResult);

        ossClient.shutdown();
        // https://{bucketName}.{endPoint}/filename
        String path = "https://" + aliyunProperties.getOss().getBucketName() + "." + aliyunProperties.getOss().getEndPoint() + "/" + fileName;

        return path;
    }

    //关于这个方法，大家后续自行去优化，要么返回值返回一个状态码；要么注解在接口方法上面抛出一个异常
    @Override
    public void sms(String phoneNumber, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunProperties.getAccessKeyId(), aliyunProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", aliyunProperties.getSms().getSignName());
        request.putQueryParameter("TemplateCode", aliyunProperties.getSms().getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        }  catch (ClientException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            throw new RuntimeException(e);
        }

    }
}
