package com.qianshou.elasticsearch.client;


import com.qianshou.elasticsearch.pojo.ElasticsearchUser;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface EsApi {
//    @GetMapping("/getNearUser")
//    Page<ElasticsearchUser> getNearUser(@RequestParam("longitude") double longitude, double latitude , String distance , Integer page , Integer size);

    @PostMapping(value = "/addUser",consumes = "application/json")
    Integer saveUser(@RequestBody ElasticsearchUser elasticsearchUser, @RequestParam("longitude") double longitude, @RequestParam("latitude") double latitude);
}
