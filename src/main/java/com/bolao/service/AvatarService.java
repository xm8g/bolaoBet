package com.bolao.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bolao.entity.Avatar;
import com.bolao.entity.Usuario;
import com.bolao.exception.FileStorageException;
import com.bolao.repository.AvatarRepository;
import com.bolao.repository.UsuarioRepository;

@Service
public class AvatarService {

	@Autowired
	private AvatarRepository avatarRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Avatar storeFile(MultipartFile avatar) {
        // Pega o nome do arquivo
        String fileName = StringUtils.cleanPath(avatar.getOriginalFilename());

        try {
            // Checa se o arquivo contém caracteres inválidos
            if(fileName.contains("..")) {
                throw new FileStorageException("Desculpe! O nome do arquivo contém uma sequencia de caracteres inválidos |  " + fileName);
            }
            Avatar avatarFile = new Avatar(fileName, avatar.getContentType(), avatar.getBytes());

            return avatarRepository.save(avatarFile);
        } catch (IOException ex) {
            throw new FileStorageException("Upload do arquivo " + fileName + " falhou. Tente novamente!");
        }
    }
	
	public Avatar loadImageAvatar(String login) {
		Usuario usuario = usuarioRepository.findByEmail(login);
		return usuario.getAvatar();
	}
}
