package com.nooblol.global.config;

import com.nooblol.global.utils.enumhandle.EnumConvertFactoryUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

  @Override
  protected void addFormatters(FormatterRegistry registry) {
    registry.addConverterFactory(new EnumConvertFactoryUtils());
  }
}
