package com.devineni.login;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUpload {
    CommonsMultipartFile file;
	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
}
