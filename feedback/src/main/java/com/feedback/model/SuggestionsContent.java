package com.feedback.model;//package com.feedback.model;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import org.hibernate.annotations.DynamicUpdate;
//import org.hibernate.validator.constraints.Range;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Date;
//
//@DynamicUpdate
//@Entity
//@Table(name = "suggestions_content")
//@ApiModel(value = "SuggestionsContent投诉内容", description = "投诉内容")
//public class SuggestionsContent implements Serializable {
//    @Id
//    @Column(length = 6)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suggestionsContentGenerator")
//    @SequenceGenerator(name = "suggestionsContentGenerator", sequenceName = "suggestionsContent_sequence",allocationSize = 1,initialValue = 1)
//    @ApiModelProperty(value="投诉建议内容id,后台生成无法修改",name="id")
//    private Integer id;
//
//    @Column(nullable = false, length = 6)
//    @ApiModelProperty(value="投诉建议Id",name="suggestionId")
//    private Integer suggestionId;
//
//    @Column(length = 255)
//    @ApiModelProperty(value="投诉内容,长度最大255",name="description")
//    private String suggesstionContent;
//
//    @Range(min=0,max = 1,message = "是否删除只能为0或1")
//    @Column(nullable = false,length = 1)
//    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
//    private Integer amputated;
//
//    @Column(length = 6)
//    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
//    private Integer createdUserId;
//
//    @Column(length = 55)
//    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
//    private String createdUserName;
//
//    @Column(nullable = false)
//    @ApiModelProperty(value="投诉时间",name="createdDateTime")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date createdDateTime;
//
//    @Column(nullable = false)
//    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date lastUpdateDateTime;
//
//    @Column(length = 6)
//    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
//    private Integer lastUpdateUserId;
//
//    @Column(length = 55)
//    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
//    private String lastUpdateUserName;
//
//
//
//
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getSuggestionId() {
//        return suggestionId;
//    }
//
//    public void setSuggestionId(Integer suggestionId) {
//        this.suggestionId = suggestionId;
//    }
//
//    public String getSuggesstionContent() {
//        return suggesstionContent;
//    }
//
//    public void setSuggesstionContent(String suggesstionContent) {
//        this.suggesstionContent = suggesstionContent;
//    }
//
//    public Integer getAmputated() {
//        return amputated;
//    }
//
//    public void setAmputated(Integer amputated) {
//        this.amputated = amputated;
//    }
//
//    public Integer getCreatedUserId() {
//        return createdUserId;
//    }
//
//    public void setCreatedUserId(Integer createdUserId) {
//        this.createdUserId = createdUserId;
//    }
//
//    public String getCreatedUserName() {
//        return createdUserName;
//    }
//
//    public void setCreatedUserName(String createdUserName) {
//        this.createdUserName = createdUserName;
//    }
//
//    public Date getCreatedDateTime() {
//        return createdDateTime;
//    }
//
//    public void setCreatedDateTime(Date createdDateTime) {
//        this.createdDateTime = createdDateTime;
//    }
//
//    public Date getLastUpdateDateTime() {
//        return lastUpdateDateTime;
//    }
//
//    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
//        this.lastUpdateDateTime = lastUpdateDateTime;
//    }
//
//    public Integer getLastUpdateUserId() {
//        return lastUpdateUserId;
//    }
//
//    public void setLastUpdateUserId(Integer lastUpdateUserId) {
//        this.lastUpdateUserId = lastUpdateUserId;
//    }
//
//    public String getLastUpdateUserName() {
//        return lastUpdateUserName;
//    }
//
//    public void setLastUpdateUserName(String lastUpdateUserName) {
//        this.lastUpdateUserName = lastUpdateUserName;
//    }
//}
