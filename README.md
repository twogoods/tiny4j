# tiny4j
一个javaer，做点web基本就是spring全家桶，都是IOC，AOP，MVC这样的概念，那么我可不可以自己实现呢？所以打算先折腾一个玩具基本的类似实现，MVC的V就算了，Controller对Rest支持的好一点就行。
### IOC
现在还只是最最简单的IOC，如下：

```
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test.xml");
        ServiceBean serviceBean=(ServiceBean)applicationContext.getBean("testService");
        System.out.println(serviceBean);
        serviceBean.service();

        ServiceBean serviceBean2=(ServiceBean)applicationContext.getBean("serviceBean");
        System.out.println(serviceBean2);
        serviceBean2.service();

        //IOC上下文
        ApplicationContextHolder holder=applicationContext.getBean("applicationContextHolder", ApplicationContextHolder.class);
        System.out.println("holder get bean : "+holder.getBean("serviceBean"));
```

