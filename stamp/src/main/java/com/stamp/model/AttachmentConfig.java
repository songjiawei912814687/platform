package com.stamp.model;

import com.common.Enum.StatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description:
 * @date: Created in 15:44 2018/9/5
 * @modified by:
 */
@Entity
@DynamicUpdate
@Table(name = "attachment_config")
@Data
public class AttachmentConfig implements Serializable {

    private static final long serialVersionUID = 3256998577030993646L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachmentConfigGenerator")
    @SequenceGenerator(name = "attachmentConfigGenerator", sequenceName = "attachmentConfigNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6,nullable = false)
    private Integer id;

    @ApiModelProperty(value = "配置名称", name = "configName", required = true)
    @Column(length = 55,nullable = false)
    private String configName;

    @ApiModelProperty(value = "附件类型", name = "configType", required = true)
    @Column(length = 3,nullable = false)
    private Integer configType;

    @ApiModelProperty(value = "附件最大值", name = "maxSize")
    @Column(length = 11)
    private Integer maxSize;

    @ApiModelProperty(value = "附件最小值", name = "minSize")
    @Column(length = 6)
    private Integer minSize;

    @ApiModelProperty(value = "允许文件类型", name = "allowFiles")
    @Column(length = 2000)
    private String allowFiles;

    @ApiModelProperty(value = "访问路径前缀", name = "urlPrefix", required = true)
    @Column(length = 105,nullable = false)
    private String urlPrefix;

    @ApiModelProperty(value = "压缩最大宽度", name = "compressMaxWidth")
    @Column(length = 11)
    private Integer compressMaxWidth;

    @ApiModelProperty(value = "描述", name = "description")
    @Column(length = 255)
    private String description;

    @ApiModelProperty(value = "是否有效", name = "state", required = true)
    @Column(length = 1,nullable = false)
    private Integer state= StatusEnum.USE.getCode();

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value = "创建人ID", name = "createdUserId")
    private Integer createdUserId;

    @Column(nullable = false, length = 55)
    @ApiModelProperty(value = "创建人name", name = "createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value = "创建时间", name = "createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value = "最后更新时间", name = "lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = false, length = 6)
    @ApiModelProperty(value = "最后更新人Id", name = "lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false, length = 55)
    @ApiModelProperty(value = "最后更新人name", name = "lastUpdateUserName")
    private String lastUpdateUserName;

    public AttachmentConfig() {
    }
}
