package com.nuga.curation.domain;

import io.swagger.annotations.ApiModelProperty;


public class WishResponseEntity {
    @ApiModelProperty(value = "status", position = 1)
    public Long articleId;
    @ApiModelProperty(value = "data", position = 2)
    public String message;
}
