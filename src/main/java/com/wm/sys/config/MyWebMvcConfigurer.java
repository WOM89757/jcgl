//package com.wm.sys.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * WebMvc 扩展配置类，应用一启动，本类就会执行
// */
//
//
//@Configuration
//public class MyWebMvcConfigurer implements WebMvcConfigurer {
//
//    @Value("${local.resourceHandler}")
//    private String resourceHandler;//请求 url 中的资源映射
//
//    @Value("${local.src}")
//    private String location;//上传文件保存的本地目录，使用@Value获取全局配置文件中配置的属性值
//
//    /**
//     * 配置静态资源映射
//     *
//     * @param registry
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //就是说 url 中出现 resourceHandler 匹配时，则映射到 location 中去,location 相当于虚拟路径
//        //映射本地文件时，开头必须是 file:/// 开头，表示协议
//        registry.addResourceHandler(resourceHandler).addResourceLocations(location);
//       // registry.addResourceHandler("/system/32/resources/**").addResourceLocations("classpath:/static/");
//    }
//}