package com.example.demo.Controller;

import com.example.demo.utils.SmallTool;
import com.example.demo.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

//RestTemplate  结合异步进行调试
@RestController
@Slf4j
public class RestAsyncController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("postAsync")
    public String postAsync() throws ExecutionException, InterruptedException {
        //开五个并行的线程访问
        ExecutorService threadPoolExecutor = ThreadPoolUtil.getThreadPoolExecutor();
        List<CompletableFuture<String>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(getStr(threadPoolExecutor));
        }
/*        try {
            Thread.sleep(5000);
            SmallTool.printTimeAndThread("阻塞后");
            Thread.sleep(3000);
            SmallTool.printTimeAndThread("再次阻塞后");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        CompletableFuture.allOf(list.toArray(new CompletableFuture[5]));

        //进行统一处理
        for (CompletableFuture<String> future : list) {
            System.out.println(future.get());
        }
        return "success";
    }

    private CompletableFuture<String> getStr(ExecutorService executor) {
        CompletableFuture<String> resultStrs = CompletableFuture.supplyAsync(() -> {
            ExecutorService threadPoolExecutor = ThreadPoolUtil.getThreadPoolExecutor();
            List<CompletableFuture<String>> list = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                list.add(getStr2(threadPoolExecutor));
            }
            CompletableFuture.allOf(list.toArray(new CompletableFuture[3]));
            String url1 = "http://127.0.0.1:8099/getUrl1/100000";
            SmallTool.printTimeAndThread("一级线程池，进入前");
            String result = restTemplate.getForObject(url1, String.class);
            SmallTool.printTimeAndThread("一级线程池，进入后");

            return result;
        }, executor).handle((outCome, exception) -> {
            if (Objects.nonNull(outCome)) {
                return outCome;
            }
            if (Objects.nonNull(exception)) {
                return "";
            }
            return "";
        });
        return resultStrs;
    }

    private CompletableFuture<String> getStr2(ExecutorService executor) {
        CompletableFuture<String> resultStrs = CompletableFuture.supplyAsync(() -> {
            String url1 = "http://127.0.0.1:8099/getUrl1/100000";
            SmallTool.printTimeAndThread("二级线程池，进入前");
            String result = restTemplate.getForObject(url1, String.class);
            SmallTool.printTimeAndThread("二级线程池，进入后");

            return result;
        }, executor).handle((outCome, exception) -> {
            if (Objects.nonNull(outCome)) {
                return outCome;
            }
            if (Objects.nonNull(exception)) {
                return "";
            }
            return "";
        });
        return resultStrs;
    }

}
