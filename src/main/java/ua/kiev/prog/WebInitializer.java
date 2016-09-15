package ua.kiev.prog;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebInitializer implements WebApplicationInitializer{

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //создаем контекст на базе анотаций для веб приложения
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(AppConfig.class);//регистрируем в контексте класс конфигураций AppConfig с нашими бинами
        ctx.setServletContext(servletContext); //передаем Spring контексту ссылку на Servle контекст

        //вместо прописывания DispatcherServlet(ctx) в web.xml как welcome page
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.addMapping("/"); //прикрепляем ссылку
        servlet.setLoadOnStartup(1);  //приоритет
    }
}
