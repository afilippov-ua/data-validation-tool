package com.filippov.data.validation.tool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
