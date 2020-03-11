package com.bolao.converter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToListConverter implements Converter<String, List<String>> {

	@Override
	public List<String> convert(String source) {
		
		if (StringUtils.isNotEmpty(source)) {
			return Stream.of(source.split(",", -1))
					.map(String::trim)
					.collect(Collectors.toList());
		}
		return null;
	}

}


