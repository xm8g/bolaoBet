package com.bolao.entity.user;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.bolao.entity.AbstractEntity;

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
