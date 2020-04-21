package web.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import weimob.cart.api.response.CartInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 锁工具类
 *
 * @author tanlongjun
 */
@Component
@Slf4j
public class RedisClientUtils {

    /**
     * redis lua锁脚本文件路径
     */
    public static final String REDIS_LOCK_LUA_PATH = "lua/redis_lock.lua";

    /**
     * redis lua解锁脚本路径
     */
    public static final String REDIS_UNLOCK_LUA_PATH = "lua/redis_unlock.lua";

    private static final Long SUCCESS = 1L;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 加锁
     *
     * @param key
     * @param ttl
     * @param value
     * @return
     */
    public boolean lock(String key, Integer ttl, String value) {
        List<String> keys = new ArrayList();
        keys.add(key);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(REDIS_LOCK_LUA_PATH)));
        redisScript.setResultType(Long.class);
        Object res = redisTemplate.execute(redisScript, keys, String.valueOf(ttl), value);
        //判断加锁是否成功
        if (SUCCESS.equals(res)) {
            log.info("成功加锁：SUCCESS to acquire lock:" + Thread.currentThread().getName() + ", Status code reply:" + res);
            return true;
        }
        while (true) {
            try {
                Thread.sleep(100);
                res = redisTemplate.execute(redisScript, keys, String.valueOf(ttl), value);
                if (SUCCESS.equals(res)) {
                    return true;
                }
                continue;
            } catch (InterruptedException e) {
                log.info("获取锁的参数:key:{},value:{}", key, value);
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 解锁
     *
     * @param key
     * @param value <p>
     */
    public void unlock(String key, String value) {
        List<String> keys = Lists.newArrayList();
        keys.add(key);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(REDIS_UNLOCK_LUA_PATH)));
        redisScript.setResultType(Long.class);
        Long execute = 0L;
        try {
            execute = redisTemplate.execute(redisScript, keys, value);
        } catch (Exception e) {
            log.error("解锁异常:{}", e.getMessage());
        }
        if (SUCCESS.equals(execute)) {
            log.info("解锁成功:{} value:{}", key, value);
        } else {
            log.info("解锁失败:{} value:{}", key, value);
        }
    }

    public void save(String key, String userId, List<CartInfo> result, Integer ttl) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(result);
            redisTemplate.opsForHash().put(key, userId, s);
            redisTemplate.expire(key, ttl, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }

    public List<CartInfo> getCache(String key, String userId) {
        try {
            String o = (String) redisTemplate.opsForHash().get(key, userId);
            if (StringUtils.isEmpty(o)) {
                return null;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            List<CartInfo> cartInfo = objectMapper.readValue(o, new TypeReference<List<CartInfo>>() {
            });
            return cartInfo;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public void deleteCache(String key, String userId) {
        redisTemplate.opsForHash().delete(key, userId);
    }
}

