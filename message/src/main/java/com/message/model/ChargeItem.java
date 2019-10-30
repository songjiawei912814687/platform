package com.message.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "chargeItem")
@Getter
@Setter
public class ChargeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargeItemGenerator")
    @SequenceGenerator(name = "chargeItemGenerator", sequenceName = "chargeItem_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false,length = 50)
    private String qlsxId;

    @Column(length = 50)
    private String qlInnerCode;//权力唯一编码

    @Column(nullable = true,length = 64)
    private String chargeItemGuId;

    //收费项目名称
    @Column(nullable = true)
    @Lob
    private String name;

    //收费标准
    @Column(nullable = true)
    @Lob
    private String basis;
    //减免说明
    @Column(nullable = true)
    @Lob
    private String reductionDesc;

    public ChargeItem() {
    }

    public ChargeItem(String qlInnerCode, String chargeItemGuId, String name, String basis, String reductionDesc) {
        this.qlInnerCode = qlInnerCode;
        this.chargeItemGuId = chargeItemGuId;
        this.name = name;
        this.basis = basis;
        this.reductionDesc = reductionDesc;
    }
}
