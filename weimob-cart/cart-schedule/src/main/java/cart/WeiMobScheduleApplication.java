package cart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: 老张
 * @Date: 2020/4/21
 */
@SpringBootApplication
@EnableScheduling
@Slf4j
public class WeiMobScheduleApplication {
    public static void main(String[] args) {
        log.info("WeiMobScheduleApplication is run");
        SpringApplication.run(WeiMobScheduleApplication.class,args);
    }
}
