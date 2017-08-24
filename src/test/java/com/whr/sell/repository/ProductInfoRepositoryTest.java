package com.whr.sell.repository;

import com.whr.sell.dataobject.ProductInfo;
import com.whr.sell.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {
    @Autowired
    ProductInfoRepository repository;

    @Test
    public void save() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("123457");
        productInfo.setProductName("甜豆浆");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很好喝的粥");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        productInfo.setCategoryType(2);
        final ProductInfo save = repository.save(productInfo);
        Assert.assertNotNull(save);
    }

    @Test
    public void findAll() throws Exception{
        final List<ProductInfo> all = repository.findAll();
        Assert.assertNotEquals(0,all.size());
    }

    @Test
    public void findOne() throws Exception{
        final ProductInfo one = repository.findOne("123457");
        Assert.assertNotNull(one);
    }

    @Test
    public void findByProductStatus() throws Exception {
        List<ProductInfo> list = repository.findByProductStatus(ProductStatusEnum.UP.getCode());
        Assert.assertNotEquals(0,list.size());
    }

}