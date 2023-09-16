package com.gundolog.api.controller;

import com.gundolog.api.request.PostCreate;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PostController {

    @PostMapping("/posts")
    public String post(@RequestBody @Valid PostCreate params) {
        log.info("title : {}, content: {}", params.title, params.content);
        return "Hello World";
    }
}

// ModelAttribute와 RequestBody의 차이는?