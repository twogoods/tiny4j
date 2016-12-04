# rest
web框架支持rest，json格式，不提供MVC的V了，基于自己以前的使用经历，会实现Spring里高频的功能以及对一些SpringMvc中不好处理的地方提供更好的支持。
rest在设计时就考虑希望可以拆开IOC独立实现,IOC可以单独使用，rest也可以离开IOC单独使用。这样拆开来代码实现也清晰一些,这样我自己实现或者其他同学想了解一个简单的web实现都更容易一些。当然Spring全家桶用多了在一个web程序里居然没有IOC功能是有点不可想象的，确实rest提供单独使用好像意义不大，有机会看看rest可不可以与Spring的IOC配合起来。
### 初始化
#### single模式
即脱离IOC单独使用rest，所有的配置都在web.xml里

```
<web-app>
    <!-- 配置扫描的包名,多个逗号隔开 -->
    <context-param>
        <param-name>component-scan</param-name>
        <param-value>com.tg.web.controller,com.tg.web.base</param-value>
    </context-param>

    <listener>
        <listener-class>com.tg.tiny4j.web.contextlistener.SingleRestLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>com.tg.tiny4j.web.servlet.DispatcherServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```
#### container模式
配合IOC使用,导入core模块依赖,继承`AbstractWebContextListener`完成自己的`ContextListener`（复制下面代码即可)

```
public class WebAppLoaderListener extends AbstractWebContextListener {
    private final WebAppControllerReader webAppControllerReader = new WebAppControllerReader();

    @Override
    public void registerHandle(HandleRegistry registry) {
        HandleAnnotation handle = new HandleAnnotation() {
            @Override
            public BeanDefinition handle(Class clazz) throws Exception {
                BaseInfo baseInfo = webAppControllerReader.read(clazz);
                if (Validate.isEmpty(baseInfo)) {
                    return null;
                } else {
                    return new BeanDefinition(baseInfo.getName(), baseInfo.getClassName());
                }
            }
        };
        registry.addHandle(handle);
    }

    @Override
    public void requestMapInitialized(ServletContextEvent servletContextEvent, WebApplicationContext applicationContext) throws Exception {
        webAppControllerReader.initRequestMap();
        webAppControllerReader.setInstances(applicationContext.getBean(webAppControllerReader.getControllerName()));
        servletContextEvent.getServletContext().setAttribute(WebApplicationEnvironment.WEBREQUESTMAPPER, webAppControllerReader.getRequestMapper());
    }
}
```
相应地在web.xml里配上刚刚创建的`ContextListener`就完成了rest和Ioc的整合,诸如扫描的包等配置,相应的移到application.xml

```
<web-app>
    <listener>
        <!--上面那个类的全类名-->
        <listener-class>com.xxx.WebAppLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>dispatcherServlet</servlet-name>
        <servlet-class>com.tg.tiny4j.web.servlet.DispatcherServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>dispatcherServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```
application.xml增加一些config的配置，`Bean`以及更多的配置参见core模块

```
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <configs>
        <!--配置文件-->
        <property-file>application.properties</property-file>
        <component-scan>${code4j.component-scan:com.tg.web.controller}</component-scan>
    </configs>
</beans>
```
###使用
rest提供了一些注解可以方便的使用，熟悉SpringMvc的同学一看就懂了。`@CROS`加上此注解就可跨域，可以配置请求源，方法等；`@PathVariable`，`@RequestParam`，`@RequestParam`和SpringMvc用法一样；方法返回值会转换成Json输出。

```
    @RequestMapping(mapUrl = "/test/{id}", method = HttpMethod.GET)
    @CROS(origins = "www.baidu.com", methods = {HttpMethod.GET}, maxAge = "3600")
    public Map<String, Object> modelTest(@PathVariable("id") long id, @RequestParam("name") String name, @RequestParam User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("id", id);
        map.put("user", user);
        return map;
    }
```
`@Interceptor`用于标识一个拦截器，order表示拦截器先后执行顺序(越小越靠前)，name默认是类名首字母小写，pathPatterns配置拦截的url。此处拦截器是基于url拦截的,而rest同一个url的不同请求方法的拦截处理这个注解就无法完成。
`@InterceptorSelect`配置响应方法走哪几个拦截器，不走哪几个，这种方式粒度更小，可以确保拦截器被正确执行解决上面的问题，因此**推荐**使用这种配置方法，当然也可以配合`@Interceptor`里的pathPatterns使用。

```
@Interceptor(pathPatterns = {"/base/user"},order = 2)
public class AInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("do A interceptor");
        return true;
    }
}

//controller里的响应方法
@RequestMapping(mapUrl = "/test", method = HttpMethod.GET)
@InterceptorSelect(include = {"aInterceptor"}, exclude = {"bInterceptor"})
public String interceptorTest() {
    return "haha";
}
```
异常处理也相同

```
    @ExceptionHandler(Exception.class)
    public String handleException(){
        return "error";
    }

    @RequestMapping(mapUrl = "/exception", method = HttpMethod.GET)
    public String exceptionTest() {
        int i = 1 / 0;
        return "haha";
    }
```
当然，如果你是配合IOC使用的，那IOC支持的都可以无所顾忌的拿来使用

```
@Api("/base")
public class TestController extends BaseController {

    @Value("${user.name:test}")
    private String name;

    @Inject
    private UserService userService;

    @RequestMapping
    public String index() {
        userService.query();
        return name;
    }
}
```
更多请直接查看[例子](https://github.com/twogoods/tiny4j/tree/master/web-test)
###最后
看，是不是很像SpringMvc，→_→哈哈
