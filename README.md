# tiny4j
会点java，做点web，基本也就是spring全家桶，都是IOC，AOP，MVC这样的概念，所以打算自己折腾一个，实现最基本最常用的一些功能。
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
[详细说明](https://github.com/twogoods/tiny4j/tree/master/core)
### rest
不多说直接上controller代码

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

    @RequestMapping(mapUrl = "/test/{id}", method = HttpMethod.GET)
    @CROS(origins = "www.baidu.com", methods = {HttpMethod.GET}, maxAge = "3600")
    public String patgTest(@PathVariable("id") String id) {
        return id;
    }

    @RequestMapping(mapUrl = "/test", method = HttpMethod.GET)
    @InterceptorSelect(include = {"aInterceptor"}, exclude = {"bInterceptor"})
    public String interceptorTest() {
        return "haha";
    }


    @RequestMapping(mapUrl = "/index")
    @CROS
    public String paramTest(@RequestParam("id") long id, @RequestParam("name") String name) {
        return name + "---" + id;
    }

    @RequestMapping(mapUrl = "/user/{id}", method = HttpMethod.PUT)
    @CROS
    public User insert(@PathVariable("id") long id, @RequestBody User user) {
        return user;
    }
}
```
是不是感觉很熟悉，SpringMvc既视感...  
**现已支持SpringBoot风格的可执行jar**，请看[详细说明](https://github.com/twogoods/tiny4j/tree/master/rest)  

TODO

* 反射优化

### AOP
TODO
