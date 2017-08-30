package com.whr.sell.service.impl;

import com.whr.sell.dataobject.OrderDetail;
import com.whr.sell.dataobject.OrderMaster;
import com.whr.sell.dataobject.ProductInfo;
import com.whr.sell.dto.CartDTO;
import com.whr.sell.dto.OrderDTO;
import com.whr.sell.dto.converter.OrderMaster2OrderDTOConverter;
import com.whr.sell.enums.OrderStatusEnum;
import com.whr.sell.enums.PayStatusEnum;
import com.whr.sell.enums.ResultEnum;
import com.whr.sell.exception.SellException;
import com.whr.sell.repository.OrderDetailRepository;
import com.whr.sell.repository.OrderMasterRepository;
import com.whr.sell.service.OrderService;
import com.whr.sell.service.ProductService;
import com.whr.sell.util.KeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    ProductService productService;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderMasterRepository orderMasterRepository;


    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        final String orderId = KeyUtils.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        //1.查询商品详情,确认商品是否存在，
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            final ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
//           2.计算总价
            orderAmount = productInfo.getProductPrice().
                    multiply(new BigDecimal(orderDetail.getProductQuantity())).
                    add(orderAmount);
            orderDetail.setDetailId(KeyUtils.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        //3.写入订单数据（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //4.扣库存
        final List<CartDTO> cartDTOList = orderDTO.getOrderDetailList()
                .stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);
        return orderDTO;
    }

    @Override
    public OrderDTO findOne(String orderId) {
        final OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        final List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (details.isEmpty()) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(details);
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        final Page<OrderMaster> masterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        final List<OrderDTO> convert = OrderMaster2OrderDTOConverter.
                convert(masterPage.getContent());
        return new PageImpl<>(convert, pageable, masterPage.getTotalElements());
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //1.判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //2.更改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        final OrderMaster saveResult = orderMasterRepository.save(orderMaster);
        if (saveResult == null) {
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //3.返库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        final List<CartDTO> collect = orderDTO.getOrderDetailList().stream().
                map(e -> new CartDTO(e.getProductId(), e.getProductQuantity())).collect(Collectors.toList());
        productService.increaseStock(collect);

        //4.判断付款状态,如果已经支付，需要更改付款状态 TODO
        if(PayStatusEnum.SUCCESS.getCode().equals(orderDTO.getPayStatus())){

        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        if(!OrderStatusEnum.NEW.getCode().equals(orderDTO.getOrderStatus())){
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        final OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        final OrderMaster result = orderMasterRepository.save(orderMaster);
        if(result==null){
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改支付状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        final OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        final OrderMaster saveResult = orderMasterRepository.save(orderMaster);
        if(saveResult==null){
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }
}
