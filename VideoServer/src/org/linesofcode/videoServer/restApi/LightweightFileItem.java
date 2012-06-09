package org.linesofcode.videoServer.restApi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.apache.commons.fileupload.FileItem;

public class LightweightFileItem implements FileItem {

	private static final long serialVersionUID = -5510923244151870961L;
	private String fieldName;
	private String contentType;
	private boolean formField;
	private String fileName;
	private File file;
	private StringBuilder buffer = new StringBuilder();

	public LightweightFileItem(String fieldName, String contentType,
			boolean isFormField, String fileName, String path) {
		this.fieldName = fieldName;
		this.contentType = contentType;
		this.formField = isFormField;
		this.fileName = fileName;
		this.file = new File(path);
	}

	@Override
	public void delete() {
		if(isFormField()) {
			return;
		}
		if(!file.delete()) {
			throw new RuntimeException(String.format("Failed to delete file %s", file.getAbsolutePath()));
		}
	}

	@Override
	public byte[] get() {
		byte[] data = new byte[(int)file.length()];
		try {
			getInputStream().read(data);
		} catch (IOException e) {
			throw new RuntimeException(String.format("Failed to read from file %s", file.getAbsolutePath()));
		}
		return data; 
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public String getFieldName() {
		return fieldName;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if(isFormField()) {
			return new ByteArrayInputStream(buffer.toString().getBytes());
		}
		FileInputStream fis = new FileInputStream(file);
		return new BufferedInputStream(fis);
	}

	@Override
	public String getName() {
		return fileName;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		if(isFormField()) {
			return new StringOutputStream(buffer);
		}
		FileOutputStream fos = new FileOutputStream(file);
		return new BufferedOutputStream(fos);
	}

	@Override
	public long getSize() {
		return file.length();
	}

	@Override
	public String getString() {
		if(isFormField()) {
			return buffer.toString();
		}
		Scanner in = null;
		try {
			in = new Scanner(getInputStream());
			StringBuilder builder = new StringBuilder();
			while(in.hasNext()) {
				builder.append(in.next());
			}
			return builder.toString();
		} catch (IOException e) {
			return null;
		} finally {
			in.close();
		}
	}

	@Override
	public String getString(String arg0) throws UnsupportedEncodingException {
		throw new UnsupportedOperationException("String representation of video files not supported");
	}

	@Override
	public boolean isFormField() {
		return formField;
	}

	@Override
	public boolean isInMemory() {
		return false;
	}

	@Override
	public void setFieldName(String arg0) {
		fieldName = arg0;
	}

	@Override
	public void setFormField(boolean arg0) {
		formField = arg0;
	}

	@Override
	public void write(File arg0) throws Exception {
		throw new UnsupportedOperationException("Writing files to disk not supported. Use streams instead.");
	}

}
