package com.whr.sell.service.impl;

import com.whr.sell.dataobject.OrderDetail;
import com.whr.sell.dto.OrderDTO;
import com.whr.sell.enums.OrderStatusEnum;
import com.whr.sell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {
    @Autowired
    OrderServiceImpl orderService;

    private final String BUYER_OPENID = "1101110";

    private final String ORDER_ID = "1504071889137336713";

    @Test
    public void testCreate() {
        OrderDTO order = new OrderDTO();
        order.setOrderId(ORDER_ID);
        order.setBuyerOpenid(BUYER_OPENID);
        order.setBuyerAddress("buyAddress测试");
        order.setBuyerName("buyName测试");
        order.setBuyerPhone("buyPhone测试");

        OrderDetail detail1 = new OrderDetail();
        detail1.setProductId("123456");
        detail1.setProductQuantity(1);

        OrderDetail detail2 = new OrderDetail();
        detail2.setProductId("123457");
        detail2.setProductQuantity(2);

        List<OrderDetail> list = new ArrayList<>();
        list.add(detail1);
        list.add(detail2);
        order.setOrderDetailList(list);

        final OrderDTO result = orderService.create(order);
        log.info("【创建订单】result={}", result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() throws Exception {
        OrderDTO result = orderService.findOne(ORDER_ID);
        log.info("【查询单个订单】result={}", result);
        Assert.assertEquals(ORDER_ID, result.getOrderId());
    }

    @Test
    public void findList() throws Exception{
        PageRequest page=new PageRequest(0,2);
        final Page<OrderDTO> list = orderService.findList(BUYER_OPENID, page);
        log.info("【查询买家订单】result={}",list);
        Assert.assertNotEquals(0,list.getTotalElements());
    }


    @Test
    public void cancel() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(), result.getOrderStatus());
    }

    @Test
    public void finish() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(), result.getOrderStatus());
    }

    @Test
    public void paid() throws Exception {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(), result.getPayStatus());
    }
}