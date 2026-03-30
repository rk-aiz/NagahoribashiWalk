package com.example.nagahoribashi_walk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.nagahoribashi_walk.util.StringUtils;


/**
 * 特定のパスは指定ディレクトリを参照するようにMVCを設定する
 * @author 海津
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // "/uploads/**" をapplication.propertiesのapp.upload.dirにマッピング
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + StringUtils.ensureTrailingSlash(uploadDir));
    }
}