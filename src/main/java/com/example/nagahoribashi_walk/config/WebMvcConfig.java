package com.example.nagahoribashi_walk.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.nagahoribashi_walk.util.MyStringUtils;

/**
 * //全てのコントローラーに対して共通処理を行う
 * 
 * @author 海津
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    // 特定のリソースパスは指定ディレクトリを参照するようにMVCを設定する
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // "/uploads/**" をapplication.propertiesのapp.upload.dirにマッピング
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + MyStringUtils.ensureTrailingSlash(uploadDir));
    }

    // リゾルバリストに必要なリゾルバを追加
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        // Pageableに対して設定を行うためのクラスであり、リゾルバ
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();

        // ページ単位に表示する件数を追加(第一引数：ページ番号、第二引数：1ページあたりの表示件数)
        resolver.setFallbackPageable(PageRequest.of(0, 12));

        // 具体的な設定をリゾルバに追加後、リストに追加
        argumentResolvers.add(resolver);
    }
}