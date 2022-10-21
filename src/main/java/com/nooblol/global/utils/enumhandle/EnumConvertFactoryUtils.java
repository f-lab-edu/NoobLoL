package com.nooblol.global.utils.enumhandle;


import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 소문자로 EnumType이 들어오는 경우 대문자로 치환해서 타입을 전달해 주기 위한 셋팅
 * <p>
 * Reference : https://kapentaz.github.io/java/spring/Enum-and-@RequestParam-in-Spring/#
 */
public class EnumConvertFactoryUtils implements
    ConverterFactory<String, Enum<? extends EnumConvertUtils>> {

  @Override
  public <T extends Enum<? extends EnumConvertUtils>> Converter<String, T> getConverter(
      Class<T> targetType) {
    return new StringToEnumConvertUtils<>(targetType);
  }

  private static final class StringToEnumConvertUtils<T extends Enum<? extends EnumConvertUtils>> implements
      Converter<String, T> {

    private final Class<T> enumType;
    private final boolean constantEnum;

    public StringToEnumConvertUtils(Class<T> enumType) {
      this.enumType = enumType;
      this.constantEnum = Arrays.stream(enumType.getInterfaces())
          .anyMatch(i -> i == EnumConvertUtils.class);
    }

    @Override
    public T convert(String source) {
      if (source.isEmpty()) {
        return null;
      }

      source = source.toUpperCase();

      T[] constants = enumType.getEnumConstants();
      for (T c : constants) {
        if (constantEnum && c.name().equals(source.trim())) {
          return c;
        }
      }
      return null;
    }
  }
}
