# tiny4j
会点java，做点web,基本也就是spring全家桶，都是IOC，AOP，MVC这样的概念，所以打算自己折腾一个，水平也有限先整个玩具级别的类似实现吧，MVC的V就算了，Controller对Rest支持的好一点就行。算不上轮子也就是个轱辘而已...
### IOC
简单使用如下：

```
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test.xml");
ServiceBean serviceBean=(ServiceBean)applicationContext.getBean("testService");
System.out.println(serviceBean);
serviceBean.service();

ServiceBean serviceBean2=(ServiceBean)applicationContext.getBean("serviceBean");
System.out.println(serviceBean2);
serviceBean2.service();

//全局的容器上下文
ApplicationContextHolder holder=applicationContext.getBean("applicationContextHolder", ApplicationContextHolder.class);
System.out.println("holder get bean : "+holder.getBean("serviceBean"));
```
[更多内容](https://github.com/twogoods/tiny4j/tree/master/core)
### AOP
TODO
### Rest
TODO

