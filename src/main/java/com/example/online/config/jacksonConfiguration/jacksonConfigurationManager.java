package com.example.online.config.jacksonConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class jacksonConfigurationManager implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        for(HttpMessageConverter cv:converters){
            if (cv instanceof MappingJackson2HttpMessageConverter){
                ObjectMapper mapper=((MappingJackson2HttpMessageConverter) cv).getObjectMapper();
                mapper.registerModule(new Hibernate6Module());
            }
        }
    }
}
