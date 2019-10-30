package com.knowledge.service;

import com.knowledge.domain.output.SearchOutput;
import com.knowledge.domain.output.SolrQltOutput;
import com.knowledge.enums.SeachTypeEnum;
import com.knowledge.mapper.jpa.QltQlsxRepository;
import com.knowledge.mapper.jpa.QuestionRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
public class SolrService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QltQlsxRepository qltQlsxRepository;

    private HttpSolrClient getClient(){
        String url = "http://10.32.250.89:8983/solr/corename/";
        HttpSolrClient httpSolrClient = new HttpSolrClient(url);
        httpSolrClient.setParser(new XMLResponseParser()); // 设置响应解析器
        httpSolrClient.setConnectionTimeout(500); // 建立连接的最长时间
        return httpSolrClient;
    }


    public void addQuestion() throws IOException, SolrServerException {
        var client = getClient();
        var questionList = questionRepository.findAll();
        if(questionList == null || questionList.size() <= 0){
            return;
        }
        for (var que : questionList){
            var solrQltOutput = new SolrQltOutput();
            solrQltOutput.setId(que.getId().toString());
            solrQltOutput.setQueId(que.getId().longValue());
            solrQltOutput.setQueTitle(que.getTitle());
            solrQltOutput.setQueDescription(que.getDescription());
            solrQltOutput.setType(SeachTypeEnum.QUE_TYPE.getCode());
            client.addBean(solrQltOutput);
        }
        client.commit();
    }

    public void addByQlt() throws IOException, SolrServerException {
        var client = getClient();

        var qltList = qltQlsxRepository.findAll();
        if(qltList == null || qltList.size() <= 0){
            return;
        }
        for(var qlt : qltList){
            var solrQltOutput = new SolrQltOutput();
            solrQltOutput.setAcpInstitution(qlt.getAcpInstitution());
            solrQltOutput.setApplicableObject(qlt.getApplicableObject());
            solrQltOutput.setContentInvolve(qlt.getContentInvolve());
            solrQltOutput.setDecInstution(qlt.getDecInstitution());
            solrQltOutput.setLeadDept(qlt.getLeadDept());
            solrQltOutput.setQl_name(qlt.getQlName());
            solrQltOutput.setQlt_id(qlt.getQlFullId());
            solrQltOutput.setId(qlt.getQlFullId());
            solrQltOutput.setType(SeachTypeEnum.QIT_TYPE.getCode());
            client.addBean(solrQltOutput);
        }
        client.commit();
    }


    public SearchOutput query(String keywords,Integer page,Integer rows,Long type) throws IOException, SolrServerException {
        var client = getClient();
        SolrQuery solrQuery = new SolrQuery();
        if(keywords == null || "".equals(keywords)){
            keywords = "*";
        }
        solrQuery.setQuery("_text_:"+keywords);
        if(type != null){
            solrQuery.setFilterQueries("type:"+ type);
        }
        boolean isHighlighting = !StringUtils.equals("*", keywords) && StringUtils.isNotEmpty(keywords);
        if (isHighlighting) {
            // 设置高亮
            solrQuery.setHighlight(true); // 开启高亮组件
            solrQuery.addHighlightField("ql_name");// 高亮字段
            solrQuery.addHighlightField("queTitle");
            solrQuery.setHighlightSimplePre("<span style='color:red;'>");// 标记，高亮关键字前缀
            solrQuery.setHighlightSimplePost("</span>");// 后缀
        }

        solrQuery.setStart((Math.max(page,1)-1) * rows);
        solrQuery.setRows(rows);
        QueryResponse queryResponse = client.query(solrQuery);
        List<SolrQltOutput> list = queryResponse.getBeans(SolrQltOutput.class);
        if (isHighlighting) {
            // 将高亮的标题数据写回到数据对象中
            Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
            for (Map.Entry<String, Map<String, List<String>>> highlighting : map.entrySet()) {
                for (SolrQltOutput solrQltOutput : list) {
                    if (!highlighting.getKey().equals(solrQltOutput.getId().toString())) {
                        continue;
                    }
                    if(highlighting.getValue().get("ql_name") != null){
                        solrQltOutput.setQl_name(StringUtils.join(highlighting.getValue().get("ql_name"), ""));
                    }
                    if(highlighting.getValue().get("queTitle") != null){
                        solrQltOutput.setQueTitle(StringUtils.join(highlighting.getValue().get("queTitle"),""));
                    }
                    break;
                }
            }
        }


        SolrDocumentList list1=queryResponse.getResults();
        SearchOutput searchOutput = new SearchOutput();
        searchOutput.setDocs(list);
        searchOutput.setNumFound((int) list1.getNumFound());
        searchOutput.setStart((int) list1.getStart());
        searchOutput.setRows(rows);
        return searchOutput;
    }


}
