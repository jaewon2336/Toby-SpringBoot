package com.example.helloboot;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {
    public static void run(Class<?> applicationClass, String... args) {
        // 스프링 컨테이너
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
            @Override
            protected void onRefresh() {
                super.onRefresh();

                // tomcat servlet web server를 만드는 보조 공장
                ServletWebServerFactory serverFactory = this.getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);

                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet",dispatcherServlet)
                            .addMapping("/*");
                }); // 서블릿 컨테이너 생성 함수 (서블릿 등록)
                webServer.start(); // 톰캣 서블릿 컨테이너 동작
            }
        };
        applicationContext.register(applicationClass);
        applicationContext.refresh(); // 스프링 컨테이너 초기화(템플릿메서드 패턴)
    }
}
