package com.whr.sell;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoggerTest {
    private final Logger logger= LoggerFactory.getLogger(Logger.class);

    @Test
    public void test1(){
        logger.debug("debug...{}--istest..{}","asbk","test2");
        logger.info("info...{}--istest..{}","asbk","test2");
        logger.error("error");
    }
}
