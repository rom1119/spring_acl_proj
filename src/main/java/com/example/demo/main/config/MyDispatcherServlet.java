package com.example.demo.main.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyDispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer {

//    static {
//        System.setProperty("spring.datasource.driverClassName", "com.mysql.cj.jdbc.Driver");
//        System.setProperty("spring.datasource.url", "jdbc:mysql://localhost/pacgame_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
//        System.setProperty("spring.datasource.username", "root");
//        System.setProperty("spring.datasource.password", "agdrtv19");
//    }



        @Override
        protected Class<?>[] getRootConfigClasses() {
            System.out.println("1111");

//            System.out.println("aaaaaaaaaaaaaa");
//            ApplicationContext context =
//                    new ClassPathXmlApplicationContext(new String[] {"classpath:/META-INF/spring/application-context.xml"});


            return new Class[]{MainConfig.class};
        }

        @Override
        protected Class<?>[] getServletConfigClasses() {
            System.out.println("2222");

            return new Class[] {
                    WebConfig.class
            };
        }

        @Override
        protected String[] getServletMappings() {
            System.out.println("3333");

            return new String[] {"/"};
        }
}
