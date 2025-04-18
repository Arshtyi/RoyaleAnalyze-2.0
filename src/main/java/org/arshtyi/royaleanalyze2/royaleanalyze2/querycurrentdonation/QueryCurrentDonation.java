package org.arshtyi.royaleanalyze2.royaleanalyze2.querycurrentdonation;

import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Urls;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreateClanInformation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.arshtyi.royaleanalyze2.royaleanalyze2.file.Formal;
import org.apache.http.impl.client.CloseableHttpClient;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/**
 * Retrieves and processes current donation information for clan members.
 * This class queries the Clash Royale API for donation data and formats it
 * in the output spreadsheet.
 */
public class QueryCurrentDonation {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private QueryCurrentDonation() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Main entry point for querying current donation information.
     * 
     * @return true if the query was successful, false otherwise
     */
    public static boolean main() {
        return queryCurrentDonation();
    }

    /**
     * Queries the current donation information for each clan and processes the
     * results.
     * For each clan, this method:
     * 1. Retrieves member donation data from the Clash Royale API
     * 2. Adds the data to the output spreadsheet
     * 3. Sorts the data by donation count
     * 4. Applies formatting (marking cells with zero donations)
     * 5. Merges regions for better readability
     * 
     * @return true if all operations completed successfully, false if any errors
     *         occurred
     */
    public static boolean queryCurrentDonation() {
        boolean flag = true;
        String logFilePath = Externs.getLogFilePath();
        String outputFilePath = Externs.getOutputInformationXlsxFilePath();
        String sheetName = Externs.getSheetName("CurrentDonation");

        Logger.log("INFO", "Querying current donation information...", QueryCurrentDonation.class, logFilePath);
        Formal.addSheet(outputFilePath, sheetName);

        int startRow = 1, startCol = 0, endRow = 1, endCol = 0;

        for (CreateClanInformation.Clan clan : Externs.getClans()) {
            Logger.log("INFO", "Querying current donation information for clan: " + clan.getName(),
                    QueryCurrentDonation.class, logFilePath);

            try {
                // Prepare API request
                String clanTag = clan.getTag();
                String url = Urls.getFullUrl(Urls.getClanInformationUrl(clanTag), "/members");
                Logger.log("INFO", "< " + clan.getName() + "> Request URL: " + url, QueryCurrentDonation.class,
                        logFilePath);

                // Execute API request
                CloseableHttpClient httpClient = Urls.createHttpClient();
                HttpGet httpGet = Urls.createHttpGet(url);
                Logger.log("INFO", "< " + clan.getName() + "> Sending request...", QueryCurrentDonation.class,
                        logFilePath);
                CloseableHttpResponse response = httpClient.execute(httpGet);

                // Process response
                if (response.getStatusLine().getStatusCode() == Externs.getSuccessfulResponseStatusCode()) {
                    Logger.log("INFO", "< " + clan.getName() + "> Request successful!", QueryCurrentDonation.class,
                            logFilePath);

                    // Parse JSON response
                    JSONObject root = new JSONObject(EntityUtils.toString(response.getEntity()));
                    JSONArray members = root.getJSONArray("items");

                    // Process each clan member
                    for (int i = 0; i < members.length(); i++) {
                        JSONObject member = members.getJSONObject(i);
                        String name = member.getString("name");
                        int donations = member.getInt("donations");
                        int donationsReceived = member.getInt("donationsReceived");

                        // Add member data to spreadsheet
                        List<Object> rowData = List.of(
                                clan.getName(), name, donations, donationsReceived);
                        Formal.addRow(outputFilePath, sheetName, rowData);
                        ++endRow;

                        Logger.log("INFO",
                                "< " + clan.getName() + "> Added row for member: " + name, QueryCurrentDonation.class,
                                logFilePath);
                    }
                } else {
                    Logger.log("ERROR",
                            "< " + clan.getName() + "> Request failed with status code: "
                                    + response.getStatusLine().getStatusCode(),
                            QueryCurrentDonation.class, logFilePath);
                    flag = false;
                }
            } catch (Exception e) {
                Logger.log("ERROR",
                        "< " + clan.getName() + "> Error while querying current donation information: "
                                + e.getMessage(),
                        QueryCurrentDonation.class, logFilePath);
                flag = false;
            }

            // Format spreadsheet if rows were added
            if (endRow > startRow) {
                try {
                    // Sort by donation count (descending)
                    Formal.sortSheetRows(outputFilePath, sheetName, startRow, endRow - 1, 2, false);
                    Logger.log("INFO",
                            "< " + clan.getName() + "> Sorted sheet rows for donations from " + startRow + " to "
                                    + (endRow - 1),
                            QueryCurrentDonation.class, logFilePath);
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "< " + clan.getName() + "> Error while sorting sheet rows: " + e.getMessage(),
                            QueryCurrentDonation.class, logFilePath);
                }

                try {
                    // Mark cells with zero donations
                    Formal.markCellsByCondition(outputFilePath, sheetName, new int[] { 2 }, new int[] { 0 },
                            new String[] { "==" }, new String[] { "#FF0000" });
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "< " + clan.getName() + "> Error while marking cells: " + e.getMessage(),
                            QueryCurrentDonation.class, logFilePath);
                }

                try {
                    // Merge clan name cells for better readability
                    Formal.mergeRegion(outputFilePath, sheetName, startRow, endRow - 1, startCol, endCol);
                    Logger.log("INFO",
                            "< " + clan.getName() + "> Merged region from " + startRow + " to " + (endRow - 1),
                            QueryCurrentDonation.class, logFilePath);
                } catch (Exception e) {
                    flag = false;
                    Logger.log("ERROR", "< " + clan.getName() + "> Error while merging region: " + e.getMessage(),
                            QueryCurrentDonation.class, logFilePath);
                }
            }

            startRow = endRow;
        }

        return flag;
    }
}
