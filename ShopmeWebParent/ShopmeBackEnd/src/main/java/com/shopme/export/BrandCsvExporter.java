package com.shopme.export;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Brand;
import jakarta.servlet.http.HttpServletResponse;

public class BrandCsvExporter extends AstractExporter {

	public void export(List<Brand> listBrands,HttpServletResponse response) throws IOException {
		super.setPesponeHeader("text/csv", ".csv",response, "brands_");
		CsvBeanWriter csvWriter =new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader= {"Brand ID", "Name", "Category"};
		String[] fieldMapping= {"id","name","categories"};
		
		csvWriter.writeHeader(csvHeader);
		for (Brand brand : listBrands) {
			csvWriter.write(brand, fieldMapping);
		}
		
		csvWriter.close();
				}
	}

