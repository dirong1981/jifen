package com.gljr.jifen.common;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class JedisUtil {
    public Jedis getJedis(){

        Jedis jedis = new Jedis("localhost");
        return jedis;
    }
}
