package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.TestDataValidationToolApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestDataValidationToolApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestTemplateClass {

    @Resource
    TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    public void getWorkspacesTest() {
        ResponseEntity<String> response = restTemplate.getForEntity(getUrl("/workspaces"), String.class);
        System.out.println("BODY: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[]");
    }

    private String getUrl(String uri) {
        return "http://localhost:" + port + uri;
    }
}
