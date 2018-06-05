package com.tmindtech.api.demoserver.test.service;

import com.tmindtech.api.demoserver.test.db.TestMapper;
import com.tmindtech.api.demoserver.test.model.Test;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

    @Autowired
    TestMapper testMapper;

    @Transactional
    public Test addTest(Test test) {
        testMapper.insertSelective(test);
        return test;
    }

    public Test getTest(Long id) {
        return testMapper.selectByPrimaryKey(id);
    }

    public void downloadImage(String imageName) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8888/" + imageName);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            Path path = Paths.get("c:/" + UUID.randomUUID());
            Files.copy(inputStream, path);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
