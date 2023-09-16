package com.shopme.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AstractExporter {

	public void export(List<Category> listCategories,HttpServletResponse response) throws IOException {
		super.setPesponeHeader("text/csv", ".csv",response, "categories_");
		CsvBeanWriter csvWriter =new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader= {"Category ID", "Name", "Alias", "Enabled"};
		String[] fieldMapping= {"id","name","alias","enabled"};
		
		csvWriter.writeHeader(csvHeader);
		for (Category category : listCategories) {
			csvWriter.write(category, fieldMapping);
		}
		
		csvWriter.close();
				}
	}

