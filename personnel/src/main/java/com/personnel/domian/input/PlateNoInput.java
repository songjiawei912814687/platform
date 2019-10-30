package com.personnel.domian.input;

import java.util.List;

public class PlateNoInput {

    private Integer id;

    private String plateNo;

    private List<String> plateNoList;

    public List<String> getPlateNoList() {
        return plateNoList;
    }

    public void setPlateNoList(List<String> plateNoList) {
        this.plateNoList = plateNoList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }
}
