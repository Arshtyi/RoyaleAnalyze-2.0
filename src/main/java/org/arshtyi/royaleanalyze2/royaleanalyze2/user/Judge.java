package org.arshtyi.royaleanalyze2.royaleanalyze2.user;

import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.file.Formal;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.UpdateInformation;
import org.arshtyi.royaleanalyze2.royaleanalyze2.querycurrentwar.QueryCurrentWar;
import org.arshtyi.royaleanalyze2.royaleanalyze2.querylastactivetime.QueryLastActiveTime;
import org.arshtyi.royaleanalyze2.royaleanalyze2.querylastmonthcontribution.QueryLastMonthContribution;
import org.arshtyi.royaleanalyze2.royaleanalyze2.querycurrentdonation.QueryCurrentDonation;

/***
 * This class is used to judge the user's choice of operations.
 * It checks if the choice is valid and executes the corresponding operation.
 * It also handles logging for successful and failed operations.
 */
public class Judge {
    /***
     * This method is the main entry point for the program.
     * It takes user input as command line arguments and executes the corresponding
     * operations.
     * 
     * @param args
     *            command line arguments representing user choices
     * @throws Exception
     *             if an error occurs during the execution of operations
     * @throws IOException
     *             if an I/O error occurs while processing files
     * @return void
     */
    public static void main(String[] args) {
        String logFilePath = Externs.getLogFilePath();
        for (String choice : args) {
            if (Externs.getOperationsDefinitions().containsKey(choice)) {
                Logger.log("INFO", "User choice: " + choice + " -- " + Externs.getOperationsDefinitions().get(choice),
                        Judge.class, logFilePath);
                if (judge(choice)) {
                    Logger.log("INFO", "Successfully executed operation: " + choice + " -- "
                            + Externs.getOperationsDefinitions().get(choice), Judge.class, logFilePath);
                } else {
                    Logger.log("ERROR", "Failed to execute operation: " + choice + " -- "
                            + Externs.getOperationsDefinitions().get(choice), Judge.class, logFilePath);
                    Externs.addFault(choice);
                }
            } else {
                Logger.log("WARN", "Undefined operation code: " + choice + ", skipping this item...", Judge.class,
                        logFilePath);
            }
        }
        if (Externs.getFaults().size() > 0) {
            Logger.log("ERROR", "Some operations failed: " + Externs.getFaults(), Judge.class, logFilePath);
            Externs.clearFaults();
        } else {
            Logger.log("INFO", "All operations executed successfully.", Judge.class, logFilePath);
        }
        return;
    }

    /***
     * This method judges the user's choice and executes the corresponding
     * operation.
     * It returns true if the operation was successful, false otherwise.
     * 
     * @param choice
     *            the user's choice of operation
     * @return true if the operation was successful, false otherwise
     */
    private static boolean judge(String choice) {
        if (choice.equals("1")) {
            return UpdateInformation.main();
        } else if (choice.equals("2")) {
            return Formal.deleteFile(Externs.getOutputInformationXlsxFilePath());
        } else if (choice.equals("3")) {
            return QueryCurrentWar.main();
        } else if (choice.equals("4")) {
            return QueryCurrentDonation.main();
        } else if (choice.equals("5")) {
            return QueryLastActiveTime.main();
        } else if (choice.equals("6")) {
            return QueryLastMonthContribution.main();
        } else if (choice.equals("0")) {
            return Formal.formalFile(Externs.getOutputInformationXlsxFilePath());
        }
        return true;
    }

    /***
     * This method judges the user's choice for a yes or no question.
     * It returns true if the answer is yes, false otherwise.
     * 
     * @param input
     *            the user's input
     * @return true if the answer is yes, false otherwise
     */
    public static boolean judgeTwo(String input) {
        input = input.replaceAll("\\s+", "").toLowerCase();
        // Return true only for "yes" or "y"
        return input.equals("yes") || input.equals("y");
    }
}
