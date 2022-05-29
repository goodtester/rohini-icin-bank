package com.rohini.icinbank.controller;

import com.rohini.icinbank.domain.common.UsernamePassword;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StatusController {
    @GetMapping(value = "/welcome", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String register (@RequestBody UsernamePassword req) {
        return "<html>\n" + "<header><title>Welcome</title></header>\n" +
                "<body>\n" + "Hello world\n" + "</body>\n" + "</html>";
    }
}
