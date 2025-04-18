package org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation;

import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Urls;

/**
 * This class is responsible for updating clan and player information.
 * It retrieves the latest information from the server and writes it to an Excel
 * file.
 * It also handles logging and error handling.
 * It is part of the RoyaleAnalyze2 application.
 */
public class UpdateInformation {
    /**
     * This method is the main entry point for updating information.
     * It calls the updateClans and updatePlayers methods to fetch and write the
     * data.
     * 
     * @return true if the update was successful, false otherwise.
     */
    public static boolean main() {
        return updateClans() && updatePlayers();
    }

    /**
     * This method updates the clan information by sending requests to the server.
     * It retrieves the clan names and writes them to an Excel file.
     * 
     * @return true if the update was successful, false otherwise.
     */
    public static boolean updateClans() {
        boolean flag = true;
        String logFilePath = Externs.getLogFilePath();
        String clansXlsxFilePath = Externs.getClansInformationXlsxFilePath();
        Logger.log("INFO", "Preparing to update clan information...", UpdateInformation.class, logFilePath);
        List<String> clanNames = new ArrayList<>();
        for (CreateClanInformation.Clan clan : Externs.getClans()) {
            Logger.log("INFO", "< " + clan.getName() + " >Starting to fetch information...", UpdateInformation.class,
                    logFilePath);
            try {
                Logger.log("INFO", "< " + clan.getName() + " >Starting to construct request...",
                        UpdateInformation.class, logFilePath);
                String clanTag = clan.getTag();
                String url = Urls.getClanInformationUrl(clanTag);
                CloseableHttpClient httpClient = Urls.createHttpClient();
                HttpGet httpGet = Urls.createHttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == Externs.getSuccessfulResponseStatusCode()) {
                    Logger.log("INFO", "< " + clan.getName() + " >Information retrieved successfully...",
                            UpdateInformation.class, logFilePath);
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String name = jsonObject.getString("name");
                    clanNames.add(name);
                } else {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    Logger.log("ERROR", "Response information: " + responseBody, UpdateInformation.class,
                            logFilePath);
                    flag = false;
                }
                httpClient.close();
            } catch (IOException e) {
                Logger.log("ERROR", "Network exception: " + e.getMessage(), UpdateInformation.class, logFilePath);
                return false;
            } catch (JSONException e) {
                Logger.log("ERROR", "JSON parsing exception: " + e.getMessage(), UpdateInformation.class, logFilePath);
                return false;
            } catch (Exception e) {
                Logger.log("ERROR", "Unexpected exception: " + e.getMessage(), UpdateInformation.class, logFilePath);
                return false;
            }
        }
        if (flag) {
            Logger.log("INFO", "Clan information retrieved successfully, starting to write to file: "
                    + clansXlsxFilePath + "...", UpdateInformation.class, logFilePath);
            try {
                FileInputStream fileInputStream = new FileInputStream(clansXlsxFilePath);
                Logger.log("INFO", "Input stream opened...", UpdateInformation.class, logFilePath);
                Workbook workbook = new XSSFWorkbook(fileInputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int rowIndex = 1;
                for (String clanName : clanNames) {
                    Row row = sheet.getRow(rowIndex++);
                    Cell cell = row.getCell(0);
                    cell.setCellValue(clanName);
                }
                fileInputStream.close();
                FileOutputStream fileOut = new FileOutputStream(clansXlsxFilePath);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
                Logger.log("INFO",
                        "File written to: " + clansXlsxFilePath + ", preparing to update data...",
                        UpdateInformation.class, logFilePath);
                if (CreateClanInformation.getClans()) {
                    Logger.log("INFO", "Clan information updated successfully...", UpdateInformation.class,
                            logFilePath);
                } else {
                    Logger.log("ERROR", "Clan information update failed, using existing data...",
                            UpdateInformation.class, logFilePath);
                }
            } catch (IOException e) {
                Logger.log("ERROR", "File writing failed...", UpdateInformation.class, logFilePath);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * This method updates the player information by sending requests to the server.
     * It retrieves the player names and clan names and writes them to an Excel
     * file.
     * 
     * @return true if the update was successful, false otherwise.
     */
    public static boolean updatePlayers() {
        String logFilePath = Externs.getLogFilePath();
        String playersXlsxFilePath = Externs.getPlayersInformationXlsxFilePath();
        boolean flag = true;
        Logger.log("INFO", "Preparing to update player information...", UpdateInformation.class, logFilePath);
        List<JSONObject> playerDetails = new ArrayList<>();
        for (CreatePlayerInformation.Player player : Externs.getPlayers()) {
            try {
                String playerTag = player.getTag();
                String url = Urls.getPlayerInformationUrl(playerTag);
                CloseableHttpClient httpClient = Urls.createHttpClient();
                HttpGet httpGet = Urls.createHttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == Externs.getSuccessfulResponseStatusCode()) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String name = jsonObject.getString("name");
                    String clan = jsonObject.has("clan") ? jsonObject.getJSONObject("clan").getString("name") : "无";
                    JSONObject playerDetail = new JSONObject();
                    playerDetail.put("name", name);
                    playerDetail.put("clan", clan);
                    playerDetails.add(playerDetail);
                } else {
                    Logger.log("ERROR", "< " + player.getName() + " >Failed to retrieve information...",
                            UpdateInformation.class, logFilePath);
                    flag = false;
                }
                httpClient.close();
            } catch (IOException e) {
                Logger.log("ERROR", "Network exception: " + e.getMessage(), UpdateInformation.class, logFilePath);
                return false;
            } catch (JSONException e) {
                Logger.log("ERROR", "JSON parsing exception: " + e.getMessage(), UpdateInformation.class, logFilePath);
                return false;
            } catch (Exception e) {
                Logger.log("ERROR", "Unexpected exception: " + e.getMessage(), UpdateInformation.class, logFilePath);
                return false;
            }
        }
        if (flag) {
            Logger.log("INFO", "Player information retrieved successfully, starting to write to file: "
                    + playersXlsxFilePath + "...", UpdateInformation.class, logFilePath);
            try {
                FileInputStream fileInputStream = new FileInputStream(playersXlsxFilePath);
                Workbook workbook = new XSSFWorkbook(fileInputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int rowIndex = 1;
                for (JSONObject playerDetail : playerDetails) {
                    Row row = sheet.getRow(rowIndex++);
                    Cell cellName = row.getCell(1);
                    Cell cellClan = row.getCell(0);
                    cellName.setCellValue(playerDetail.getString("name"));
                    cellClan.setCellValue(playerDetail.getString("clan"));
                }
                fileInputStream.close();
                FileOutputStream fileOut = new FileOutputStream(playersXlsxFilePath);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
                if (CreatePlayerInformation.getPlayers()) {
                    Logger.log("INFO", "Player information updated successfully, preparing to label...",
                            UpdateInformation.class, logFilePath);
                    return DataAnnotation.labelClan();
                } else {
                    Logger.log("ERROR", "Player information update failed, using existing data...",
                            UpdateInformation.class, logFilePath);
                    flag = false;
                }
            } catch (IOException e) {
                Logger.log("ERROR", "File writing failed...", UpdateInformation.class, logFilePath);
                flag = false;
            }
        }
        return flag;
    }
}
