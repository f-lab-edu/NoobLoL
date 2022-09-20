package com.nooblol.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/*
현재 throw-exception-if-no-handler-found , add-mappings에 대해서 설정을 비활성화? 해야하는 상황이다.
Custom한 ResponseBody를 항상 Return하도록 하기위해서 설정을 한상태인데...
https://tecoble.techcourse.co.kr/post/2021-11-24-spring-customize-unhandled-api/
해당 글에서 영감을 받았다. 위의 mvc에 대한 설정을 진행할떄도 참고했던 포스트인데...

포스트 작성자 분의 경우에는 Swagger로 설정을 했지만, 나역시도 Spring Rest Docs로 설정을 바꿔볼 수 있지 않을까해서 변경해봤다.

....이미 있네..?
 * https://stackoverflow.com/questions/63893805/in-spring-boot-rest-docs-how-to-set-html-suffix
 */

@Configuration
public class DocsResourceConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/docs/**")
        .addResourceLocations("classpath:/static/docs/");
  }

  @Bean
  public InternalResourceViewResolver internalResourceViewResolver() {
    InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setPrefix("/static/");
    internalResourceViewResolver.setSuffix(".html");
    return internalResourceViewResolver;
  }

}
