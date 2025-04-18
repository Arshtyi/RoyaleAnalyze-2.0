package org.arshtyi.royaleanalyze2.royaleanalyze2.querylastactivetime;

import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreateClanInformation;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Urls;
import org.arshtyi.royaleanalyze2.royaleanalyze2.file.Formal;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;
import org.arshtyi.royaleanalyze2.royaleanalyze2.time.ConvertTime;

public class QueryLastActiveTime {
    private QueryLastActiveTime() {
        // Prevent instantiation of this utility class
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * This method is the main entry point for querying last active time
     * information.
     * It initializes the process and returns a boolean indicating success or
     * failure.
     * 
     * @return true if the query was successful, false otherwise
     */
    public static boolean main() {
        return queryLastActiveTime();
    }

    public static boolean queryLastActiveTime() {
        boolean flag = true;
        String logFilePath = Externs.getLogFilePath();
        String outputFilePath = Externs.getOutputInformationXlsxFilePath();
        String sheetName = Externs.getSheetName("LastActiveTime");
        Logger.log("INFO", "Querying last active time information...", QueryLastActiveTime.class, logFilePath);
        Formal.addSheet(outputFilePath, sheetName);
        int stRow = 1, stCol = 0, edRow = 1, edCol = 0;
        for (CreateClanInformation.Clan clan : Externs.getClans()) {
            Logger.log("INFO", "Querying last active time information for clan: " + clan.getName(),
                    QueryLastActiveTime.class, logFilePath);
            try {
                Logger.log("INFO", "< " + clan.getName() + " >Starting to construct the request URL...",
                        QueryLastActiveTime.class, logFilePath);
                String clanTag = clan.getTag();
                String url = Urls.getClanInformationUrl(clanTag) + "/members";
                Logger.log("INFO", "< " + clan.getName() + " >Request URL: " + url, QueryLastActiveTime.class,
                        logFilePath);
                CloseableHttpClient httpClient = Urls.createHttpClient();
                HttpGet httpGet = Urls.createHttpGet(url);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == Externs.getSuccessfulResponseStatusCode()) {
                    Logger.log("INFO", "< " + clan.getName() + " >Request successful!", QueryLastActiveTime.class,
                            logFilePath);
                    JSONObject root = new JSONObject(EntityUtils.toString(response.getEntity()));
                    JSONArray members = root.getJSONArray("items");
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject member = members.getJSONObject(i);
                        String name = member.getString("name");
                        int lastActiveTime = Math
                                .abs(ConvertTime.calculateTimeDifference(member.getString("lastSeen")));
                        List<Object> rowData = List.of(
                                clan.getName(), name, lastActiveTime);
                        Formal.addRow(outputFilePath, sheetName, rowData);
                        ++edRow;
                        Logger.log("INFO",
                                "< " + clan.getName() + " >Added row for member: " + name, QueryLastActiveTime.class,
                                logFilePath);
                    }
                } else {
                    Logger.log("ERROR", "Failed to query last active time information for clan: " + clan.getName(),
                            QueryLastActiveTime.class, logFilePath);
                    flag = false;
                }
            }

            catch (Exception e) {
                Logger.log("ERROR",
                        "Error occurred while querying last active time information for clan: " + clan.getName() +
                                " - " + e.getMessage(),
                        QueryLastActiveTime.class, logFilePath);
                flag = false;
            }
            if (edRow > stRow) {
                try {
                    Formal.sortSheetRows(outputFilePath, sheetName, stRow, edRow - 1, 2, true);
                    Logger.log("INFO", "Sorted sheet rows for last active time from " + stRow + " to " + (edRow - 1),
                            QueryLastActiveTime.class, logFilePath);

                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "Failed to sort sheet rows for last active time: " + e.getMessage(),
                            QueryLastActiveTime.class, logFilePath);
                }
                try {
                    Formal.markCellsByCondition(outputFilePath, sheetName, new int[] { 2 },
                            new int[] { 5 * 24 * 60 * 60 }, new String[] { ">" }, new String[] { "#FF0000" });
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "Failed to mark cells by condition for last active time: " + e.getMessage(),
                            QueryLastActiveTime.class, logFilePath);
                }
                try {
                    Formal.mergeRegion(outputFilePath, sheetName, stRow, edRow - 1, stCol, edCol);
                    Logger.log("INFO", "Merged region from " + stRow + " to " + (edRow - 1), QueryLastActiveTime.class,
                            logFilePath);
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "Failed to merge region for last active time: " + e.getMessage(),
                            QueryLastActiveTime.class, logFilePath);
                }
            }
            stRow = edRow;
        }
        try {
            Formal.convertTime(outputFilePath, sheetName, 2);
            Logger.log("INFO", "Converted time format for last active time", QueryLastActiveTime.class,
                    logFilePath);
        } catch (Exception e) {
            flag = false;
            Logger.log("ERROR", "Failed to convert time format for last active time: " + e.getMessage(),
                    QueryLastActiveTime.class, logFilePath);
        }
        return flag;
    }
}
