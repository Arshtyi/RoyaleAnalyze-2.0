package org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation;

import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;

/**
 * This class is used to create information by reading from files.
 * It includes methods to read clan and player information and handle logging.
 * It is part of the RoyaleAnalyze2 application.
 */
public class CreateInformation {
    /**
     * This class is not meant to be instantiated.
     */
    /**
     * This class is used to create information by reading from files.
     * It includes methods to read clan and player information and handle logging.
     * It is part of the RoyaleAnalyze2 application.
     */
    public static void main() {
        String logFilePath = Externs.getLogFilePath();
        Logger.log("INFO", "Preparing to read file information...", CreateInformation.class, logFilePath);
        if (CreateClanInformation.main()) {
            Logger.log("INFO", "Clan information read successfully, preparing to read player information...",
                    CreateInformation.class, logFilePath);
        } else {
            Logger.log("ERROR", "Failed to read clan information, the program cannot continue...",
                    CreateInformation.class, logFilePath);
            Logger.log("ERROR",
                    "The program is preparing to terminate, please check the clan information file after termination...",
                    CreateInformation.class, logFilePath);
            System.exit(1);
        }
        if (Externs.isPlayersInformationExists()) {
            if (CreatePlayerInformation.main()) {
                Logger.log("INFO", "Player information read successfully...",
                        CreateInformation.class, logFilePath);
                Externs.setPlayersInformationExists(true);
            } else {
                Logger.log("INFO", "Failed to read player information, the program will not be able to filter...",
                        CreateInformation.class, logFilePath);
                Externs.setPlayersInformationExists(false);
            }
            Logger.log("INFO", "File information read successfully, entering the program...", CreateInformation.class,
                    logFilePath);
        }
    }
}
