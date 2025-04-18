package org.arshtyi.royaleanalyze2.royaleanalyze2.check;

import java.io.File;
import org.arshtyi.royaleanalyze2.royaleanalyze2.log.Logger;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Validates the application environment, including directories and files.
 * Responsible for checking the existence of required resources and creating
 * them if necessary.
 */
public class Check {
    /**
     * Utility class with static methods only - prevent instantiation.
     */
    private Check() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Verifies if a file exists at the specified path.
     * 
     * @param path
     *            The path to the file to check
     * @return true if the file exists and is a file, false otherwise
     */
    public static boolean checkFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }

    /**
     * Verifies if a directory exists at the specified path.
     * 
     * @param path
     *            The path to the directory to check
     * @return true if the directory exists and is a directory, false otherwise
     */
    public static boolean checkDirectory(String path) {
        File directory = new File(path);
        return directory.exists() && directory.isDirectory();
    }

    /**
     * Validates and prepares the log directory and file.
     * Creates the log directory if it doesn't exist.
     * Creates or clears the log file as needed.
     * Terminates the program if critical log operations fail.
     */
    public static void checkLog() {
        String logDirPath = Externs.getLogDirectoryPath();
        String logFilePath = Externs.getLogFilePath();

        // Check and create log directory if needed
        if (checkDirectory(logDirPath)) {
            Logger.log("INFO", "Log directory path is complete...", Check.class, logFilePath);
        } else {
            File logDir = new File(logDirPath);
            if (logDir.mkdirs()) {
                Logger.log("INFO", "Log directory created successfully...", Check.class, logFilePath);
                return;
            } else {
                Logger.log("ERROR",
                        "Log directory creation failed, which will cause the program to fail to record logs...",
                        Check.class, "");
                Logger.log("ERROR",
                        "The program is about to terminate, please try to manually fix the log directory path after termination...",
                        Check.class, "");
                System.exit(1);
            }
        }

        // Check and create/clear log file as needed
        File file = new File(logFilePath);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Logger.log("INFO", "Log file created successfully: " + logFilePath, Check.class,
                            logFilePath);
                    return;
                } else {
                    Logger.log("ERROR",
                            "Log file creation failed, which will cause the program to fail to record logs into the log file...",
                            Check.class, "");
                }
            } catch (IOException e) {
                Logger.log("ERROR",
                        "An exception occurred while creating the log file: " + e.getMessage(), Check.class,
                        "");
            }
        } else {
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write("");
                writer.flush();
            } catch (IOException e) {
                Logger.log("ERROR", "An exception occurred while clearing log file contents: " + e.getMessage(),
                        Check.class, logFilePath);
            }
        }
    }

    /**
     * Performs a comprehensive check of the application environment.
     * Validates the existence of required directories and files.
     * Attempts to create missing directories when possible.
     * 
     * @return true if all critical checks pass, false otherwise
     */
    public static boolean checkEnvironment() {
        boolean flag = true;
        String logFilePath = Externs.getLogFilePath();
        Logger.log("INFO", "About to start environment check...", Check.class, logFilePath);

        // Get paths to required resources
        String inputDirectoryPath = Externs.getInputDirectoryPath();
        String clansInformationXlsxFilePath = Externs.getClansInformationXlsxFilePath();
        String playersInformationXlsxFilePath = Externs.getPlayersInformationXlsxFilePath();
        String outputDirectoryPath = Externs.getOutputDirectoryPath();

        // Check input directory
        if (checkDirectory(inputDirectoryPath)) {
            Logger.log("INFO", "Input directory path is complete, preparing to check clan information file...",
                    Check.class, logFilePath);
        } else {
            Logger.log("ERROR",
                    "The input directory path is incomplete, indicating the absence of the clan information file, which will cause the program to fail to run...",
                    Check.class, logFilePath);
            return false;
        }

        // Check clan information file
        if (checkFile(clansInformationXlsxFilePath)) {
            Logger.log("INFO", "Clan information file exists, preparing to check player information file...",
                    Check.class, logFilePath);
        } else {
            Logger.log("ERROR", "Clan information file does not exist, which will cause the program to fail to run...",
                    Check.class, logFilePath);
            Logger.log("ERROR",
                    "The program is about to terminate, please try to manually fix the clan information file after termination...",
                    Check.class, logFilePath);
            return false;
        }

        // Check player information file (optional)
        if (checkFile(playersInformationXlsxFilePath)) {
            Logger.log("INFO", "Player information file exists, preparing to check log directory path...", Check.class,
                    logFilePath);
        } else {
            Externs.setPlayersInformationExists(flag = false);
            Logger.log("WARN",
                    "Player information file does not exist, although this will not cause the program to fail to run, it will prevent the program from querying player profiles...",
                    Check.class, logFilePath);
            Logger.log("INFO", "Preparing to check log directory path...", Check.class, logFilePath);
        }

        // Check output directory and create if missing
        if (checkDirectory(outputDirectoryPath)) {
            Logger.log("INFO", "Output directory path is complete, preparing to exit check...", Check.class,
                    logFilePath);
        } else {
            Logger.log("ERROR",
                    "Output directory path is incomplete, which will cause the program to fail to output results...",
                    Check.class, logFilePath);
            Logger.log("INFO", "Attempting to create output directory...", Check.class, logFilePath);
            File outputDir = new File(outputDirectoryPath);
            if (outputDir.mkdirs()) {
                Logger.log("INFO", "Output directory created successfully, preparing to exit check...", Check.class,
                        logFilePath);
            } else {
                Logger.log("ERROR",
                        "Output directory creation failed, which will cause the program to fail to output results...",
                        Check.class, logFilePath);
                Logger.log("ERROR",
                        "The program is about to terminate, please try to manually fix the output directory path after termination...",
                        Check.class, logFilePath);
                return false;
            }
        }

        return flag;
    }

    /**
     * Entry point for environment validation.
     * Performs comprehensive environment checks and handles failures appropriately.
     */
    public static void main() {
        String logFilePath = Externs.getLogFilePath();
        if (checkEnvironment()) {
            Logger.log("INFO", "Environment check passed, the program is ready to start...", Check.class,
                    logFilePath);
        } else {
            Logger.log("ERROR", "Environment check failed, the program is about to terminate...", Check.class,
                    logFilePath);
            Logger.log("ERROR", "Please try to manually fix the environment issues after termination...", Check.class,
                    logFilePath);
            System.exit(1);
        }
    }

    /**
     * Performs cleanup operations before application exit.
     * Closes any open resources and removes temporary files.
     */
    public static void checkResources() {
        String logFilePath = Externs.getLogFilePath();
        Logger.log("INFO", "Starting resource check and cleanup...", Check.class, logFilePath);

        // Clean temporary files
        String tempDirPath = Externs.getTempDirectoryPath();
        if (checkDirectory(tempDirPath)) {
            File tempDir = new File(tempDirPath);
            File[] tempFiles = tempDir.listFiles();
            if (tempFiles != null && tempFiles.length > 0) {
                Logger.log("INFO", "Found " + tempFiles.length + " temporary files to clean up...", Check.class,
                        logFilePath);
                for (File file : tempFiles) {
                    if (file.delete()) {
                        Logger.log("INFO", "Successfully deleted temporary file: " + file.getName(), Check.class,
                                logFilePath);
                    } else {
                        Logger.log("WARN", "Failed to delete temporary file: " + file.getName(), Check.class,
                                logFilePath);
                    }
                }
            } else {
                Logger.log("INFO", "No temporary files found for cleanup", Check.class, logFilePath);
            }
        }

        // Force garbage collection to release resources
        try {
            System.gc();
            Logger.log("INFO", "Triggered garbage collection to help release unused resources", Check.class,
                    logFilePath);
        } catch (Exception e) {
            Logger.log("WARN", "Exception occurred during garbage collection: " + e.getMessage(), Check.class,
                    logFilePath);
        }

        // Final check of output directory
        String outputDirectoryPath = Externs.getOutputDirectoryPath();
        if (checkDirectory(outputDirectoryPath)) {
            Logger.log("INFO", "Checked output directory for resources", Check.class, logFilePath);
        }

        Logger.log("INFO", "Resource check and cleanup completed", Check.class, logFilePath);
    }
}
