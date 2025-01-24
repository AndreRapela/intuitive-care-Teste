package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WebScraping {

    public static void main(String[] args) {

        String baseUrl = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";

        String userHome = System.getProperty("user.home") + "\\Desktop\\intuitive-care\\intuitive-care-Teste\\Java\\TestWebScraping";

        String downloadDir = userHome + "\\downloads";
        String zipFilePath = userHome + "\\anexos.zip";

        try {

            Path downloadPath = Paths.get(downloadDir);
            Files.createDirectories(downloadPath);
            System.out.println("Diretório de downloads: " + downloadPath.toAbsolutePath());


            Document document = Jsoup.connect(baseUrl).get();


            Elements links = document.select("a[href]");


            for (Element link : links) {
                String href = link.absUrl("href");
                if ((href.contains("Anexo_I") || href.contains("Anexo_II")) && href.toLowerCase().endsWith(".pdf")) {
                    String fileName = href.substring(href.lastIndexOf("/") + 1);
                    downloadFile(href, Paths.get(downloadDir, fileName).toString());
                }
            }


            zipFiles(downloadDir, zipFilePath);

            System.out.println("Download e compactação concluídos com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void downloadFile(String fileUrl, String destination) throws IOException {
        Files.createDirectories(Paths.get(destination).getParent()); // Garantir que o diretório exista
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Arquivo baixado: " + destination);
        }
    }


    private static void zipFiles(String sourceDir, String zipFilePath) throws IOException {
        Path sourcePath = Paths.get(sourceDir);


        if (!Files.exists(sourcePath)) {
            System.err.println("Diretório não encontrado: " + sourcePath.toAbsolutePath());
            return;
        }

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Files.walk(sourcePath)
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        try {
                            ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                            zos.putNextEntry(zipEntry);
                            Files.copy(filePath, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

        System.out.println("Arquivos compactados em: " + zipFilePath);
    }
}
