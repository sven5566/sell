package com.whr.sell.VO;

import lombok.Data;

@Data
public class ResultVO <T>{
    private int code=0;
    private String msg="";
    private T data;
}
