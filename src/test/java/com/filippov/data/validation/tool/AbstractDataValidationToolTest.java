package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.config.DataValidationToolConfig;
import com.filippov.data.validation.tool.config.EmbeddedMongoConfiguration;
import com.filippov.data.validation.tool.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {DataValidationToolConfig.class, TestConfig.class, EmbeddedMongoConfiguration.class})
public abstract class AbstractDataValidationToolTest {

}
