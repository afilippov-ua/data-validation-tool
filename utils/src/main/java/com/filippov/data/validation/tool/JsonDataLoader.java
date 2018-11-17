package com.filippov.data.validation.tool;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

public class JsonDataLoader {

    @SneakyThrows
    public List<Map<String, Object>> loadData(String resourceName) {
        return new ObjectMapper()
                .readValue(
                        this.getClass().getClassLoader().getResourceAsStream(resourceName),
                        new TypeReference<List<Map<String, Object>>>(){});
    }

    @SneakyThrows
    public <T> T loadData(String resourceName, TypeReference typeReference) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream(resourceName), typeReference);
    }
}
