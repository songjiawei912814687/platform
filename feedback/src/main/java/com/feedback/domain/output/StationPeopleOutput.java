package com.feedback.domain.output;


import com.feedback.model.StationPeople;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationPeopleOutput extends StationPeople {

    private Integer takeCount;

    private Integer compCount;

    private String orgName;

    private Integer orgId;
}
