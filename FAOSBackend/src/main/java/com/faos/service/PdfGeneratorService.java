package com.faos.service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import com.faos.model.Supplier;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfGeneratorService {
	
	public void generateSupplierPdf(List<Supplier> suppliers, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Add title
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph title = new Paragraph("Active Suppliers", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Empty line for spacing

        // Create table
        PdfPTable table = new PdfPTable(4); // Assuming 4 columns: ID, Name, Email, and Contact
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.5f, 3.0f});
        table.setSpacingBefore(10);

        // Table header
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(7, 71, 153));
        cell.setPadding(5);
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontHeader.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Name", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", fontHeader));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Contact", fontHeader));
        table.addCell(cell);

        // Table data
        for (Supplier supplier : suppliers) {
            table.addCell(supplier.getSupplierID());
            table.addCell(supplier.getName());
            table.addCell(supplier.getEmail());
            table.addCell(supplier.getContact());
        }

        document.add(table);
        document.close();
}
	}
