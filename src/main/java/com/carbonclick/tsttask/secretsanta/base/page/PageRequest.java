package com.carbonclick.tsttask.secretsanta.base.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
