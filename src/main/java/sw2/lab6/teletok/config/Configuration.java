package sw2.lab6.teletok.config;


import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@org.springframework.context.annotation.Configuration
public class Configuration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry){
        registry
                .addResourceHandler("/fileUploaded/**")
                .addResourceLocations("file:C:/Users/samue/PicturesSW/fileUploaded");
    }

}
