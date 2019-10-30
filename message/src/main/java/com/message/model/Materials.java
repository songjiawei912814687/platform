package com.message.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "materials")
@Getter
@Setter
public class Materials {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialsNewGenerator")
    @SequenceGenerator(name = "materialsNewGenerator", sequenceName = "materialsNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 50)
    private String qitQlsxId;

    @Column(length = 50)
    private String qlInnerCode;//权力唯一编码

    @Column(nullable = true,length = 64)
    private String materialGuId;

    @Column(nullable = true,length = 15)
    private String format;

    @Column(nullable = true)
    @Lob
    private String name;

    @Column(nullable = true,length = 8)
    private Integer isRq;

    @Column(nullable = true,length = 8)
    private Integer necessity;

    @Column(nullable = true)
    @Lob
    private String bak;

    @Column(nullable = true)
    @Lob
    private String detailRequirement;

    @Column(nullable = true)
    @Lob
    private String necessityDesc;

    @Column(nullable = true,length = 512)
    private String materialRes;

    @Column(nullable = true,length = 2000)
    private String exampleTableFileUrl;

    @Column(nullable = true,length = 512)
    private String exampleTableFileName;


    @Column(nullable = true,length = 2000)
    private String emptyTableFileUrl;

    @Column(nullable = true,length = 512)
    private String emptyTableFileName;

    public Materials() {
    }

    public Materials(String qlInnerCode, String materialGuId, String format, String name, Integer isRq, Integer necessity, String bak, String detailRequirement, String necessityDesc, String materialRes, String exampleTableFileUrl, String exampleTableFileName, String emptyTableFileUrl, String emptyTableFileName) {
        this.qlInnerCode = qlInnerCode;
        this.materialGuId = materialGuId;
        this.format = format;
        this.name = name;
        this.isRq = isRq;
        this.necessity = necessity;
        this.bak = bak;
        this.detailRequirement = detailRequirement;
        this.necessityDesc = necessityDesc;
        this.materialRes = materialRes;
        this.exampleTableFileUrl = exampleTableFileUrl;
        this.exampleTableFileName = exampleTableFileName;
        this.emptyTableFileUrl = emptyTableFileUrl;
        this.emptyTableFileName = emptyTableFileName;
    }
}
