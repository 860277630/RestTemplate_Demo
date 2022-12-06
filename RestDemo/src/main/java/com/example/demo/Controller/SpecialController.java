package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import java.io.InputStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.InputStream;

/**
 * @program: demo
 * @description:
 * @author: wjl
 * @create: 2022-12-06 20:57
 **/
@RestController
@Slf4j
public class SpecialController {

    @RequestMapping("getWord")
    public ResponseEntity<InputStreamResource> getWord(@RequestParam("date") String dateStr){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition",
                "attachment; filename=" + dateStr+".doc");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(this.getWord_1(dateStr).getBody());
    }

    public ResponseEntity<InputStreamResource> getWord_1( String dateStr) {

        CloseableHttpResponse response = postWithParams("127.0.0.1", "{\"date_now\":\""+dateStr+"\"}");
        if(response.getStatusLine().getStatusCode() == HttpStatus.OK.value()){
            //如果成功  就  将  结果存入服务器
            try {
                InputStream content = response.getEntity().getContent();
                return new ResponseEntity<>(new InputStreamResource(content),HttpStatus.OK);
            }catch (Exception e){
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static CloseableHttpResponse postWithParams(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpEntity responseEntity = null;
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(3600000).setConnectionRequestTimeout(3600000)
                    .setSocketTimeout(3600000).build();
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Connection", "close");
            httpPost.setConfig(requestConfig);

            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            System.out.println("响应状态为:" + response.getStatusLine());
            responseEntity = response.getEntity();
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                //System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
            //resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
