package com.whr.sell;

import com.whr.sell.dataobject.ProductCategory;
import com.whr.sell.repository.ProductCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    Logger logger= LoggerFactory.getLogger(ProductCategoryRepository.class);

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Test
    public void saveTest(){
        ProductCategory productCategory=new ProductCategory("test2",3);
        productCategoryRepository.save(productCategory);
    }

    @Test
    public void testFindAll(){
        List<ProductCategory> all = productCategoryRepository.findAll();
        logger.error("all=="+all);
    }

    @Test
    public void findOneTest() {
        ProductCategory productCategory = productCategoryRepository.findOne(1);
        System.out.println(productCategory.toString());
    }
    @Test
    public void testFindList() {
        List<Integer> list= Arrays.asList(1,4);
        List<ProductCategory> resultList = productCategoryRepository.findByCategoryTypeIn(list);
        System.out.println(resultList);
    }
}
