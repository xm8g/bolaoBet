package com.bolao.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolao.entity.Time;
import com.bolao.entity.dto.Datatables;
import com.bolao.entity.dto.DatatablesColunas;
import com.bolao.repository.TimeRepository;

@Service
public class TimeService {

	@Autowired
	private TimeRepository timeRepository;
	
	@Autowired
	private Datatables datatables;
	
	@Transactional(readOnly = false)
	public void salvarTime(Time time) {
		timeRepository.save(time);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> todos(HttpServletRequest req) {
		datatables.setRequest(req);
		datatables.setColunas(DatatablesColunas.TIMES);
		Page<Time> page = timeRepository.findAll(datatables.getPageable());
		return datatables.getResponse(page);
	}
	
	@Transactional(readOnly = false)
	public void remover(Long id) {
		timeRepository.deleteById(id);
	}
	
	@Transactional(readOnly = false)
	public Time buscarTime(Long id) {
		return timeRepository.findById(id).get();
	}
	
	
}
