package com.example.demo.Controller;


import com.example.demo.model.ChatMessage;
import com.example.demo.model.ChatRequestParameter;
import com.example.demo.model.ChatResponseParameter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class RestTestController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/testRestForGet")
    public String testRestForGet(){

        //IP可以换成任意IP
        String url1 = "http://127.0.0.1:8099/getUrl1/100000";
        String url2 = "http://127.0.0.1:8099/getUrl2";

        String result = restTemplate.getForObject(url1, String.class);
        ResponseEntity<String> entity = restTemplate.getForEntity(url1, String.class);
        //参数
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("pwd","123");
        map.put("age",15);
        User user = restTemplate.getForObject(url2+"?name={name}&pwd={pwd}&age={age}", User.class, map);
        ResponseEntity<User> forEntity = restTemplate.getForEntity(url2+"?name={name}&pwd={pwd}&age={age}", User.class, map);
        log.info(result);
        log.info(entity.toString());
        log.info(entity.getBody().toString());
        log.info(user.toString());
        log.info(forEntity.toString());
        log.info(forEntity.getBody().toString());
        return "finish";
    }

    @RequestMapping("/testRestForPost")
    public String testRestForPost(){

        //IP可以换成任意IP
        String url1 = "http://127.0.0.1:8099/postUrl1";
        String url2 = "http://127.0.0.1:8099/postUrl2";


        //post方法必须使用  MultiValueMap 传参  对于单个参数  也可以使用实体但是不能使用单个变量传参  也可以
        MultiValueMap<String,Object> paramMap2 = new LinkedMultiValueMap<>();
        String userName = "prter";
        paramMap2.add("userName",userName);
        paramMap2.add("age",15);

        User u = new User();
        u.setName("王丹丹");
        u.setPwd("123456");
        u.setAge(15);

        User user1 = restTemplate.postForObject(url1, u, User.class);
        ResponseEntity<User> entity1 = restTemplate.postForEntity(url1, u, User.class);


        User user = restTemplate.postForObject(url2, paramMap2, User.class);
        ResponseEntity<User> entity = restTemplate.postForEntity(url2, paramMap2, User.class);

        log.info(user.toString());
        log.info(entity.toString());
        log.info(entity.getBody().toString());
        log.info(user1.toString());
        log.info(entity1.toString());
        log.info(entity1.getBody().toString());
        return "finish";
    }

    //对于请求的方法  @requestBody和@requestParam  混用的情况  则需要另一种请求方式
    @RequestMapping("/testRestForPost1")
    public String testRestForPost1(){

        //IP可以换成任意IP
        String url = "http://127.0.0.1:8099/postUrl3";

        //申明一个请求头
        HttpHeaders headers = new HttpHeaders();
        //application/json
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> map = new HashMap<>();
        map.put("userName","prter");

        User u = new User();
        u.setName("lilei");
        u.setPwd("123");
        u.setAge(15);
        HttpEntity<User> entityParam = new HttpEntity<User>(u,headers);
        User user = restTemplate.postForObject(url+"?userName={userName}", entityParam, User.class,map);
        log.info(user.toString());

        return "finish";
    }


    //Exchange   既可以进行get访问  也可以进行post访问  并且返回值必须用ResponseEntity包装
    @RequestMapping("/testRestForExchangeGet")
    public String testRestForExchangeGet(){
        String url = "http://127.0.0.1:8099/getUrl2";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name","粉红豹");
        paramMap.put("pwd","123");
        paramMap.put("age",15);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //HttpEntity  包装了传参
        HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(null,headers);
        //ResponseEntity 包装返回结果
        ResponseEntity<User> response = restTemplate.exchange(url+"?name={name}&pwd={pwd}&age={age}", HttpMethod.GET,requestEntity,User.class,paramMap);
        log.info(response.toString());
        log.info(response.getBody().toString());
        return "finish";
    }

    //Exchange   既可以进行get访问  也可以进行post访问  并且返回值必须用ResponseEntity包装
    @RequestMapping("/testRestForExchangePost")
    public String testRestForExchangePost(){
        String url = "http://127.0.0.1:8099/postUrl2";
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("userName","粉红豹");
        paramMap.add("age",15);
        //HttpEntity  包装了传参
        HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(paramMap);
        //ResponseEntity 包装返回结果
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.POST,requestEntity,User.class);
        log.info(response.toString());
        log.info(response.getBody().toString());
        return "finish";
    }

    //restTemplate  添加代理  以访问  google网站为例
    @RequestMapping("/proxy")
    public String proxyNoParams(){

        //百度不需要  代理（即翻墙所以可以直接访问）
        String url1 = "http://www.baidu.com";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url1, String.class);
        System.out.println(forEntity.toString());
        String object = restTemplate.getForObject(url1, String.class);
        System.out.println(object);
        String s = restTemplate.postForObject(url1, null, String.class);
        System.out.println(s);
        ResponseEntity<String> entity = restTemplate.postForEntity(url1, null, String.class);
        System.out.println(entity.toString());

        //  谷歌需要代理   谷歌接口  不允许post访问
        String url2 = "http://www.google.com";

        ResponseEntity<String> forEntity2 = restTemplate.getForEntity(url2, String.class);
        System.out.println(forEntity2.toString());
        String object2 = restTemplate.getForObject(url2, String.class);
        System.out.println(object2);


        // 然后我们访问  需要认证的 openai接口  同样是外网
        // 因为 restTemplate  在注入的时候就 使用了代理 所以这里正常写就可以
        // 如果是new的方式则需要  初始化
        // 关于openai的 密钥申请连接参考：https://www.bilibili.com/video/BV1iM4y1D7HW/?spm_id_from=333.337.search-card.all.click&vd_source=97411b9a8288d7869f5363f72b0d7613

        String url3 = "https://api.openai.com/v1/chat/completions";
        //申明一个请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","Bearer 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");
        // 然后进行raw形式的 申请方式
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRequestParameter parameter = new ChatRequestParameter();
        parameter.addMessages(new ChatMessage("user","你好"));
        String valueAsString = null;
        try {
            valueAsString = objectMapper.writeValueAsString(parameter);
        }catch (Exception e){
            e.printStackTrace();
        }
        HttpEntity<String> entityParam = new HttpEntity<>(valueAsString,headers);
        ChatResponseParameter response = restTemplate.postForObject(url3, entityParam, ChatResponseParameter.class);
        System.out.println(response.toString());
        return "finish";
    }




}
