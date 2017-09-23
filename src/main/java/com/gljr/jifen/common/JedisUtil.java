package com.gljr.jifen.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class JedisUtil {

    @Value("${redis.host}")
    private static String redis_host;

    public static Jedis getJedis(){

        Jedis jedis = new Jedis("localhost");
        return jedis;
    }
}
