package org.arshtyi.royaleanalyze2.royaleanalyze2.querylastmonthcontribution;

import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreateClanInformation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Urls;
import org.arshtyi.royaleanalyze2.royaleanalyze2.file.Formal;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class QueryLastMonthContribution {
    private QueryLastMonthContribution() {
        // Prevent instantiation of this utility class
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * This method is the main entry point for querying last month contribution
     * 
     * @return true if the query was successful, false otherwise
     */
    public static boolean main() {
        return queryLastMonthContribution();
    }

    /**
     * This method queries the last month contribution information for each clan.
     * It constructs the request URL, sends the request, and processes the response.
     * 
     * @param void
     * @return true if the query was successful, false otherwise
     * @throws Exception
     *             if an error occurs during the query process
     */
    public static boolean queryLastMonthContribution() {
        boolean flag = true;
        String logFilePath = Externs.getLogFilePath();
        String outputFilePath = Externs.getOutputInformationXlsxFilePath();
        String sheetName = Externs.getSheetName("LastMonthContribution");
        int responseLimit = 4;
        Logger.log("INFO", "Querying last month contribution information...", QueryLastMonthContribution.class,
                logFilePath);
        Formal.addSheet(outputFilePath, sheetName);
        int stRow = 1, stCol = 0, edRow = 1, edCol = 0;
        for (CreateClanInformation.Clan clan : Externs.getClans()) {
            Logger.log("INFO", "Querying last month contribution information for clan: " + clan.getName(),
                    QueryLastMonthContribution.class, logFilePath);
            try {
                String clanTag = clan.getTag();
                String url = Urls.getFullUrl(Urls.getClanInformationUrl(clanTag),
                        "/riverracelog?limit=" + responseLimit);
                Logger.log("INFO", "< " + clan.getName() + ">Request URL: " + url, QueryLastMonthContribution.class,
                        logFilePath);
                CloseableHttpClient httpClient = Urls.createHttpClient();
                HttpGet httpGet = Urls.createHttpGet(url);
                Logger.log("INFO", "< " + clan.getName() + ">Sending request...", QueryLastMonthContribution.class,
                        logFilePath);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == Externs.getSuccessfulResponseStatusCode()) {
                    Logger.log("INFO", "< " + clan.getName() + ">Request successful!", QueryLastMonthContribution.class,
                            logFilePath);
                    JSONObject root = new JSONObject(EntityUtils.toString(response.getEntity()));
                    JSONArray items = root.getJSONArray("items");
                    Map<String, List<Object>> rowDatas = new HashMap<>();
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        JSONArray clans = item.getJSONArray("standings");
                        for (int j = 0; j < clans.length(); j++) {
                            JSONObject clanInfo = clans.getJSONObject(j);
                            String CurrentClanTag = clanInfo.getJSONObject("clan").getString("tag");
                            if (CurrentClanTag.equals("#" + clanTag)) {
                                JSONArray participants = clanInfo.getJSONObject("clan").getJSONArray("participants");
                                for (int k = 0; k < participants.length(); k++) {
                                    JSONObject participant = participants.getJSONObject(k);
                                    String name = participant.getString("name");
                                    String tag = participant.getString("tag");
                                    int decksUsed = participant.getInt("decksUsed");
                                    int fame = participant.getInt("fame");
                                    List<Object> rowData = rowDatas.get(tag);
                                    if (rowData == null) {
                                        rowData = new ArrayList<>(
                                                List.of(clan.getName(), name, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                                    }
                                    rowData.set(2 * responseLimit - 2 * i, decksUsed);
                                    rowData.set(2 * responseLimit - 2 * i + 1, fame);
                                    rowData.set(2 * responseLimit + 2,
                                            ((Integer) rowData.get(2 * responseLimit + 2)) + decksUsed);
                                    rowData.set(2 * responseLimit + 3,
                                            ((Integer) rowData.get(2 * responseLimit + 3)) + fame);
                                    rowDatas.put(tag, rowData);
                                }
                            }
                        }
                    }
                    for (Map.Entry<String, List<Object>> entry : rowDatas.entrySet()) {
                        List<Object> rowData = entry.getValue();
                        Formal.addRow(outputFilePath, sheetName, rowData);
                        ++edRow;
                        Logger.log("INFO",
                                "< " + clan.getName() + ">Added row for member: " + rowData.get(1),
                                QueryLastMonthContribution.class, logFilePath);
                    }
                } else {
                    Logger.log("ERROR",
                            "< " + clan.getName() + ">Request failed with status code: "
                                    + response.getStatusLine().getStatusCode(),
                            QueryLastMonthContribution.class, logFilePath);
                    flag = false;
                }
            } catch (Exception e) {
                Logger.log("ERROR",
                        "Error occurred while querying last month contribution information for clan: " + clan.getName(),
                        QueryLastMonthContribution.class, logFilePath);
                flag = false;
            }
            if (edRow > stRow) {
                try {
                    Formal.sortSheetRows(outputFilePath, sheetName, stRow, edRow - 1, 2 * responseLimit + 3, false);
                    Logger.log("INFO",
                            "< " + clan.getName() + ">Sorted sheet rows for last month contribution from " + stRow
                                    + " to " + (edRow - 1),
                            QueryLastMonthContribution.class, logFilePath);
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "< " + clan.getName() + ">Error while sorting sheet rows: " + e.getMessage(),
                            QueryLastMonthContribution.class, logFilePath);
                }
                try {
                    int[] columnsToMark = new int[responseLimit + 1];
                    int[] standings = new int[responseLimit + 1];
                    String[] conditions = new String[responseLimit + 1];
                    String[] colors = new String[responseLimit + 1];
                    for (int i = 0; i <= responseLimit; i++) {
                        columnsToMark[i] = 2 * i + 3;
                        standings[i] = 0;
                        conditions[i] = "==";
                        colors[i] = "#FF0000";
                    }
                    Formal.markCellsByCondition(outputFilePath, sheetName, columnsToMark,
                            standings, conditions, colors);
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "< " + clan.getName() + ">Error while marking cells: " + e.getMessage(),
                            QueryLastMonthContribution.class, logFilePath);
                }
                try {
                    Formal.mergeRegion(outputFilePath, sheetName, stRow, edRow - 1, stCol, edCol);
                    Logger.log("INFO", "< " + clan.getName() + ">Merged region from " + stRow + " to " + (edRow - 1),
                            QueryLastMonthContribution.class, logFilePath);
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "< " + clan.getName() + ">Error while merging region: " + e.getMessage(),
                            QueryLastMonthContribution.class, logFilePath);
                }
            }
            stRow = edRow;
        }
        return flag;
    }
}
