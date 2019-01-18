package com.filippov.data.validation.tool.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("datasources")
public class DatasourceController {

    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String init() {
        return "hello world";
    }
}
