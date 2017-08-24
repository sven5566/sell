package com.whr.sell.service;

import com.whr.sell.dataobject.ProductCategory;
import com.whr.sell.service.impl.CategoryServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryServiceImplTest {
    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    public void findOne() {
        final ProductCategory one = categoryService.findOne(1);
        Assert.assertNotNull(one);
    }

    @Test
    public void findAll() {
        final List<ProductCategory> all = categoryService.findAll();
        Assert.assertNotEquals(0,all.size());
    }

    @Test
    public void findByCategoryTypeIn() {
        final List<ProductCategory> list = categoryService.findByCategoryTypeIn(Arrays.asList(3,4));
        Assert.assertNotEquals(0,list.size());
    }

    @Test
    public void save() {
        ProductCategory productCategory=new ProductCategory("testService",4);
        Assert.assertEquals(productCategory,categoryService.save(productCategory));
    }
}
