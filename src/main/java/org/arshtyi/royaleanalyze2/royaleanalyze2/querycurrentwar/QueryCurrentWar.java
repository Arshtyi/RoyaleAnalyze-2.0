package org.arshtyi.royaleanalyze2.royaleanalyze2.querycurrentwar;

import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Urls;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreateClanInformation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.arshtyi.royaleanalyze2.royaleanalyze2.file.Formal;

import java.io.IOException;
import java.util.List;

/**
 * This class is used to query the current war information of clans.
 * It constructs a request URL for each clan, sends a request to the server,
 * and processes the response to extract relevant information.
 * It also handles errors and logs the process.
 * It is part of the RoyaleAnalyze2 application.
 */
public class QueryCurrentWar {
    private QueryCurrentWar() {
        // Prevent instantiation of this utility class
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * This method is the main entry point for querying current war information.
     * It initializes the process and returns a boolean indicating success or
     * failure.
     * 
     * @return true if the query was successful, false otherwise.
     */
    public static boolean main() {
        return queryCurrentWar();
    }

    /**
     * This method queries the current war information for each clan.
     * It constructs the request URL, sends the request, and processes the response.
     * It also handles errors and logs the process.
     * 
     * @return true if the query was successful, false otherwise.
     * 
     * @throws Exception
     *             if an error occurs during the query process.
     * 
     * @throws IOException
     *             if an I/O error occurs while processing the response.
     */
    private static boolean queryCurrentWar() {
        String outputFilePath = Externs.getOutputInformationXlsxFilePath();
        String logFilePath = Externs.getLogFilePath();
        String sheetName = Externs.getSheetName("CurrentWar");
        boolean flag = true;
        Logger.log("INFO", "Querying current war information...", QueryCurrentWar.class, logFilePath);
        Formal.addSheet(outputFilePath, sheetName);
        int stRow = 1, stCol = 0, edRow = 1, edCol = 0;
        for (CreateClanInformation.Clan clan : Externs.getClans()) {
            Logger.log("INFO", "Processing clan: " + clan.getName(), QueryCurrentWar.class, logFilePath);
            try {
                Logger.log("INFO", "< " + clan.getName() + ">Starting to construct request URL...",
                        QueryCurrentWar.class, logFilePath);
                String clanTag = clan.getTag();
                String url = Urls.getFullUrl(Urls.getClanInformationUrl(clanTag), "/currentriverrace");
                Logger.log("INFO", "< " + clan.getName() + ">Request URL: " + url, QueryCurrentWar.class,
                        logFilePath);
                CloseableHttpClient httpClient = Urls.createHttpClient();
                HttpGet httpGet = Urls.createHttpGet(url);
                Logger.log("INFO", "< " + clan.getName() + ">Sending request...", QueryCurrentWar.class,
                        logFilePath);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == Externs.getSuccessfulResponseStatusCode()) {
                    Logger.log("INFO", "< " + clan.getName() + ">Request successful!", QueryCurrentWar.class,
                            logFilePath);
                    JSONObject root = new JSONObject(EntityUtils.toString(response.getEntity()));
                    JSONObject currentWar = root.getJSONObject("clan");
                    JSONArray participants = currentWar.getJSONArray("participants");
                    for (int i = 0; i < participants.length(); i++) {
                        JSONObject participant = participants.getJSONObject(i);
                        String palyerName = participant.getString("name");
                        int decksUsedToday = participant.getInt("decksUsedToday");
                        int decksUsed = participant.getInt("decksUsed");
                        int boatAttacks = participant.getInt("boatAttacks");
                        int fame = participant.getInt("fame");
                        List<Object> rowData = List.of(
                                clan.getName(),
                                palyerName,
                                decksUsedToday,
                                decksUsed,
                                boatAttacks,
                                fame);
                        Formal.addRow(outputFilePath, sheetName, rowData);
                        ++edRow;
                        Logger.log("INFO", "Added player " + palyerName + " from " + clan.getName() + " to sheet",
                                QueryCurrentWar.class, logFilePath);
                    }
                } else {
                    Logger.log("ERROR", "< " + clan.getName() + ">Request failed with status: " +
                            response.getStatusLine().getStatusCode(), QueryCurrentWar.class, logFilePath);
                    flag = false;
                }
            } catch (Exception e) {
                Logger.log("ERROR", "< " + clan.getName() + ">Error occurred while processing: " + e.getMessage(),
                        QueryCurrentWar.class, logFilePath);
                System.out.println(e.getMessage());
                flag = false;
            }
            if (edRow > stRow) {
                try {
                    Formal.sortSheetRows(outputFilePath, sheetName, stRow, edRow - 1, 5, false);

                    Logger.log("INFO", "< " + clan.getName() + ">Sorted rows for fame", QueryCurrentWar.class,
                            logFilePath);
                } catch (Exception e) {
                    Logger.log("ERROR", "< " + clan.getName() + ">Failed to sort rows: " + e.getMessage(),
                            QueryCurrentWar.class, logFilePath);
                    flag = false;
                }
                try {
                    Formal.markCellsByCondition(outputFilePath, sheetName, new int[] { 5 },
                            new int[] { 0 }, new String[] { "==" }, new String[] { "#FF0000" });
                } catch (Exception e) {
                    Logger.log("ERROR", "< " + clan.getName() + ">Failed to mark cells: " + e.getMessage(),
                            QueryCurrentWar.class, logFilePath);
                    flag = false;
                }
                try {
                    Formal.mergeRegion(outputFilePath, sheetName, stRow, edRow - 1, stCol, edCol);
                    Logger.log("INFO", "< " + clan.getName() + ">Merged cells for clan", QueryCurrentWar.class,
                            logFilePath);
                } catch (Exception e) {
                    Logger.log("ERROR", "< " + clan.getName() + ">Failed to merge cells: " + e.getMessage(),
                            QueryCurrentWar.class, logFilePath);
                    flag = false;
                }
            }
            stRow = edRow;
        }
        return flag;
    }

}