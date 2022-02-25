package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class TestController {
    //既可以post  又可以get
    @RequestMapping("/commonUrl")
    public String commonUrl(){
        System.out.println("=============commonUrl=================");
        return "commonUrl";
    }

    //post请求
    @PostMapping("/postUrl1")
    public User postUrl1(@RequestBody User u){
        System.out.println("=============postUrl1=================");
        return u;
    }

    //post请求
    @PostMapping("/postUrl2")
    public User postUrl2(@RequestParam("userName") String userName,@RequestParam("age")Integer age){
        System.out.println("=============postUrl2=================");
        User user = new User();
        user.setName(userName);
        user.setAge(age);
        return user;
    }

    //post请求
    @PostMapping("/postUrl3")
    public User postUrl3(@RequestBody User u, @RequestParam("userName") String userName){
        System.out.println("=============postUrl3================="+u.toString());
        u.setName(userName);
        return u;
    }

    //get请求方式1
    @GetMapping("/getUrl1/{id}")
    public String getUrl(@PathVariable("id")String id){
        System.out.println("=============getUrl=================");
        return "输入的ID：为"+id;
    }

    //get请求方式2
    @GetMapping("/getUrl2")
    public User getUrl2(String name, String pwd, Integer age){
        log.info(name+"===="+pwd+"===="+age);
        User user = new User();
        user.setName(name);
        user.setPwd(pwd);
        user.setAge(age);
        return user;
    }

}

class User{
    private String name;
    private String pwd;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", age=" + age +
                '}';
    }
}
