package com.knowledge.domain.output;

import java.util.List;

public class SearchOutput {
    private Integer numFound;
    private Integer start;
    private Integer rows;

    private List<SolrQltOutput> docs;

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getNumFound() {
        return numFound;
    }

    public void setNumFound(Integer numFound) {
        this.numFound = numFound;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public List<SolrQltOutput> getDocs() {
        return docs;
    }

    public void setDocs(List<SolrQltOutput> docs) {
        this.docs = docs;
    }
}
