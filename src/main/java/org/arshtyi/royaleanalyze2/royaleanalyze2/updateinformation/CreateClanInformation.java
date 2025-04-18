package org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;

/**
 * This class is used to create clan information by reading from an Excel file.
 * It retrieves the clan names and tags from the file and stores them in a list
 * of Clan objects.
 * It also includes methods to get clan information and to handle logging.
 * It is part of the RoyaleAnalyze2 application.
 */
@EnableAutoConfiguration
public class CreateClanInformation {
    /**
     * This class represents a clan with a name and a tag.
     * It includes getters and setters for the name and tag.
     */
    public static class Clan {
        private String name;
        private String tag;

        /**
         * This constructor initializes a Clan object with the given name and tag.
         */
        public Clan(String name, String tag) {
            this.name = name;
            this.tag = tag;
        }

        /**
         * This method returns the name of the clan.
         * 
         * @return The name of the clan.
         */
        public String getName() {
            return name;
        }

        /**
         * This method sets the name of the clan.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * This method returns the tag of the clan.
         * 
         * @return The tag of the clan.
         */
        public String getTag() {
            return tag;
        }

        /**
         * This method sets the tag of the clan.
         */
        public void setTag(String tag) {
            this.tag = tag;
        }
    }

    /**
     * This method retrieves clan information from an Excel file and writes it to a
     * JSON file.
     * It uses the Apache POI library to read the Excel file and the Jackson library
     * to write the JSON file.
     * It also includes error handling and logging.
     * 
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean getClans() {
        String logFilePath = Externs.getLogFilePath();
        String clansXlsxFilePath = Externs.getClansInformationXlsxFilePath();
        String clansJsonFilePath = Externs.getClansInformationJsonFilePath();
        Logger.log("INFO", "Preparing to get clan information...", CreateClanInformation.class, logFilePath);
        File file = new File(clansXlsxFilePath);
        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Clan> clans = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String name = row.getCell(0).getStringCellValue();
                    String tag = row.getCell(1).getStringCellValue().substring(1);
                    clans.add(new Clan(name, tag));
                }
            }
            Externs.setClans(clans);
            if (Externs.getClans().isEmpty()) {
                Logger.log("ERROR", "Clan information is empty, preparing to exit...", CreateClanInformation.class,
                        logFilePath);
                return false;
            }
            Logger.log("INFO", "Clan information retrieved, preparing to write to file...",
                    CreateClanInformation.class, logFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(new File(clansJsonFilePath), clans);
                Logger.log("INFO", "Clan information written to: " + clansJsonFilePath, CreateClanInformation.class,
                        logFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log("ERROR", "Failed to write clan information, preparing to exit...",
                        CreateClanInformation.class, logFilePath);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("ERROR", "Failed to retrieve clan information, preparing to exit...",
                    CreateClanInformation.class, logFilePath);
            return false;
        }
    }

    /**
     * This method is the main entry point for the program.
     * It calls the getClans method to retrieve clan information.
     * 
     * @return true if the operation was successful, false otherwise.
     */
    public static boolean main() {
        return getClans();
    }
}
