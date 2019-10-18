package com.bolao.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name="avatar")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Avatar extends AbstractEntity {

	private String fileName;

    private String fileType;

    @Lob
    private byte[] data;
	
}
