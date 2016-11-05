# rest
使用SpringMvc做成Rest的时候是有一些不太优雅的地方,我自己碰到最明显的就是拦截器是基于Url的,而rest同一个Url的不同请求方法的拦截处理就不优雅.
这里把自己遇到过不太好处理的地方统统自己实现一下,保证使用起来足够简单.
### 初始化
rest在设计时就考虑希望可以拆开IOC独立实现,这样与IOC拆开来代码实现就更清晰,可以给看源码的同学提供最大的方便(代码写的其实并不好,逃...)
#### single模式
即单独使用rest,web.xml配置

```
<web-app>
    <!-- 配置控制器的包名,多个逗号隔开 -->
    <context-param>
        <param-name>component-scan</param-name>
        <param-value>com.tg.web.controller,com.tg.web.base</param-value>
    </context-param>

    <listener>
        <listener-class>com.tg.tiny4j.web.contextlistener.SingleRestLoaderListener</listener-class>
    </listener>

</web-app>
```
#### container模式
配合IOC使用,导入core模块依赖,继承AbstractWebContextListener,如:

```
public class WebAppLoaderListener extends AbstractWebContextListener {
    @Override
    public void registerHandle(HandleRegistry registry) {
        final WebAppControllerReader webAppControllerReader=new WebAppControllerReader();
        HandleAnnotation handle=new HandleAnnotation() {
            @Override
            public BeanDefinition handle(Class clazz) throws ClassNotFoundException {
                ControllerInfo controllerInfo=webAppControllerReader.read(clazz);
                return new BeanDefinition(controllerInfo.getName(), controllerInfo.getClassName());
            }
        };
        registry.addHandle(handle);
    }
}
```
相应的web.xml,诸如扫描的包等配置,相应的移到application.xml,application.xml的配置参见core模块

```
<web-app>
    <listener>
        <!-- 刚刚创建的WebAppLoaderListener类-->
        <listener-class>com.tg.tiny4j.web.contextlistener.SingleRestLoaderListener</listener-class>
    </listener>
</web-app>
```
#### integration
这个模块引用了core,rest.整合了这两个,使用时`<listener-class>`配成`com.tg.tiny4j.integration.context.listener.WebAppLoaderListener`,
这种方式虽然初始化项目不需要写任何类只加配置就行,但依赖不是很清楚.当然有了这个模块就比较容易想到怎么抽象出上面container模式的实现了.