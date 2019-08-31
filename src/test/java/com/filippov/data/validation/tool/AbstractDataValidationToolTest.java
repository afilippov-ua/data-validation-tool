package com.filippov.data.validation.tool;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {DataValidationToolConfig.class, TestConfig.class, EmbeddedMongoConfiguration.class})
public abstract class AbstractDataValidationToolTest {

}
