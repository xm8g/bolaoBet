package com.bolao.converter;

import java.io.IOException;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bolao.entity.jogo.Escudo;
import com.bolao.exception.FileStorageException;

@Component
public class MultipartToEscudoConverter implements Converter<MultipartFile, Escudo> {

	@Override
	public Escudo convert(MultipartFile escudoFile) {
		if (escudoFile.isEmpty()) {
			return null;
		}
        String fileName = StringUtils.cleanPath(escudoFile.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Desculpe! O nome do arquivo contém uma sequencia de caracteres inválidos |  " + fileName);
            }
            Escudo escudo = new Escudo(fileName, escudoFile.getContentType(), escudoFile.getBytes());

            return escudo;
        } catch (IOException ex) {
            throw new FileStorageException("Upload do arquivo " + fileName + " falhou. Tente novamente!");
        }
    }
}
