package lzlz000.shirodemo.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lzlz000.shirodemo.entity.MyProperties;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class SpringConfig {

    /**
     * 使用fast-json 替换 jackson
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters((HttpMessageConverter<?>) fastConverter);
    }

    /**
     * 自定义配置
     */
    @Bean
    @ConfigurationProperties(prefix = "myYml")
    public MyProperties myYml(){
        return new MyProperties();
    }
}
