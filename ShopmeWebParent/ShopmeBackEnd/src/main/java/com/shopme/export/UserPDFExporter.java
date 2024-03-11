package com.shopme.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserPDFExporter extends AstractExporter {


	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setPesponeHeader("application/.pdf", ".pdf", response,"user_");
		
		
		Document document=new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(30);
		font.setColor(Color.black);
		
		Paragraph paragraph=new Paragraph("List of users",font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		
		PdfPTable table= new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f,3.5f,3.0f,3.0f,3.0f,1.7f});
		writeTableHeader(table);
		WriteTableData(table,listUsers);
		document.add(table);
		document.close();
	}

	private void WriteTableData(PdfPTable table, List<User> listUsers) {
		for(User user:listUsers) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getFirstname());
			table.addCell(user.getLastname());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell= new PdfPCell();
		cell.setBackgroundColor(Color.gray);
		cell.setPadding(5);
		Font font=FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.white);
		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("First Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Last Name", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
		
		
		
	}

}
