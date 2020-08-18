package com.carbonclick.tsttask.secretsanta.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Default page request")
public class PageRequest {

    @ApiModelProperty("Page number")
    private int pageNumber = 0;

    @ApiModelProperty("Page size")
    private int pageSize = 10;

    public int getOffset() {
        return pageNumber * pageSize;
    }
}
