package com.whr.sell.service;

import com.whr.sell.dataobject.ProductInfo;
import com.whr.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductInfo findOne(String productId);
    List<ProductInfo> findAll();
    List<ProductInfo> findUpAll();
    ProductInfo save(ProductInfo productInfo);
    Page<ProductInfo> findAll(Pageable pageable);
    public void increaseStock(List<CartDTO> cartDTOList) ;
    public void decreaseStock(List<CartDTO> cartDTOList) ;
}
