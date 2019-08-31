package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.config.DataValidationToolConfig;
import com.filippov.data.validation.tool.config.EmbeddedMongoConfiguration;
import com.filippov.data.validation.tool.config.TestConfig;
import com.filippov.data.validation.tool.service.DefaultDatasourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {DatasourceController.class, DefaultDatasourceService.class, EmbeddedMongoConfiguration.class, DataValidationToolConfig.class, TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestClass {

    @Resource
    TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    private int port;

    @Test
    public void testRetrieveStudentCourse() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/datasources"), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
