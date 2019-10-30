package com.knowledge.controller;

import com.common.response.ResponseResult;
import com.knowledge.domain.output.SearchOutput;
import com.knowledge.service.SolrService;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("slor")
public class SolrController {

    @Autowired
    private SolrService solrService;

    @GetMapping("add")
    public ResponseResult add() throws IOException, SolrServerException {
        solrService.addByQlt();
        solrService.addQuestion();
        return ResponseResult.success();
    }

    @GetMapping("query")
    public ResponseResult query(String keywords,Integer page,Integer rows,Long type) throws IOException, SolrServerException {
        var list = solrService.query(keywords,page,rows,type);
        return ResponseResult.success(list);
    }
}
