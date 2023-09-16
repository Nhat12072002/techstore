package com.shopme.export;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AstractExporter {
	public void export(List<User> listUsers,HttpServletResponse response) throws IOException {
	super.setPesponeHeader("text/csv", ".csv", response,"user_");
	CsvBeanWriter csvWriter =new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
	
	String[] csvHeader= {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
	String[] fieldMapping= {"id","email","firstname","lastname","roles","enabled"};
	
	csvWriter.writeHeader(csvHeader);
	for (User user : listUsers) {
		csvWriter.write(user, fieldMapping);
	}
	
	csvWriter.close();
			}
}
