package weimob.cart.server.starter;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import weimob.cart.CartServerConfiguration;

/**
 * @Author: 老张
 * @Date: 2020/3/26
 */
@Slf4j
@SpringBootApplication
@Import(CartServerConfiguration.class)
@EnableDubbo(scanBasePackages = "weimob.cart.api.facade")
public class WeiMobCartServerApplication {
    public static void main(String[] args) {
        log.info("cart server is starting");
        SpringApplication.run(WeiMobCartServerApplication.class,args);
    }
}
