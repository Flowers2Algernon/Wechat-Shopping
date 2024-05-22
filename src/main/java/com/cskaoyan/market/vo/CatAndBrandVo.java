package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName CatAndBrandVo
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/16 10:10
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatAndBrandVo {

    private Integer value;

    private String label;

    private List<CatAndBrandVo> children;
}
