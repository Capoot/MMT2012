package org.linesofcode.videoServer.restApi;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

public class LightweightFileItemFactory implements FileItemFactory {

	private String dir;
	
	public LightweightFileItemFactory(String dir) {
		this.dir = dir;
	}
	
	@Override
	public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
		return new LightweightFileItem(fieldName, contentType, isFormField, fileName, String.format("%s/%s", dir, fileName));
	}

}
