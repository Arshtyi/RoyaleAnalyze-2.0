package org.arshtyi.royaleanalyze2.royaleanalyze2.file;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.time.ConvertTime;
import org.arshtyi.royaleanalyze2.royaleanalyze2.time.GetTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * This class is used to handle file operations, including deleting files,
 * adding sheets, adding rows, merging regions, and sorting rows.
 * It also includes methods for marking cells based on conditions and formatting
 * files.
 * It uses Apache POI library for Excel file manipulation.
 * It provides methods to delete files, add sheets, add rows, merge regions,
 * sort rows, and mark cells based on conditions.
 * It also includes methods to format files and check for temporary sheets.
 * It uses logging to track operations and errors.
 * It provides methods to handle file paths and directories used in the
 * application.
 * It includes methods to get the current directory, input/output directories,
 * log directory,
 * config directory, and various file paths.
 * It also includes methods to validate and normalize file paths.
 * It also includes methods to check the existence of directories and files.
 * It includes methods to check the log directory and file, and create them if
 * they do not exist.
 * It also includes methods to check the existence of directories and files.
 */
public class Formal {
    /**
     * This method is used to delete a file at the specified path.
     * It checks if the file exists and attempts to delete it.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the file to be deleted.
     * 
     * @return true if the file was deleted successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while deleting the file.
     */
    public static boolean deleteFile(String filePath) {
        String logFilePath = Externs.getLogFilePath();
        Logger.log("INFO", "Preparing to delete the file:" + filePath + "...", Formal.class, logFilePath);
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                Logger.log("INFO", "File deleted successfully...", Formal.class, logFilePath);
                return true;
            } else {
                Logger.log("ERROR", "Failed to delete the file...", Formal.class, logFilePath);
                return false;
            }
        } else {
            Logger.log("INFO", "The file does not exist...", Formal.class, logFilePath);
            return false;
        }
    }

    /**
     * This method is used to delete a sheet from the specified Excel file.
     * It checks if the file exists and if the specified sheet is present before
     * attempting to delete it.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file.
     * 
     * @param sheetName
     *            The name of the sheet to be deleted.
     * 
     * @return true if the sheet was deleted successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while deleting the sheet.
     */
    public static boolean deleteSheet(String filePath, String sheetName) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;
        String logFilePath = Externs.getLogFilePath();
        try {
            Logger.log("INFO", "Preparing to safely delete sheet: " + sheetName, Formal.class, logFilePath);
            File file = new File(filePath);

            // Check if the file exists
            if (!file.exists()) {
                Logger.log("INFO", "File does not exist: " + filePath, Formal.class, logFilePath);
                return false;
            }

            // Open the file and create a workbook
            fis = new FileInputStream(file);
            workbook = WorkbookFactory.create(fis);

            // Check if the sheet exists
            int sheetIndex = workbook.getSheetIndex(sheetName);
            if (sheetIndex == -1) {
                Logger.log("INFO", "Sheet " + sheetName + " does not exist.", Formal.class, logFilePath);
                return false;
            }

            // If only one sheet remains, create a temporary sheet to avoid deletion
            if (workbook.getNumberOfSheets() <= 1) {
                String tempSheetName = "TempSheet";
                Logger.log("INFO", "Only one sheet remains. Creating temporary sheet: " + tempSheetName,
                        Formal.class, logFilePath);
                workbook.createSheet(tempSheetName);
            }

            // Delete the specified sheet
            workbook.removeSheetAt(sheetIndex);
            Logger.log("INFO", "Sheet " + sheetName + " has been removed.", Formal.class, logFilePath);

            // Save the changes to the file
            fos = new FileOutputStream(filePath);
            workbook.write(fos);
            Logger.log("INFO", "Sheet deleted successfully and file saved.", Formal.class, logFilePath);
            fos.close();
            return true;
        } catch (Exception e) {
            Logger.log("ERROR", "Error occurred while safely deleting the sheet: " + e.getMessage(),
                    Formal.class, logFilePath);
            return false;
        } finally {
            // Ensure all resources are closed properly
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
                if (workbook != null)
                    workbook.close();
            } catch (Exception e) {
                Logger.log("ERROR", "Error closing resources: " + e.getMessage(), Formal.class, logFilePath);
            }
        }
    }

    /**
     * This method is used to format an Excel file at the specified path.
     * It checks for a temporary sheet and removes it if found.
     * It formats the columns and applies styles to the cells.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file to be formatted.
     * 
     * @return true if the file was formatted successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while formatting the file.
     */
    public static boolean formalFile(String filePath) {
        String logFilePath = Externs.getLogFilePath();
        Logger.log("INFO", "Preparing to format...", Formal.class, logFilePath);
        // Check for the existence of the File
        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                Workbook workbook = WorkbookFactory.create(fis);

                if (workbook.getNumberOfSheets() > 1) {
                    int tempSheetIndex = workbook.getSheetIndex("TempSheet");
                    if (tempSheetIndex != -1) {
                        Logger.log("INFO", "Multiple sheets detected. Removing TempSheet.", Formal.class,
                                logFilePath);
                        fis.close();
                        workbook.close();
                        deleteSheet(filePath, "TempSheet");
                    } else {
                        fis.close();
                        workbook.close();
                    }
                } else {
                    fis.close();
                    workbook.close();
                }
            }
        } catch (Exception e) {
            Logger.log("WARNING", "Error while checking for TempSheet: " + e.getMessage(), Formal.class,
                    logFilePath);
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;

        try {
            Logger.log("INFO", "Opening file for formatting: " + filePath, Formal.class, logFilePath);
            File file = new File(filePath);
            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Logger.log("INFO", "Processing sheet: " + sheet.getSheetName(), Formal.class, logFilePath);

                // Collect merged regions
                List<CellRangeAddress> mergedRegions = new ArrayList<>();
                for (int j = 0; j < sheet.getNumMergedRegions(); j++) {
                    mergedRegions.add(sheet.getMergedRegion(j));
                }

                // Create a cache for styles to avoid duplication
                Map<Integer, CellStyle> styleCache = new HashMap<>();

                // Calculate column width
                // If the first row exists
                if (sheet.getLastRowNum() >= 0 && sheet.getRow(0) != null) {
                    int lastCol = sheet.getRow(0).getLastCellNum();

                    // Calculate the width of each column
                    for (int colNum = 0; colNum < lastCol; colNum++) {
                        int maxLength = 0;

                        // Check the first row for merged regions
                        for (Row row : sheet) {
                            if (row == null)
                                continue;

                            Cell cell = row.getCell(colNum);
                            if (cell == null)
                                continue;

                            // Check if the cell is in any merged region
                            boolean inMergedRegion = false;
                            int colSpan = 1; // Default column span

                            for (CellRangeAddress region : mergedRegions) {
                                if (region.isInRange(cell.getRowIndex(), colNum)) {
                                    inMergedRegion = true;

                                    // If the cell is the top-left corner of the merged region
                                    if (region.getFirstRow() == cell.getRowIndex() &&
                                            region.getFirstColumn() == colNum) {
                                        // Calculate the column span
                                        colSpan = region.getLastColumn() - region.getFirstColumn() + 1;

                                        // Calculate the length of the content in the merged cell
                                        String content = cell.toString();
                                        int contentLength = content.getBytes().length;

                                        // Adjust length, considering the column span
                                        int adjustedLength = (int) Math.ceil((double) contentLength / colSpan);

                                        if (adjustedLength > maxLength) {
                                            maxLength = adjustedLength;
                                        }
                                    }
                                    break; // Found the region, no need to continue checking
                                }
                            }

                            // If the cell is not in a merged region, calculate its length
                            if (!inMergedRegion) {
                                int length = cell.toString().getBytes().length;
                                if (length > maxLength) {
                                    maxLength = length;
                                }
                            }
                        }

                        // Set minimum column width to ensure a certain width
                        maxLength = Math.max(maxLength, 5);

                        // Set column width, using a factor of 1.2 to provide extra space
                        sheet.setColumnWidth(colNum, (int) (maxLength * 1.1 * 256));
                    }
                }

                // Apply center alignment style to all cells, preserving original formatting
                for (Row row : sheet) {
                    if (row == null)
                        continue;

                    for (Cell cell : row) {
                        if (cell == null)
                            continue;

                        // Get original style index
                        CellStyle originalStyle = cell.getCellStyle();
                        int styleIndex = originalStyle.getIndex();

                        // Check if the style is already cached
                        if (!styleCache.containsKey(styleIndex)) {
                            // Create a new style based on the original style
                            CellStyle newStyle = workbook.createCellStyle();
                            newStyle.cloneStyleFrom(originalStyle);

                            // Modify only the alignment
                            newStyle.setAlignment(HorizontalAlignment.CENTER);
                            newStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);

                            // Cache the new style
                            styleCache.put(styleIndex, newStyle);
                        }

                        // Set the cell style to the cached style
                        cell.setCellStyle(styleCache.get(styleIndex));
                    }
                }
                // Freeze the first row in each sheet
                sheet.createFreezePane(0, 1);
                Logger.log("INFO", "First row frozen in sheet: " + sheet.getSheetName(), Formal.class, logFilePath);
                Logger.log("INFO", "Sheet " + sheet.getSheetName() + " has been formatted.", Formal.class,
                        logFilePath);
            }

            // Save the changes to the file
            fis.close();
            fos = new FileOutputStream(filePath);
            workbook.write(fos);
            Logger.log("INFO", "Formatting completed successfully.", Formal.class, logFilePath);
            return true;

        } catch (Exception e) {
            Logger.log("ERROR", "Error occurred while formatting the file: " + e.getMessage(), Formal.class,
                    logFilePath);
            return false;
        } finally {
            // Safely close all resources
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
                if (workbook != null)
                    workbook.close();
            } catch (Exception e) {
                Logger.log("ERROR", "Error closing resources: " + e.getMessage(), Formal.class, logFilePath);
            }
        }
    }

    /**
     * This method is used to add a new sheet to an existing Excel file.
     * It checks if the file exists, creates a new workbook if it doesn't,
     * deletes the specified sheet if it exists, and then creates a new sheet with
     * the specified name.
     * It also adds headers to the first row of the new sheet.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file.
     * 
     * @param sheetName
     *            The name of the new sheet to be added.
     * 
     * @return true if the sheet was added successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while adding the sheet.
     * 
     * @throws IllegalArgumentException
     *             If the sheet name is invalid or already
     *             exists.
     * 
     * @throws SecurityException
     *             If the file path is invalid or contains illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    public static boolean addSheet(String filePath, String sheetName) {
        String logFilePath = Externs.getLogFilePath();
        try {
            Logger.log("INFO", "Preparing to add sheet '" + sheetName + "' to file: " + filePath, Formal.class,
                    logFilePath);
            File file = new File(filePath);
            if (!file.exists()) {
                Logger.log("INFO", "File does not exist, creating new file: " + filePath, Formal.class,
                        logFilePath);
                Workbook newWorkbook = new XSSFWorkbook();
                try (FileOutputStream os = new FileOutputStream(file)) {
                    newWorkbook.write(os);
                }
                newWorkbook.close();
            }
            deleteSheet(filePath, sheetName);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.createSheet(sheetName);
            Row firstRow = sheet.createRow(0);
            int i;
            for (i = 0; i < Externs.getSheetFirstLineContents(sheetName).size(); i++) {
                Cell cell = firstRow.createCell(i);
                cell.setCellValue(Externs.getSheetFirstLineContents(sheetName).get(i));
            }
            Cell cell = firstRow.createCell(i);
            cell.setCellValue(GetTime.getCurrentTime());
            Logger.log("INFO", "Sheet '" + sheetName + "' has been created.", Formal.class, logFilePath);
            fileInputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();
            Logger.log("INFO", "Sheet '" + sheetName + "' has been added successfully.", Formal.class,
                    logFilePath);
            return true;
        } catch (Exception e) {
            Logger.log("ERROR", "Error occurred while adding sheet: " + e.getMessage(), Formal.class,
                    logFilePath);
            return false;
        }
    }

    /**
     * This method is used to add a new row to an existing sheet in an Excel file.
     * It checks if the file and sheet exist, creates a new row at the end of the
     * sheet,
     * and populates it with the provided data.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file.
     * 
     * @param sheetName
     *            The name of the sheet to which the row will be added.
     * 
     * @param rowData
     *            A list of objects representing the data to be added to the new
     *            row.
     * 
     * @return true if the row was added successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while adding the row.
     * 
     * @throws IllegalArgumentException
     *             If the sheet name is invalid or does not
     *             exist.
     * 
     * @throws SecurityException
     *             If the file path is invalid or contains illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    public static boolean addRow(String filePath, String sheetName, List<Object> rowData) {
        String logFilePath = Externs.getLogFilePath();
        try {
            Logger.log("INFO", "Adding row to sheet: " + sheetName, Formal.class, logFilePath);
            File file = new File(filePath);
            if (!file.exists()) {
                Logger.log("ERROR", "File does not exist: " + filePath, Formal.class, logFilePath);
                return false;
            }

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                Logger.log("ERROR", "Sheet does not exist: " + sheetName, Formal.class, logFilePath);
                fis.close();
                workbook.close();
                return false;
            }

            int rowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowNum);

            for (int i = 0; i < rowData.size(); i++) {
                Cell cell = row.createCell(i);
                Object value = rowData.get(i);

                // Set cell value based on the type of the object
                if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Double) {
                    cell.setCellValue((Double) value);
                } else if (value instanceof Long) {
                    cell.setCellValue((Long) value);
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                } else {
                    // Assume it's a string or null
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            fis.close();
            workbook.close();

            Logger.log("INFO", "Row added successfully", Formal.class, logFilePath);
            return true;
        } catch (Exception e) {
            Logger.log("ERROR", "Error adding row: " + e.getMessage(), Formal.class, logFilePath);
            return false;
        }
    }

    /**
     * This method is used to merge a region of cells in an Excel sheet.
     * It checks if the file and sheet exist, and merges the specified region.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file.
     * 
     * @param sheetName
     *            The name of the sheet in which to merge the region.
     * 
     * @param startRow
     *            The starting row index of the region to be merged.
     * 
     * @param endRow
     *            The ending row index of the region to be merged.
     * 
     * @param startCol
     *            The starting column index of the region to be merged.
     * 
     * @param endCol
     *            The ending column index of the region to be merged.
     * 
     * @return true if the region was merged successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while merging the region.
     * 
     * @throws IllegalArgumentException
     *             If the sheet name is invalid or does not
     *             exist.
     * 
     * @throws SecurityException
     *             If the file path is invalid or contains illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    public static boolean mergeRegion(String filePath, String sheetName, int startRow, int endRow, int startCol,
            int endCol) {
        String logFilePath = Externs.getLogFilePath();
        try {
            Logger.log("INFO", "Preparing to merge region in sheet: " + sheetName, Formal.class, logFilePath);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = workbook.getSheet(sheetName);
            sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
            fileInputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();
            Logger.log("INFO", "Region merged successfully in sheet: " + sheetName, Formal.class, logFilePath);
            return true;
        } catch (Exception e) {
            Logger.log("ERROR", "Error occurred while merging region: " + e.getMessage(), Formal.class,
                    logFilePath);
            return false;
        }
    }

    /**
     * This method is used to sort rows in an Excel sheet based on a specified
     * column.
     * It checks if the file and sheet exist, and sorts the specified range of rows.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file.
     * 
     * @param sheetName
     *            The name of the sheet in which to sort the rows.
     * 
     * @param startRow
     *            The starting row index of the range to be sorted.
     * 
     * @param endRow
     *            The ending row index of the range to be sorted.
     * 
     * @param columnIndex
     *            The index of the column based on which to sort the rows.
     * 
     * @param ascending
     *            true for ascending order, false for descending order.
     * 
     * @return true if the rows were sorted successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while sorting the rows.
     */
    public static boolean sortSheetRows(String filePath, String sheetName, int startRow, int endRow, int columnIndex,
            boolean ascending) {
        String logFilePath = Externs.getLogFilePath();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;

        try {
            Logger.log("INFO",
                    "Preparing to sort rows in sheet: " + sheetName + " from row " + startRow + " to " + endRow,
                    Formal.class, logFilePath);

            File file = new File(filePath);
            if (!file.exists()) {
                Logger.log("ERROR", "File does not exist: " + filePath, Formal.class, logFilePath);
                return false;
            }

            fis = new FileInputStream(file);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                Logger.log("ERROR", "Sheet does not exist: " + sheetName, Formal.class, logFilePath);
                return false;
            }

            // Validate row indices
            if (startRow < 0 || endRow >= sheet.getLastRowNum() + 1 || startRow > endRow) {
                Logger.log("ERROR", "Invalid row range: " + startRow + " to " + endRow, Formal.class,
                        logFilePath);
                return false;
            }

            // Bubble sort algorithm for swapping rows
            for (int i = startRow; i < endRow; i++) {
                for (int j = startRow; j < endRow - (i - startRow); j++) {
                    // Get integer values from the specified column
                    int value1 = getIntValue(sheet, j, columnIndex);
                    int value2 = getIntValue(sheet, j + 1, columnIndex);

                    // Determine if swap is needed based on sort order
                    boolean needSwap = ascending ? value1 > value2 : value1 < value2;

                    if (needSwap) {
                        swapRows(sheet, j, j + 1);
                    }
                }
            }

            // Save changes
            fis.close();
            fos = new FileOutputStream(file);
            workbook.write(fos);

            Logger.log("INFO", "Rows sorted successfully in sheet: " + sheetName, Formal.class, logFilePath);
            return true;

        } catch (Exception e) {
            Logger.log("ERROR", "Error sorting rows: " + e.getMessage(), Formal.class, logFilePath);
            return false;
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
                if (workbook != null)
                    workbook.close();
            } catch (Exception e) {
                Logger.log("ERROR", "Error closing resources: " + e.getMessage(), Formal.class, logFilePath);
            }
        }
    }

    /**
     * This method is used to get the integer value from a specified cell in a
     * sheet.
     * It checks if the cell exists and retrieves its value.
     * It logs a warning if the cell cannot be parsed as an integer.
     * 
     * @param sheet
     *            The sheet from which to get the cell value.
     * 
     * @param rowIndex
     *            The index of the row containing the cell.
     * 
     * @param columnIndex
     *            The index of the column containing the cell.
     * 
     * @return The integer value of the cell, or 0 if it cannot be parsed.
     * 
     * @throws IllegalArgumentException
     *             If the row or column index is invalid.
     * 
     * @throws SecurityException
     *             If the sheet is invalid or contains illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    private static int getIntValue(Sheet sheet, int rowIndex, int columnIndex) {
        String logFilePath = Externs.getLogFilePath();
        Row row = sheet.getRow(rowIndex);
        if (row == null)
            return 0;

        Cell cell = row.getCell(columnIndex);
        if (cell == null)
            return 0;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else {
                return Integer.parseInt(cell.toString().trim());
            }
        } catch (NumberFormatException e) {
            Logger.log("WARNING", "Error parsing integer at row " + rowIndex + ", column " + columnIndex,
                    Formal.class, logFilePath);
            return 0;
        }
    }

    /**
     * This method is used to swap two rows in a sheet.
     * It checks if the rows exist and swaps their content.
     * It logs the result of the operation.
     * 
     * @param sheet
     *            The sheet in which to swap the rows.
     * 
     * @param row1Index
     *            The index of the first row to be swapped.
     * 
     * @param row2Index
     *            The index of the second row to be swapped.
     * 
     * @throws IllegalArgumentException
     *             If the row indices are invalid.
     * 
     * @throws SecurityException
     *             If the sheet is invalid or contains illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    private static void swapRows(Sheet sheet, int row1Index, int row2Index) {
        Row row1 = sheet.getRow(row1Index);
        Row row2 = sheet.getRow(row2Index);

        if (row1 == null && row2 == null)
            return;

        // Create rows if they don't exist
        if (row1 == null)
            row1 = sheet.createRow(row1Index);
        if (row2 == null)
            row2 = sheet.createRow(row2Index);

        // Find the maximum column index
        int maxCols = Math.max(
                row1.getLastCellNum() == -1 ? 0 : row1.getLastCellNum(),
                row2.getLastCellNum() == -1 ? 0 : row2.getLastCellNum());

        // Store row1 data temporarily
        Object[] tempValues = new Object[maxCols];
        CellStyle[] tempStyles = new CellStyle[maxCols];
        CellType[] tempTypes = new CellType[maxCols];

        for (int i = 0; i < maxCols; i++) {
            Cell cell = row1.getCell(i);
            if (cell != null) {
                tempTypes[i] = cell.getCellType();
                tempStyles[i] = cell.getCellStyle();

                switch (tempTypes[i]) {
                    case NUMERIC:
                        tempValues[i] = cell.getNumericCellValue();
                        break;
                    case STRING:
                        tempValues[i] = cell.getStringCellValue();
                        break;
                    case BOOLEAN:
                        tempValues[i] = cell.getBooleanCellValue();
                        break;
                    case FORMULA:
                        tempValues[i] = cell.getCellFormula();
                        break;
                    default:
                        tempValues[i] = cell.toString();
                }
            }
        }

        // Copy row2 to row1
        for (int i = 0; i < maxCols; i++) {
            Cell sourceCell = row2.getCell(i);
            if (sourceCell != null) {
                Cell targetCell = row1.getCell(i) != null ? row1.getCell(i) : row1.createCell(i);
                copyCell(sourceCell, targetCell);
            } else if (row1.getCell(i) != null) {
                row1.removeCell(row1.getCell(i));
            }
        }

        // Copy stored data to row2
        for (int i = 0; i < maxCols; i++) {
            if (tempValues[i] != null) {
                Cell targetCell = row2.getCell(i) != null ? row2.getCell(i) : row2.createCell(i);
                targetCell.setCellStyle(tempStyles[i]);

                switch (tempTypes[i]) {
                    case NUMERIC:
                        targetCell.setCellValue((Double) tempValues[i]);
                        break;
                    case STRING:
                        targetCell.setCellValue((String) tempValues[i]);
                        break;
                    case BOOLEAN:
                        targetCell.setCellValue((Boolean) tempValues[i]);
                        break;
                    case FORMULA:
                        targetCell.setCellFormula((String) tempValues[i]);
                        break;
                    default:
                        targetCell.setCellValue(tempValues[i].toString());
                }
            } else if (row2.getCell(i) != null) {
                row2.removeCell(row2.getCell(i));
            }
        }
    }

    /**
     * This method is used to copy a cell's content and style from one cell to
     * another.
     * It checks the cell type and copies the appropriate value and style.
     * 
     * @param source
     *            The source cell from which to copy the content and style.
     * 
     * @param target
     *            The target cell to which the content and style will be copied.
     * 
     * @throws IllegalArgumentException
     *             If the source or target cell is null.
     * 
     * @throws SecurityException
     *             If the cells are invalid or contain illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    private static void copyCell(Cell source, Cell target) {
        target.setCellStyle(source.getCellStyle());

        switch (source.getCellType()) {
            case NUMERIC:
                target.setCellValue(source.getNumericCellValue());
                break;
            case STRING:
                target.setCellValue(source.getStringCellValue());
                break;
            case BOOLEAN:
                target.setCellValue(source.getBooleanCellValue());
                break;
            case FORMULA:
                target.setCellFormula(source.getCellFormula());
                break;
            case BLANK:
                break;
            default:
                target.setCellValue(source.toString());
        }
    }

    /**
     * This method is used to mark cells in an Excel sheet based on specified
     * conditions.
     * It checks if the file and sheet exist, and applies styles to cells that meet
     * the conditions.
     * It logs the result of the operation.
     * 
     * @param filePath
     *            The path to the Excel file.
     * 
     * @param sheetName
     *            The name of the sheet in which to mark the cells.
     * 
     * @param columnIndices
     *            An array of column indices to check for conditions.
     * 
     * @param standards
     *            An array of standard values for comparison.
     * 
     * @param comparisonTypes
     *            An array of comparison types (e.g., "equals",
     *            "greater", "less").
     * 
     * @param hexColors
     *            An array of hex color codes for marking cells.
     * 
     * @return true if the cells were marked successfully, false otherwise.
     * 
     * @throws IOException
     *             If an I/O error occurs while marking the cells.
     * 
     * @throws IllegalArgumentException
     *             If the sheet name is invalid or does not
     *             exist.
     * 
     * @throws SecurityException
     *             If the file path is invalid or contains illegal
     *             characters.
     * 
     * @throws Exception
     *             If any other error occurs during the operation.
     */
    public static boolean markCellsByCondition(String filePath, String sheetName, int[] columnIndices,
            int[] standards, String[] comparisonTypes, String[] hexColors) {
        String logFilePath = Externs.getLogFilePath();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;

        try {
            Logger.log("INFO", "Preparing to mark cells in sheet: " + sheetName, Formal.class, logFilePath);

            // Validate input parameters
            if (columnIndices.length != standards.length ||
                    standards.length != comparisonTypes.length ||
                    comparisonTypes.length != hexColors.length) {
                Logger.log("ERROR", "Input arrays must have the same length", Formal.class, logFilePath);
                return false;
            }

            File file = new File(filePath);
            if (!file.exists()) {
                Logger.log("ERROR", "File does not exist: " + filePath, Formal.class, logFilePath);
                return false;
            }

            fis = new FileInputStream(file);
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                Logger.log("ERROR", "Sheet does not exist: " + sheetName, Formal.class, logFilePath);
                return false;
            }

            // Style cache to avoid creating too many styles
            Map<String, CellStyle> styleCache = new HashMap<>();

            // Process each row
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Start from 1 to skip header
                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    continue;

                // Check each specified column
                for (int i = 0; i < columnIndices.length; i++) {
                    int colIndex = columnIndices[i];
                    Cell cell = row.getCell(colIndex);
                    if (cell == null)
                        continue;

                    // Get integer value from cell
                    int cellValue;
                    try {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            cellValue = (int) cell.getNumericCellValue();
                        } else {
                            cellValue = Integer.parseInt(cell.toString().trim());
                        }
                    } catch (NumberFormatException e) {
                        continue; // Skip cells that can't be parsed as integers
                    }

                    // Apply condition check
                    boolean conditionMet = false;
                    switch (comparisonTypes[i].toLowerCase()) {
                        case "equals":
                        case "equal":
                        case "=":
                        case "==":
                            conditionMet = (cellValue == standards[i]);
                            break;
                        case "greater":
                        case "greater_than":
                        case ">":
                            conditionMet = (cellValue > standards[i]);
                            break;
                        case "less":
                        case "less_than":
                        case "<":
                            conditionMet = (cellValue < standards[i]);
                            break;
                        default:
                            Logger.log("WARNING", "Unknown comparison type: " + comparisonTypes[i],
                                    Formal.class, logFilePath);
                            continue;
                    }

                    // Apply style if condition is met
                    if (conditionMet) {
                        CellStyle originalStyle = cell.getCellStyle();
                        String hexColor = hexColors[i].replace("#", ""); // Remove # if present

                        // Create a unique key for the style combination
                        String styleKey = originalStyle.getIndex() + "_" + hexColor;

                        CellStyle newStyle;
                        if (styleCache.containsKey(styleKey)) {
                            newStyle = styleCache.get(styleKey);
                        } else {
                            // Create a new style that clones the original
                            newStyle = workbook.createCellStyle();
                            newStyle.cloneStyleFrom(originalStyle);

                            // Set fill pattern and color
                            newStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                            if (workbook instanceof XSSFWorkbook) {
                                // Use XSSFColor for .xlsx files
                                XSSFWorkbook xssfWorkbook = (XSSFWorkbook) workbook;
                                // Parse hex color to RGB
                                int r = Integer.parseInt(hexColor.substring(0, 2), 16);
                                int g = Integer.parseInt(hexColor.substring(2, 4), 16);
                                int b = Integer.parseInt(hexColor.substring(4, 6), 16);

                                // Create XSSFColor and set it as the fill foreground color
                                XSSFColor xssfColor = new XSSFColor(new java.awt.Color(r, g, b),
                                        xssfWorkbook.getStylesSource().getIndexedColors());
                                ((XSSFCellStyle) newStyle).setFillForegroundColor(xssfColor);

                                // Set fill pattern
                                newStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                                // Log the applied color
                                Logger.log("INFO", "Applied color #" + hexColor + " to cell at row: " +
                                        cell.getRowIndex() + ", column: " + cell.getColumnIndex(),
                                        Formal.class, logFilePath);
                            } else {
                                Logger.log("WARNING", "Custom RGB colors only supported for .xlsx files",
                                        Formal.class, logFilePath);
                                newStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                            }

                            styleCache.put(styleKey, newStyle);
                        }

                        cell.setCellStyle(newStyle);
                    }
                }
            }

            // Save changes
            fis.close();
            fos = new FileOutputStream(file);
            workbook.write(fos);

            Logger.log("INFO", "Cells marked successfully in sheet: " + sheetName, Formal.class, logFilePath);
            return true;

        } catch (Exception e) {
            Logger.log("ERROR", "Error marking cells: " + e.getMessage(), Formal.class, logFilePath);
            return false;
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
                if (workbook != null)
                    workbook.close();
            } catch (Exception e) {
                Logger.log("ERROR", "Error closing resources: " + e.getMessage(), Formal.class, logFilePath);
            }
        }
    }

    /**
     * This method is used to convert time values in a specified column of an
     * Excel sheet from numeric format to string format.
     * 
     * @param filePath
     * @param sheetName
     * @param column
     *            index
     * @return void
     * @throws Exception
     */
    public static void convertTime(String filePath, String sheetName, int col) {
        String logFilePath = Externs.getLogFilePath();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        Workbook workbook = null;
        try {
            Logger.log("INFO", "Converting time in sheet: " + sheetName, Formal.class, logFilePath);
            fis = new FileInputStream(new File(filePath));
            workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                Logger.log("ERROR", "Sheet does not exist: " + sheetName, Formal.class, logFilePath);
                return;
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { // Start from 1 to skip header
                Row row = sheet.getRow(rowIndex);
                if (row == null)
                    continue;

                Cell cell = row.getCell(col);
                if (cell == null)
                    continue;
                int timeValue = (int) cell.getNumericCellValue();
                String timeString = ConvertTime.convertTime(timeValue);
                cell.setCellValue(timeString);
            }
            fos = new FileOutputStream(filePath);
            workbook.write(fos);
            Logger.log("INFO", "Time converted successfully in sheet: " + sheetName, Formal.class, logFilePath);
        } catch (Exception e) {
            Logger.log("ERROR", "Error converting time: " + e.getMessage(), Formal.class, logFilePath);
        } finally {
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
                if (workbook != null)
                    workbook.close();
            } catch (Exception e) {
                Logger.log("ERROR", "Error closing resources: " + e.getMessage(), Formal.class, logFilePath);
            }
        }
    }
}
