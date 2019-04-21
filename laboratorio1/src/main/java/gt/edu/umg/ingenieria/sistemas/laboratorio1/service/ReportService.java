package gt.edu.umg.ingenieria.sistemas.laboratorio1.service;

import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.Client;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.reflect.Field;

/**
 *
 * @author Josué Barillas (jbarillas)
 */
@Service
public class ReportService {
    public String generateReport(List<Client> clientList, ServletContext servletContext) {

        String serverPath = servletContext.getRealPath("/");
        String directory = "var/wwww";

        File reportsDirectory = new File(serverPath + directory);
        List<File> filesInReportsDirectory = new ArrayList<>();
        if (!reportsDirectory.exists()) {
            boolean directoryCreated = reportsDirectory.mkdirs();
        } else {
            try (Stream<Path> paths = Files.walk(Paths.get(serverPath + directory))) {
                filesInReportsDirectory = paths
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileName = "/Clientes_1.html";
        if (!filesInReportsDirectory.isEmpty()) {
            fileName = "/Clientes_" + (filesInReportsDirectory.size() + 1) + ".html";
        }
        fileName = directory + fileName;
        String filePath = serverPath + fileName;
        fileName = "/" + fileName;

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                boolean fileCreated = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<table id = \"clientReport\" border = \"1\">\n");
        stringBuilder.append("<tr>\n");
        for (Field field : Client.class.getDeclaredFields()) {
            stringBuilder.append("<th>");
            stringBuilder.append(field.getName());
            stringBuilder.append("</th>");
        }
        stringBuilder.append("</tr>\n");

        for (Client client : clientList) {
            stringBuilder.append("<tr>\n");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getId());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getFirstName());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getLastName());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getNit());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getPhone());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getAddress());
            stringBuilder.append("</td>");
            stringBuilder.append("<td>");
            stringBuilder.append(client.getBirthDate());
            stringBuilder.append("</td>");
            stringBuilder.append("</tr>\n");
        }

        stringBuilder.append("</table>\n");

        Writer writer;
        try {
            writer = new FileWriter(file);
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        return "El reporte " + fileName + " ha sido generado.";
    }
}
