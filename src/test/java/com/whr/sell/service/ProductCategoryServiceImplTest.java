package com.whr.sell.service;

import com.whr.sell.dataobject.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceTest {
    
    @Test
    public ProductCategory findOne(Integer categoryId) {
        repository.findOne(categoryId);
    }

    @Test
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Test
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Test
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
