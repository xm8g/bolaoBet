package com.bolao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolao.repository.CampeonatoRepository;

@Service
public class CampeonatoService {

	@Autowired
	private CampeonatoRepository campeonatoRepository;
}
