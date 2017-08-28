package com.whr.sell.service;

import com.whr.sell.dataobject.ProductCategory;
import com.whr.sell.dataobject.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductInfo findOne(String productId);
    List<ProductInfo> findAll();
    List<ProductInfo> findUpAll();
    ProductInfo save(ProductInfo productInfo);
    Page<ProductInfo> findAll(Pageable pageable);
}
