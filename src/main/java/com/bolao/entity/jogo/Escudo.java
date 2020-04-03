package com.bolao.entity.jogo;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Escudo {

	private String fileName;

    private String fileType;

    @Lob
    private byte[] data;
}
