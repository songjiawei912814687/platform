package com.selfservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Materials {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialsNewGenerator")
    @SequenceGenerator(name = "materialsNewGenerator", sequenceName = "materialsNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 50)
    private String qitQlsxId;

    @Column(length = 50)
    private String qlInnerCode;

    @Column(nullable = true,length = 64)
    private String materialGuId;

    @Column(nullable = true,length = 2)
    private Integer format;

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

}
