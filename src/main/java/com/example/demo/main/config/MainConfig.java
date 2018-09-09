package com.example.demo.main.config;

import com.example.demo.acl.config.AclConfig;
import com.example.demo.acl.config.AclMethodSecurityConfiguration;
import com.example.demo.user.service.IUserService;
import com.example.demo.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.sql.DataSource;
import java.util.Locale;

@Configuration
@Import({WebConfig.class,
        SessionConfig.class,
        SecurityConfiguration.class,
        MvcConfigurer.class,
        AclMethodSecurityConfiguration.class,
        AclConfig.class
})
//@EnableJpaRepositories(basePackages = {"com.example.demo.main.repository"})
//@ComponentScan(basePackages = {"com.example.demo.main", "com.example.demo.acl.service", "com.example.demo.user"})
@EnableTransactionManagement
public class MainConfig {

    LocalContainerEntityManagerFactoryBean lEMF;

    @Autowired
    private Environment env;

    @Bean
    public IUserService userService()
    {
        return new UserService();
    }

    @Bean
    public ModelMapper modelMapper()
    {
        return new ModelMapper();
    }



//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory()
//    {
////        javax.persistence.Table
//
//        lEMF = new LocalContainerEntityManagerFactoryBean();
//        lEMF.setPersistenceUnitName("com.example.demo.persistanceunit");
////        lEMF.setDataSource(dataSource());
//        DataSource dataSource = lEMF.getDataSource();
//
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        lEMF.setJpaVendorAdapter(vendorAdapter);
//        lEMF.setJpaProperties(additionalProperties());
////        System.out.println("11111111111");
////        System.out.println(dataSource);
////        System.out.println("22222222222");
////        System.out.println(env.getProperty("spring.datasource.username"));
////        System.out.println(env.getProperty("spring.datasource.password"));
////        System.out.println(env.getProperty("spring.datasource.url"));
////        System.out.println(env.getProperty("spring.datasource.driverClassName"));
////        lEMF.setPersistenceXmlLocation("./persistence.xml");
//        return lEMF;
//    }

//    @Bean
    public DataSource dataSource() {
        System.out.println("qqq2");

//        System.out.println(env.getProperty("spring.datasource.username"));
//        System.out.println(env.getProperty("hibernate.connection.password"));
//        System.out.println(env.getProperty("hibernate.connection.url"));
//        System.out.println(env.getProperty("hibernate.connection.driver_class"));
        return DataSourceBuilder
                .create()
                .username(env.getProperty("spring.datasource.username"))
                .password(env.getProperty("spring.datasource.password"))
                .url(env.getProperty("spring.datasource.url"))
                .driverClassName(env.getProperty("spring.datasource.driver_class"))
                .build();



//        Map<String, Object> map = new HashMap();
//        for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
//            PropertySource propertySource = (PropertySource) it.next();
//            if (propertySource instanceof MapPropertySource) {
//                map.putAll(((MapPropertySource) propertySource).getSource());
//            }
//            System.out.println(propertySource.getName() + " : " + propertySource.getSource());
//
//        }
    }

//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
//
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//
//    Properties additionalProperties() {
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
//        properties.setProperty(
//                "hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
//
//        return properties;
//    }
//
//
//    @Bean
//    public PlatformTransactionManager transactionManager(){
//        JpaTransactionManager transactionManager
//                = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(
//                entityManagerFactory().getObject() );
//
//        return transactionManager;
//    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.forLanguageTag("pl-PL"));
        return slr;
    }

//    @Bean
//    public LocalValidatorFactoryBean validator() {
//        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//        bean.setValidationMessageSource(messageSource());
//        return bean;
//    }
//
//    @Bean
//    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename("classpath:messages");
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

//    private static SessionFactory getSessionFactory()
//    {
//        if (sessionFactory == null)
//        {
//            Configuration configuration = new Configuration().configure();
//            ServiceRegistryBuilder registry = new ServiceRegistryBuilder();
//            registry.applySettings(configuration.getProperties());
//            ServiceRegistry serviceRegistry = registry.buildServiceRegistry();
//            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//        }
//        return sessionFactory;
//    }
}
