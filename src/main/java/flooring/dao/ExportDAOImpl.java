package flooring.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import flooring.exception.PersistenceException;

@Component
public class ExportDAOImpl implements ExportDAO{
    
    @Override
    public void exportOrderData() throws PersistenceException{
        File[] files = new File("src/main/resources/Orders").listFiles();

        if (files == null) {
            throw new PersistenceException("Could not find Orders directory.");
        }

        File newDirectory = new File("src/main/resources/DataExports");
        newDirectory.mkdirs();
        File exportFile = new File(newDirectory, "DataExport.txt");

        try(PrintWriter pw = new PrintWriter(new FileWriter(exportFile))){
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))){
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                    String dateString = file.getName().substring(7, 15);
                    LocalDate date = LocalDate.parse(dateString, inputFormatter);
                    
                    reader.readLine();//Skip header
                    String line;
                    while ((line = reader.readLine()) != null) {
                        pw.println(line + "," + date.format(outputFormatter));
                    }
                }
            }
        }catch (IOException e) {
            throw new PersistenceException("Could not export order data.", e);
        }
    }

}
