package org.arshtyi.royaleanalyze2.royaleanalyze2.extern;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreateClanInformation;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreatePlayerInformation;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

public class Externs {
    /**
     * This is a utility class and cannot be instantiated.
     */
    private Externs() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final String CLANS_INFORMATION_XLSX_FILE_NAME = "clans.xlsx";
    private static final String PLAYERS_INFORMATION_XLSX_FILE_NAME = "players.xlsx";
    private static final String CLANS_INFORMATION_JSON_FILE_NAME = "clans.json";
    private static final String PLAYERS_INFORMATION_JSON_FILE_NAME = "players.json";
    private static final String OUTPUT_INFORMATION_XLSX_FILE_NAME = "Information.xlsx";
    private static final String API_KEY_FILE_NAME = "APIKEY.pem";
    private static final String LOG_FILE_NAME = "ProgramLog.log";
    private static final String INPUT_DIRECTORY_NAME = "input";
    private static final String OUTPUT_DIRECTORY_NAME = "output";
    private static final String LOG_DIRECTORY_NAME = "log";
    private static final String CONFIG_DIRECTORY_NAME = "config";
    private static final String TEMP_DIRECTORY_NAME = "temp";
    private static final String TEMP_DIRECTORY_PATH = buildPath(getCurrentPath(), TEMP_DIRECTORY_NAME);
    private static final String CURRENT_PATH = getCurrentPath();
    private static final String INPUT_DIRECTORY = buildPath(CURRENT_PATH, INPUT_DIRECTORY_NAME);
    private static final String OUTPUT_DIRECTORY = buildPath(CURRENT_PATH, OUTPUT_DIRECTORY_NAME);
    private static final String LOG_DIRECTORY = buildPath(CURRENT_PATH, LOG_DIRECTORY_NAME);
    private static final String CONFIG_DIRECTORY = buildPath(CURRENT_PATH, CONFIG_DIRECTORY_NAME);
    private static final String CLANS_INFORMATION_XLSX_FILE_PATH = buildPath(INPUT_DIRECTORY,
            CLANS_INFORMATION_XLSX_FILE_NAME);

    public static String getInputDirectoryPath() {
        return INPUT_DIRECTORY;
    }

    public static String getOutputDirectoryPath() {
        return OUTPUT_DIRECTORY;
    }

    public static String getLogDirectoryPath() {
        return LOG_DIRECTORY;
    }

    public static String getConfigDirectoryPath() {
        return CONFIG_DIRECTORY;
    }

    public static String getTempDirectoryPath() {
        return TEMP_DIRECTORY_PATH;
    }

    public static String getClansInformationXlsxFilePath() {
        return CLANS_INFORMATION_XLSX_FILE_PATH;
    }

    private static final String PLAYERS_INFORMATION_XLSX_FILE_PATH = buildPath(INPUT_DIRECTORY,
            PLAYERS_INFORMATION_XLSX_FILE_NAME);

    public static String getPlayersInformationXlsxFilePath() {
        return PLAYERS_INFORMATION_XLSX_FILE_PATH;
    }

    private static final String CLANS_INFORMATION_JSON_FILE_PATH = buildPath(INPUT_DIRECTORY,
            CLANS_INFORMATION_JSON_FILE_NAME);

    public static String getClansInformationJsonFilePath() {
        return CLANS_INFORMATION_JSON_FILE_PATH;
    }

    private static final String PLAYERS_INFORMATION_JSON_FILE_PATH = buildPath(INPUT_DIRECTORY,
            PLAYERS_INFORMATION_JSON_FILE_NAME);

    public static String getPlayersInformationJsonFilePath() {
        return PLAYERS_INFORMATION_JSON_FILE_PATH;
    }

    private static final String OUTPUT_INFORMATION_XLSX_FILE_PATH = buildPath(OUTPUT_DIRECTORY,
            OUTPUT_INFORMATION_XLSX_FILE_NAME);

    public static String getOutputInformationXlsxFilePath() {
        return OUTPUT_INFORMATION_XLSX_FILE_PATH;
    }

    private static final String API_KEY_FILE_PATH = buildPath(CONFIG_DIRECTORY, API_KEY_FILE_NAME);
    private static final String LOG_FILE_PATH = buildPath(LOG_DIRECTORY, LOG_FILE_NAME);

    public static String getLogFilePath() {
        return LOG_FILE_PATH;
    }

    private static final String FIXED_API_KEY = "Bearer " + getApiKey();

    public static String getFixedApiKey() {
        return FIXED_API_KEY;
    }

    private static List<CreateClanInformation.Clan> clans = new ArrayList<>();

    public static List<CreateClanInformation.Clan> getClans() {
        return clans;
    }

    public static void setClans(List<CreateClanInformation.Clan> clans) {
        Externs.clans = clans;
    }

    public static List<CreateClanInformation.Clan> addClan(CreateClanInformation.Clan clan) {
        Externs.clans.add(clan);
        return clans;
    }

    public static void clearClans() {
        Externs.clans.clear();
    }

    private static List<CreatePlayerInformation.Player> players = new ArrayList<>();

    public static List<CreatePlayerInformation.Player> getPlayers() {
        return players;
    }

    public static void setPlayers(List<CreatePlayerInformation.Player> players) {
        Externs.players = players;
    }

    public static void addPlayer(CreatePlayerInformation.Player player) {
        Externs.players.add(player);
    }

    public static void clearPlayers() {
        Externs.players.clear();
    }

    private static List<String> USER_CHOICES = new ArrayList<>();

    public static List<String> getUserChoices() {
        return USER_CHOICES;
    }

    public static void addUserChoice(String userChoice) {
        Externs.USER_CHOICES.add(userChoice);
    }

    public static void clearUserChoices() {
        Externs.USER_CHOICES.clear();
    }

    private static final int SUCCESSFUL_RESPONSE_STATUS_CODE = 200;

    public static int getSuccessfulResponseStatusCode() {
        return SUCCESSFUL_RESPONSE_STATUS_CODE;
    }

    private static final String CLANS_SHEET_NAME = "Clans";

    public static String getClansSheetName() {
        return CLANS_SHEET_NAME;
    }

    private static final String PLAYERS_SHEET_NAME = "Players";

    public static String getPlayersSheetName() {
        return PLAYERS_SHEET_NAME;
    }

    private static boolean PLAYERS_INFORMATION_EXISTS = true;

    public static boolean isPlayersInformationExists() {
        return PLAYERS_INFORMATION_EXISTS;
    }

    public static void setPlayersInformationExists(boolean playersInformationExists) {
        Externs.PLAYERS_INFORMATION_EXISTS = playersInformationExists;
    }

    private static List<String> FAULTS = new ArrayList<>();

    public static List<String> getFaults() {
        return FAULTS;
    }

    public static void setFaults(List<String> faults) {
        Externs.FAULTS = faults;
    }

    public static void addFault(String fault) {
        Externs.FAULTS.add(fault);
    }

    public static void clearFaults() {
        Externs.FAULTS.clear();
    }

    private static final Map<String, String> OPERATIONS_DEFINITIONS = new LinkedHashMap<>() {
        {
            put("1", "Update and check the input files.");
            put("2", "Clear the output files.");
            put("3", "Check the current clan war contribution status of all clans");
            put("4", "Check the current clan donation status of all clans");
            put("5", "Check the last active time of all clan members");
            put("6", "Check last month's clan war total contribution");
            put("0", "Format the output files.");
        }
    };

    public static Map<String, String> getOperationsDefinitions() {
        return OPERATIONS_DEFINITIONS;
    }

    public static String getOperationDefinition(String operation) {
        return OPERATIONS_DEFINITIONS.get(operation);
    }

    private static final Map<String, String> SHEET_NAMES = new HashMap<>() {
        {
            put("CurrentWar", "CurrentWar");
            put("CurrentDonation", "CurrentDonation");
            put("LastActiveTime", "LastActiveTime");
            put("LastMonthContribution", "LastMonthContribution");
        }
    };

    public static String getSheetName(String operation) {
        return SHEET_NAMES.get(operation);
    }

    private static final Map<String, List<String>> SHEET_FIRST_LINE_CONTENTS = new HashMap<>() {
        {
            put("CurrentWar", List.of("部落", "玩家", "今日使用卡组数", "使用卡组总数", "袭击战船次数", "贡献值"));
            put("CurrentDonation", List.of("部落", "玩家", "捐赠值", "收到的捐赠值"));
            put("LastActiveTime", List.of("部落", "玩家", "最后活跃时间"));
            put("LastMonthContribution", List.of("部落", "玩家", "第一周使用卡组数", "第一周贡献值",
                    "第二周使用卡组数", "第二周贡献值", "第三周使用卡组数", "第三周贡献值", "第四周使用卡组数", "第四周贡献值", "总使用卡组数", "总贡献值"));
        }
    };

    public static List<String> getSheetFirstLineContents(String operation) {
        return SHEET_FIRST_LINE_CONTENTS.get(operation);
    }

    /**
     * This method normalizes a file path by converting it to an absolute path and
     * resolving any relative components.
     * 
     * @param path
     *            the file path to be normalized
     * @return normalized file path or empty string if input is null or empty
     * @exception if
     *                the path is invalid or cannot be normalized.
     */
    public static String normalizePath(String path) {
        // If the path is null or empty, return an empty string
        if (path == null || path.isEmpty()) {
            return "";
        }
        /**
         * If the path is not absolute, convert it to an absolute path.
         * This is done by using the Paths.get() method to create a Path object from the
         * given path string and then calling toAbsolutePath() to ensure it is absolute.
         * 
         * @return normalized file path or the original path if an exception as follows
         *         occurs.
         * @exception if
         *                the path is invalid or cannot be normalized.
         */
        try {
            Path normalizedPath = Paths.get(path).normalize();
            if (!normalizedPath.isAbsolute()) {
                normalizedPath = normalizedPath.toAbsolutePath();
            }
            return normalizedPath.toString();
        } catch (Exception e) {
            System.err.println("Error normalizing path: " + e.getMessage());
            return path;
        }
    }

    /**
     * This method retrieves the current path of the application.
     * It first checks if the system property "app.base.path" is set, and if so, it
     * uses that path.
     * If the system property is not set, it tries to get the path from the
     * ProtectionDomain and if that fails, it falls back to the current working
     * directory.
     * 
     * @param void
     * @return normalized current path
     * @exception if
     *                the path cannot be determined
     */
    public static String getCurrentPath() {
        // First try to get the path from the system property "app.base.path"
        String basePath = System.getProperty("app.base.path");
        if (basePath != null && !basePath.isEmpty()) {
            return normalizePath(basePath);
        }
        // If the system property is not set, try to get the path from the
        // ProtectionDomain
        try {
            ProtectionDomain protectionDomain = Externs.class.getProtectionDomain();
            CodeSource codeSource = protectionDomain.getCodeSource();
            if (codeSource != null) {
                URL location = codeSource.getLocation();
                File file = new File(location.toURI());
                if (file.isFile()) {
                    return normalizePath(file.getParentFile().getAbsolutePath());
                } else {
                    return normalizePath(file.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting current path: " + e.getMessage());
        }
        // If the ProtectionDomain is not available, try to get the current path from
        // classloader
        URL classResource = Externs.class.getResource("");
        if (classResource != null) {
            try {
                String path = Paths.get(classResource.toURI()).toString();
                // 移除包路径部分
                String packagePath = Externs.class.getPackage().getName().replace('.', File.separatorChar);
                if (path.endsWith(packagePath)) {
                    path = path.substring(0, path.length() - packagePath.length());
                }
                return normalizePath(path);
            } catch (URISyntaxException e) {
                System.err.println("Error getting current path from classloader: " + e.getMessage());
            }
        }
        // If all else fails, return the current working directory
        return normalizePath(System.getProperty("user.dir"));
    }

    /**
     * This method builds a path by resolving a relative path against a base path.
     * 
     * @param basePath
     *            the base path to resolve against.
     * @param relativePath
     *            the relative path to be resolved.
     * @return the resolved path as a string
     *         * @throws IllegalArgumentException if the relative path is null or
     *         contains ".." or if the resolved path is outside the base directory.
     */
    public static String buildPath(String basePath, String relativePath) {
        /**
         * If the relative path is null or contains "..", throw an exception.
         * 
         * @IllegalArgumentException if the relative path is null or contains ".."
         */
        if (relativePath == null || relativePath.contains("..")) {
            throw new IllegalArgumentException("Unsafe path:" + relativePath);
        }
        try {
            Path base = Paths.get(basePath).normalize().toAbsolutePath();
            /**
             * Replace forward slashes and backslashes with the system's file separator
             */
            relativePath = relativePath.replace('/', File.separatorChar).replace('\\', File.separatorChar);
            Path resolvedPath = base.resolve(relativePath).normalize();

            /**
             * Ensure the resolved path is still within the base directory
             * 
             * @IllegalArgumentException if the resolved path is outside the base directory
             */
            if (!resolvedPath.startsWith(base)) {
                throw new IllegalArgumentException("Path exceeds base directory range: " + relativePath);
            }

            return resolvedPath.toString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Path building failed:" + e.getMessage(), e);
        }
    }

    /**
     * This method retrieves the API key from the API_KEY_FILE_PATH.
     * It reads the contents of the file and returns it as a string.
     * 
     * @param void
     * @throws IOException
     *             if an error occurs while reading the file
     * @return the API key as a string
     */
    public static String getApiKey() {
        try {
            return new String(java.nio.file.Files.readAllBytes(Paths.get(API_KEY_FILE_PATH)));
        } catch (IOException e) {
            System.err.println("Error reading API key file: " + e.getMessage());
            return "";
        }
    }
}