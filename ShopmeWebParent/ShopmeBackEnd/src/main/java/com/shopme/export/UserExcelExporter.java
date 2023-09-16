package com.shopme.export;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shopme.common.entity.User;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class UserExcelExporter extends AstractExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	public UserExcelExporter() {
		 workbook= new XSSFWorkbook();
		 
	}
	private void writeHeaderLine() {
	 sheet=workbook.createSheet("Users");
	 XSSFRow row=sheet.createRow(0);
	 
	 XSSFCellStyle cellStyle=workbook.createCellStyle();
		XSSFFont font= workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		createCell(row,0,"User ID",cellStyle);
		createCell(row,1,"Email",cellStyle);
		createCell(row,2,"First Name",cellStyle);
		createCell(row,3,"Last Name",cellStyle);
		createCell(row,4,"Roles",cellStyle);
		createCell(row,5,"Enabled",cellStyle);
	}
	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style ) {
		XSSFCell cell=row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		if(value instanceof Integer) {
			cell.setCellValue((Integer)value);
		}else if(value instanceof Boolean) {
		cell.setCellValue((Boolean)value);
		} else {
			cell.setCellValue((String)value);
		}
		cell.setCellStyle(style);
	}
	public void export(List<User> listUsers,HttpServletResponse response) throws IOException {
		super.setPesponeHeader("application/oclet-stream", ".xlsx", response,"user_");
		
		writeHeaderLine();
		writeDataLines(listUsers);
		
		ServletOutputStream outputStream=response.getOutputStream();	
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
		
		
}
	private void writeDataLines(List<User> listUsers) {
		int rowIndex=1;
		XSSFCellStyle cellStyle=workbook.createCellStyle();
		XSSFFont font= workbook.createFont();
		
		font.setFontHeight(14);
		cellStyle.setFont(font);
		for(User user: listUsers) {
			XSSFRow row=sheet.createRow(rowIndex++);
			int columnIndex=0;
			createCell(row,columnIndex++,user.getId(),cellStyle);
			createCell(row,columnIndex++,user.getEmail(),cellStyle);
			createCell(row,columnIndex++,user.getFirstname(),cellStyle);
			createCell(row,columnIndex++,user.getLastname(),cellStyle);
			createCell(row,columnIndex++,user.getRoles().toString(),cellStyle);
			createCell(row,columnIndex++,user.isEnabled(),cellStyle);
		}
		
	}
}
