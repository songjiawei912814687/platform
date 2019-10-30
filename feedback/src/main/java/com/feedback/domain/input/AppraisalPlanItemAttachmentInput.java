package com.feedback.domain.input;

import com.feedback.model.PlanItemAddition;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-08-28  11:27
 */
@Getter
@Setter
public class AppraisalPlanItemAttachmentInput {

    private Integer planItemId;

    private List<PlanItemAddition> list;
}
