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
import org.arshtyi.royaleanalyze2.royaleanalyze2.user.Judge;

/**
 * This class is used to create player information by reading from an Excel
 * file.
 * It retrieves the player names, tags, clans, and QQ numbers from the file and
 * stores them in a list of Player objects.
 * It also includes methods to get player information and to handle logging.
 * It is part of the RoyaleAnalyze2 application.
 * It is annotated with @EnableAutoConfiguration to enable Spring Boot's
 * auto-configuration feature.
 * This allows the application to automatically configure itself based on the
 * dependencies present in the classpath.
 */
@EnableAutoConfiguration
public class CreatePlayerInformation {
    /**
     * This class represents a player with a name, tag, clan, and QQ number.
     * It includes getters and setters for these attributes.
     */
    public static class Player {
        private String name;
        private String tag;
        private String clan;
        private String qq;

        /**
         * This constructor initializes a Player object with the given name, tag, clan,
         * and QQ number.
         */
        public Player(String name, String tag, String clan, String qq) {
            this.name = name;
            this.tag = tag;
            this.clan = clan;
            this.qq = qq;
        }

        /**
         * This method returns the name of the player.
         * 
         * @return the name of the player
         * 
         */
        public String getName() {
            return name;
        }

        /**
         * This method sets the name of the player.
         * 
         * @param name
         *            the name of the player
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * This method returns the tag of the player.
         * 
         * @return the tag of the player
         */
        public String getTag() {
            return tag;
        }

        /**
         * This method sets the tag of the player.
         * 
         * @param tag
         *            the tag of the player
         */
        public void setTag(String tag) {
            this.tag = tag;
        }

        /**
         * This method returns the clan of the player.
         * 
         * @return the clan of the player
         */
        public String getClan() {
            return clan;
        }

        /**
         * This method sets the clan of the player.
         * 
         * @param clan
         *            the clan of the player
         */
        public void setClan(String clan) {
            this.clan = clan;
        }

        /**
         * This method returns the QQ number of the player.
         * 
         * @return the QQ number of the player
         */
        public String getQQ() {
            return qq;
        }

        /**
         * This method sets the QQ number of the player.
         * 
         * @param qq
         *            the QQ number of the player
         */
        public void setQQ(String qq) {
            this.qq = qq;
        }
    }

    /**
     * This method retrieves player information from an Excel file and writes it to
     * a JSON file.
     * It uses the Apache POI library to read the Excel file and the Jackson library
     * to write the JSON file.
     * It also includes error handling and logging.
     * 
     * @return true if the player information is successfully retrieved and written
     *         to a file, false otherwise
     */
    public static boolean main() {
        return getPlayers();
    }

    /**
     * This method retrieves player information from an Excel file and writes it to
     * a JSON file.
     * It uses the Apache POI library to read the Excel file and the Jackson library
     * to write the JSON file.
     * It also includes error handling and logging.
     * 
     * @return true if the player information is successfully retrieved and written
     *         to a file, false otherwise
     */
    public static boolean getPlayers() {
        String logFilePath = Externs.getLogFilePath();
        String playersXlsxFilePath = Externs.getPlayersInformationXlsxFilePath();
        String playersJsonFilePath = Externs.getPlayersInformationJsonFilePath();
        Logger.log("INFO", "Preparing to get player information...", CreatePlayerInformation.class, logFilePath);
        File file = new File(playersXlsxFilePath);
        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Player> players = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    String busyOrNot = row.getCell(4).getStringCellValue();
                    if (Judge.judgeTwo(busyOrNot))
                        continue;
                    String clan = row.getCell(0).getStringCellValue();
                    String name;
                    if (row.getCell(1).getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
                        name = String.valueOf((int) row.getCell(2).getNumericCellValue());
                    } else {
                        name = row.getCell(1).getStringCellValue();
                    }
                    String qq;
                    if (row.getCell(2).getCellType() == org.apache.poi.ss.usermodel.CellType.NUMERIC) {
                        qq = String.valueOf((int) row.getCell(2).getNumericCellValue());
                    } else {
                        qq = row.getCell(2).getStringCellValue();
                    }
                    String tag = row.getCell(3).getStringCellValue().substring(1);
                    players.add(new Player(name, tag, clan, qq));
                }
            }
            Externs.setPlayers(players);
            if (Externs.getPlayers().isEmpty()) {
                Logger.log("INFO", "Player information is empty, the program will not be able to filter...",
                        CreatePlayerInformation.class, logFilePath);
                Externs.setPlayersInformationExists(false);
                return false;
            }
            Logger.log("INFO", "Player information obtained, preparing to write to file...",
                    CreatePlayerInformation.class, logFilePath);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Logger.log("INFO", "Player information written to: " + playersJsonFilePath,
                        CreatePlayerInformation.class,
                        logFilePath);
                objectMapper.writeValue(new File(playersJsonFilePath), players);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log("ERROR", "Failed to write player information, the program is preparing to exit...",
                        CreatePlayerInformation.class, logFilePath);
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("ERROR", "Failed to get player information, the program is preparing to exit...",
                    CreatePlayerInformation.class, logFilePath);
            return false;
        }
    }
}
