package com.bolao.web.dwr;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.bolao.entity.Time;
import com.bolao.repository.TimeRepository;

@Component
@RemoteProxy
public class DWRConsultas {

	@Autowired
	private TimeRepository timeRepository;
	
	@RemoteMethod
	public void buscarTimesPeloPais(String pais) {
		System.out.println(pais);
		//return timeRepository.findByPais(pais);
	}
}
