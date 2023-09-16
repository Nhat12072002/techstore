package com.shopme.export;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class AstractExporter {
	public void setPesponeHeader(String contentType,String extention,HttpServletResponse response,String prefix) throws IOException {
		SimpleDateFormat dateFormatter= new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp=dateFormatter.format(new Date());
		String fileName=prefix+timestamp+extention;
		response.setContentType(contentType);
		String headerKey="Content-Disposition";
		String headerValue="attachment; filename="+fileName;
		response.setHeader(headerKey, headerValue);
}
}
