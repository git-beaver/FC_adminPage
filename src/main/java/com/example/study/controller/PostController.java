package com.example.study.controller;

import com.example.study.model.SearchParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class PostController {

    //HTML <Form>, ajax 검색에 주로 사용 = 검색 파라미터가 많다는 것
    //http post body에 data를 집어 넣어 보냄
    //json, xml, multipart-form / text-plain

    //@RequestMapping(method = RequestMethod.POST, path = "postMethod") > 아랫줄과 동일
    @PostMapping(value = "/postMethod"/*, produces = {"application-json"}*/)
    public SearchParam postMethod(@RequestBody SearchParam searchParam) {

        return searchParam;
    }

    @PutMapping("putMethod")
    public void put() {

    }

    @PatchMapping("patchMethod")
    public void patch() {

    }
}
