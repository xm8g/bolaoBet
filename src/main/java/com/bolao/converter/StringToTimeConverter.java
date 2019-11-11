package com.bolao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.bolao.entity.Time;
import com.bolao.service.TimeService;

@Component 
public class StringToTimeConverter implements Converter<String, Time> {

	@Autowired
	private TimeService service;
	
	@Override
	public Time convert(String id) {
		
		if (id.isEmpty()) {
			return null;
		}
		Time time = service.buscarTime(Long.valueOf(id));
		
		return time;
	}

}
