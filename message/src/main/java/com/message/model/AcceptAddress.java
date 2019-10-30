package com.message.model;

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
    private String qlInnerCode;//权力唯一编码

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

    public AcceptAddress() {
    }

    public AcceptAddress(String qlInnerCode, String address, String addressKind, String acceptTimeDesc, String phone, String UUID) {
        this.qlInnerCode = qlInnerCode;
        this.address = address;
        this.addressKind = addressKind;
        this.acceptTimeDesc = acceptTimeDesc;
        this.phone = phone;
        this.UUID = UUID;
    }
}
