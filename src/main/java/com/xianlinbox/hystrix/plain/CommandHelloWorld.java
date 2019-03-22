package com.xianlinbox.hystrix.plain;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import rx.Observable;
import rx.Observer;

public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")); //必须
        this.name = name;
    }

    @Override
    protected String run() {
        /*
         网络调用 或者其他一些业务逻辑，可能会超时或者抛异常
        */
        System.out.println("运行方法中");
        return "Hello " + name + "!";
    }

    public static void main(String[] args) {
//        String s = new CommandHelloWorld("Bob").execute(); //
//        Future<String> future = new CommandHelloWorld("Bob").queue();

        //注册观察者事件拦截
        Observable<String> fs = new CommandHelloWorld("老王").observe();
        //注册结果回调事件
//        fs.subscribe(new Action1<String>() {
//            @Override
//            public void call(String result) {
//                System.out.println("result is:" + result);
//                //执行结果处理,result 为HelloWorldCommand返回的结果
//                //用户对结果做二次处理.
//            }
//        });
        //注册完整执行生命周期事件
        fs.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                // onNext/onError完成之后最后回调
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                // 当产生异常时回调
                System.out.println("onError " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                // 获取结果后回调
                System.out.println("onNext: " + v);
            }
        });
    }
}
  
