package com.tmindtech.api.demoserver.test;

import com.tmindtech.api.demoserver.base.annotation.AwesomeParam;
import com.tmindtech.api.demoserver.test.model.LombokJoinModel;
import com.tmindtech.api.demoserver.test.model.LombokModel;
import com.tmindtech.api.demoserver.test.model.Test;
import com.tmindtech.api.demoserver.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/add")
    public Object addTest(@RequestBody Test test) {
        return testService.addTest(test);
    }

    @GetMapping("/get/{id}")
    public Object getTest(@PathVariable("id") Long id) {
        return testService.getTest(id);
    }

    @PostMapping("/add/image")
    public void downloadImage(@AwesomeParam String imageName) {
        testService.downloadImage(imageName);
    }

    public static void main(String[] args) {
//        LombokJoinModel model = new LombokJoinModel();
//        model.lombokModel.setName("张三");
//        model.lombokModel.setAge(4);
//
//        System.out.println(model.lombokModel.getName() + ":" + model.lombokModel.getAge());

        LombokModel lombokModel = new LombokModel();
        lombokModel.setName("李四");
        lombokModel.setAge(5);
        System.out.println(lombokModel.getName() + ":" + lombokModel.getAge());
    }
}
