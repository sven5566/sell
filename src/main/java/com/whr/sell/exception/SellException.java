package com.whr.sell.exception;

import com.whr.sell.enums.ResultEnum;

public class SellException extends RuntimeException {
    Integer code;
    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code=resultEnum.getCode();
    }

    public SellException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
