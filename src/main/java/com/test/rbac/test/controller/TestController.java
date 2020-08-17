package com.test.rbac.test.controller;

import com.test.rbac.test.entity.Man;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 测试用的，没什么其他用处
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    //引入Spring data 的Redis操作组件，配置已经自动配置好了，直接注入即可
    private RedisTemplate redisTemplate;

    /**
     * 往redis里存储数据，但是这样存储redis会对数据进行一个序列化的操作，并不影响操作
     * @param man
     * @return True:创建成功  False:创建失败
     */
    @PostMapping("/setRedis")
    //使用线程池,新起一个线程去调用set方法
    @Async("asyncServiceExecutor")
    public Boolean set(@RequestBody Man man){
        //这里先要使用opForValue这个方法，使组件能够生成Redis类型一样的Key,Value的对象格式，然后再通过set方法去set数据
        redisTemplate.opsForValue().set("name",man);
        Boolean flag = redisTemplate.hasKey("name");
        return flag;
    }

    /**
     * 在Redis里拿取数据
     * @param key
     * @return
     */
    @GetMapping("/getRedis/{key}")
    public Object get(@PathVariable String key){
        Object obj = redisTemplate.opsForValue().get(key);
        return obj;
    }

    /**
     * 删除Redis里的指定键的数据，如果删除成功返回False，失败返回True
     * @param key
     * @return False:成功删除  True:删除失败
     */
    @DeleteMapping("/deleteRedis/{key}")
    public Boolean delete(@PathVariable String key){
        //删除时不需要先使用opForValue这个方法了
        redisTemplate.delete(key);
        //使用hasKey来判断redis里是否还存在这个key,还有Key的数据返回True,没有了返回False
        Boolean flag = redisTemplate.hasKey(key);
        return flag;
    }

}
