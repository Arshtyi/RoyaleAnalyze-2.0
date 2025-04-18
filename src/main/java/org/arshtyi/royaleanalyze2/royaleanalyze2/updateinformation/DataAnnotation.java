package org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;

/**
 * This class is used to annotate data in an Excel file.
 * It checks if the clan names in the file match the existing clans and
 * highlights them accordingly.
 * It uses Apache POI library to read and write Excel files.
 * It is part of the RoyaleAnalyze2 application.
 * It includes methods to open input and output streams, annotate the data, and
 * handle exceptions.
 * It also includes logging functionality to track the process.
 */
public class DataAnnotation {
    /**
     * This method is used to label clans in the Excel file.
     * It opens the input stream, reads the data, and annotates the cells based on
     * the clan names.
     * It then writes the annotated data back to the file and closes the output
     * stream.
     * 
     * @return true if the labeling was successful, false otherwise.
     */
    public static boolean labelClan() {
        String logFilePath = Externs.getLogFilePath();
        String playersXlsxFilePath = Externs.getPlayersInformationXlsxFilePath();
        try {
            FileInputStream file = new FileInputStream(new File(playersXlsxFilePath));
            Logger.log("INFO", "Opening input stream, preparing to annotate...", DataAnnotation.class,
                    logFilePath);
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            List<String> clanNames = new ArrayList<>();
            for (CreateClanInformation.Clan clan : Externs.getClans()) {
                clanNames.add(clan.getName());
            }
            CellStyle cellStyle1 = workbook.createCellStyle();
            cellStyle1.setFillForegroundColor(IndexedColors.RED.getIndex());
            cellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle1.setAlignment(HorizontalAlignment.CENTER);
            cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
            CellStyle cellStyle2 = workbook.createCellStyle();
            cellStyle2.setFillPattern(FillPatternType.NO_FILL);
            cellStyle2.setAlignment(HorizontalAlignment.CENTER);
            cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            for (int index = 1; index <= sheet.getLastRowNum(); index++) {
                String cellValue = sheet.getRow(index).getCell(0).getStringCellValue();
                if (!clanNames.contains(cellValue)) {
                    sheet.getRow(index).getCell(0).setCellStyle(cellStyle1);
                } else {
                    sheet.getRow(index).getCell(0).setCellStyle(cellStyle2);
                }
            }
            Logger.log("INFO", "Annotation completed, closing input stream...", DataAnnotation.class,
                    logFilePath);
            file.close();
            Logger.log("INFO", "Opening output stream, preparing to write...", DataAnnotation.class, logFilePath);
            FileOutputStream fileOut = new FileOutputStream(playersXlsxFilePath);
            workbook.write(fileOut);
            Logger.log("INFO", "Write completed, closing output stream...", DataAnnotation.class, logFilePath);
            fileOut.close();
            workbook.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}