package com.xianlinbox.hystrix.plain.fallback;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class RequestCacheCommand extends HystrixCommand<String> {
    private final int id;

    public RequestCacheCommand(int id) {
        super(HystrixCommandGroupKey.Factory.asKey("RequestCacheCommand"));
        this.id = id;
    }

    @Override
    protected String run() throws Exception {
        System.out.println(Thread.currentThread().getName() + " executing id=" + id);
        return "executed=" + id;
    }

    //重写getCacheKey方法,实现区分不同请求的逻辑
    @Override
    protected String getCacheKey() {
        return String.valueOf(id);
    }

    public static void main(String[] args) {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            RequestCacheCommand command2a = new RequestCacheCommand(2);
            RequestCacheCommand command2b = new RequestCacheCommand(2);
            String result2a = command2a.execute();
            Assert.assertTrue("executed=2".equals(result2a));
            //isResponseFromCache判定是否是在缓存中获取结果
            Assert.assertFalse(command2a.isResponseFromCache());
            String result2b = command2b.execute();
            Assert.assertTrue("executed=2".equals(result2b));
            Assert.assertTrue(command2b.isResponseFromCache());
        } finally {
            context.shutdown();
        }
        context = HystrixRequestContext.initializeContext();
        try {
            RequestCacheCommand command3b = new RequestCacheCommand(2);
            String result3b = command3b.execute();
            Assert.assertTrue("executed=2".equals(result3b));
            Assert.assertFalse(command3b.isResponseFromCache());
        } finally {
            context.shutdown();
        }
    }
}

class Assert {

    public static void assertTrue(Object execute) {
        System.out.println(execute);
    }

    public static void assertFalse(boolean responseFromCache) {
        System.out.println(responseFromCache == false);
    }
}