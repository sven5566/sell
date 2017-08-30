package com.whr.sell.dto.converter;

import com.whr.sell.dataobject.OrderMaster;
import com.whr.sell.dto.OrderDTO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMaster2OrderDTOConverter {
    public static List<OrderDTO> convert(List<OrderMaster> orderMaster){
        return orderMaster.stream().map(e->convert(e)).collect(Collectors.toList());
    }
    public static OrderDTO convert(OrderMaster orderMaster){
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        return orderDTO;
    }

}
