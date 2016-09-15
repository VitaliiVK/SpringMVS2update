package ua.kiev.prog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@ComponentScan("ua.kiev.prog")
@EnableWebMvc
public class AppConfig {

    @Bean // Бин указывает где лежат страницы для отображения пользователю
    public UrlBasedViewResolver setupViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setPrefix("/WEB-INF/pages/"); //каталог где лежат страницы
        resolver.setSuffix(".jsp"); //расширение страниц
        resolver.setViewClass(JstlView.class); //класс для рендеренга Jstl страниц
        resolver.setOrder(1);
        return resolver;
    }

    @Bean //для передачи файлов в Multipart формате
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
