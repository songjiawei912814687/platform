package com.selfservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "acceptAddress")
@Getter
@Setter
public class AcceptAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acceptAddressGenerator")
    @SequenceGenerator(name = "acceptAddressGenerator", sequenceName = "acceptAddress_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 50)
    private String qlsxId;

    @Column(length = 50)
    private String qlInnerCode;

    //受理地点
    @Column(nullable = true)
    @Lob
    private String address;
    //受理地点分类
    @Column(nullable = true)
    @Lob
    private String addressKind;

    //受理接待时间
    @Column(nullable = true)
    @Lob
    private String acceptTimeDesc;

    //联系电话
    @Column(nullable = true)
    @Lob
    private String phone;

    //测绘局地址
    @Column(nullable = true)
    @Lob
    private String UUID;


    public AcceptAddress(String qlsxId, String address, String addressKind, String acceptTimeDesc, String phone, String UUID) {
        this.qlsxId = qlsxId;
        this.address = address;
        this.addressKind = addressKind;
        this.acceptTimeDesc = acceptTimeDesc;
        this.phone = phone;
        this.UUID = UUID;
    }

    public AcceptAddress() {
    }
}
