##IOC
基于xml配置的方式  

```
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <bean class="com.tg.tiny4j.core.ioc.beans.reader.LogBeanPostProcessor"></bean>

    <bean id="testDao" class="com.tg.tiny4j.core.ioc.beans.reader.DaoBean"></bean>

    <bean id="testService" class="com.tg.tiny4j.core.ioc.beans.reader.ServiceBean">
        <property name="s1" value="s1"></property>
        <property name="s2" value="s2"></property>
        <property name="daoBean" ref="testDao"></property>
    </bean>

</beans>
```
编程方式使用:

```
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("test.xml");
ServiceBean serviceBean=(ServiceBean)applicationContext.getBean("testService");
System.out.println(serviceBean);
serviceBean.service();

ServiceBean serviceBean2=(ServiceBean)applicationContext.getBean("serviceBean");
System.out.println(serviceBean2);
serviceBean2.service();
```
当然你也可以添加配置文件,并配置component-scan属性来自动扫描包下的类

```
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <configs>
        <property-file>application.properties</property-file>
        <component-scan>${tiny4j.component-scan:com.tg}</component-scan>
    </configs>

    <bean id="testDao" class="com.tg.tiny4j.core.ioc.beans.reader.DaoBean"></bean>
</beans>
```
只要配置好扫描的包完全可以只使用注解

```
@Configuration
public class ConfigBean {

    @Value("${user.name:testname}")
    private String name;
    @Value("${user.pass}")
    private String pass;

    @Inject
    private ServiceBean serviceBean;

    //创建的bean的名字就是方法名
    @Bean
    public DaoBean beanAnnDao(){
        return new DaoBean();
    }
    @Bean
    public ServiceBean beanAnnService(){
        //new ServiceBean(beanAnnDao()); 暂不支持构造器注入
        ServiceBean newBean=new ServiceBean();
        newBean.setS1(name+"---"+serviceBean.getS1());
        newBean.setS2(pass+"---"+serviceBean.getS2());
        newBean.setDaoBean(beanAnnDao());
        return newBean;
    }

}
```

