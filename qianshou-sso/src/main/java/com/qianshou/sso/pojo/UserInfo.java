package com.qianshou.sso.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author xiehao
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user_info")
@ApiModel(value="UserInfo对象", description="用户信息表")
public class UserInfo implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String logo;

    @ApiModelProperty(value = "用户标签：多个用逗号分隔")
    private String tags;

    @ApiModelProperty(value = "性别，1-男，2-女，3-未知")
    private Boolean sex;

    @ApiModelProperty(value = "用户年龄")
    private Integer age;

    @ApiModelProperty(value = "学历")
    private String edu;

    @ApiModelProperty(value = "居住城市")
    private String city;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "封面图片")
    private String coverPic;

    @ApiModelProperty(value = "行业")
    private String industry;

    @ApiModelProperty(value = "收入")
    private String income;

    @ApiModelProperty(value = "婚姻状态")
    private String marriage;

    private Date created;

    private Date updated;


}
