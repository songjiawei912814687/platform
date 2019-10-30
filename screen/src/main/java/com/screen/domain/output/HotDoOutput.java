package com.screen.domain.output;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Young
 * @description: 热门事项办件量对象
 * @date: Created in 16:00 2018/11/20
 * @modified by:
 */
@Getter
@Setter
public class HotDoOutput implements Serializable {


    private String qlFullId;
    private String qlNames;
    private Integer count;
    private BigDecimal point;

}
