package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TransformacaoDeDados {

    public static String extractPdfToCsv(String pdfPath, int startPage, int endPage) throws IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));

       
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(startPage);
        stripper.setEndPage(endPage);


        String extractedText = stripper.getText(document);
        document.close();


        String csvFilePath = "Rol_de_Procedimentos_e_Eventos.csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath));

    
        writer.write(extractedText);
        writer.close();

        return csvFilePath;
    }

    public static void compressToZip(String csvFile) throws IOException {
        String zipFilePath = "Teste_André_Rapela.zip";

 
        try (FileInputStream fis = new FileInputStream(csvFile);
             FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {


            ZipEntry zipEntry = new ZipEntry(new File(csvFile).getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            zos.closeEntry();
        }

        
        Files.delete(Paths.get(csvFile));
    }

    public static void main(String[] args) {
        String userHome = System.getProperty("user.home");  
        String pdfPath = userHome + File.separator + "Desktop" + File.separator +
                "intuitive-care" + File.separator + "intuitive-care-Teste" + File.separator +
                "java" + File.separator + "TestWebscraping" + File.separator + "anexos" + File.separator +
                "Anexo_I_Rol_2021RN_465.2021_RN624_RN625.2024.pdf";  

        try {
   
            String csvFile = extractPdfToCsv(pdfPath, 3, 191);

            compressToZip(csvFile);

            System.out.println("Processo concluído: O arquivo ZIP foi gerado com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
