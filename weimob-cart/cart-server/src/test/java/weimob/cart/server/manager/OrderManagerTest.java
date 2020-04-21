package weimob.cart.server.manager;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import weimob.cart.CartServerConfiguration;

import static org.junit.Assert.*;

/**
 * @Author: 老张
 * @Date: 2020/4/21
 */

public class OrderManagerTest {
    @Autowired
    private OrderManager orderManager;


    @Test
    public void createOrder() {
    }
}