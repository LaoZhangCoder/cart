package cart.web.starter;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import web.CartWebConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author: 老张
 * @date: 2019-07-24
 */
@Slf4j
@SpringBootApplication
@Import(CartWebConfiguration.class)
@EnableDubbo
public class WeiMobCartWebApplication {
    public static void main(String[] args) {
        log.info("cart web is starting");
        SpringApplication.run(WeiMobCartWebApplication.class,args);
    }
}
