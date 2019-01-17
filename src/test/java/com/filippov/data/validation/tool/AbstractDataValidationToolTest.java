package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.config.EmbeddedMongoConfiguration;
import com.filippov.data.validation.tool.config.TestConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfiguration.class, EmbeddedMongoConfiguration.class})
public abstract class AbstractDataValidationToolTest {

}
