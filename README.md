<!-- @format -->

# RoyaleAnalyze 2.0

## Project Introduction

-   This project is version 2.0 of [RoyaleAnalyze](https://github.com/Arshtyi/RoyaleAnalyze), implemented using the official [Clash Royale API](https://developer.clashroyale.com/#/). The previous version relied on [RoyaleAPI](https://royaleapi.com/), which has unfortunately discontinued its maintenance.
-   Latest version: V0.0.3 (Last updated: April 10, 2025)
-   Built with [JDK 21](https://www.oracle.com/cn/java/technologies/downloads/#java21)
-   Project structure: Maven
-   Dependencies are listed in [pom.xml](https://github.com/Arshtyi/RoyaleAnalyze-2.0/blob/main/pom.xml)
-   Note: Due to architectural considerations, there will likely be no official Releases published

## Important Notes

-   The project was restructured on April 4, 2025, removing all interactive features while preserving the original functionality. It now executes all features sequentially upon startup and then terminates.
-   Development and testing have been primarily conducted on the Windows platform. If you encounter any bugs, please [contact the author](https://github.com/Arshtyi), create an [Issue](https://github.com/Arshtyi/RoyaleAnalyze-2.0/issues), or submit a [Pull Request](https://github.com/Arshtyi/RoyaleAnalyze-2.0/pulls).
-   The [Clash Royale API](https://developer.clashroyale.com/#/) enforces strict request validation and does not allow disguised requests. Therefore:
    -   The project stores the API key in an external file
    -   You must apply for your own API key and replace the content in the `APIKEY.pem` file
    -   Advanced users may also want to update the relevant configurations in the `Urls` class
-   Warning: Do not run this application as a JAR file, as it will encounter path resolution errors.

## Usage

### Prerequisites

-   [JDK 23](https://www.oracle.com/cn/java/technologies/downloads/#java23): Verify your JDK version with:
    ```
    java --version
    ```
-   An API key from [Clash Royale API](https://developer.clashroyale.com/#/), which must be added to `resources/config/APIKEY.pem`
-   Maven (optional, as the project already includes mvnw and mvnw.cmd wrapper scripts)

### Installation

```powershell
git clone https://github.com/Arshtyi/RoyaleAnalyze-2.0.git
cd RoyaleAnalyze-2.0
```

### Configuration

-   Verify that your API key has been properly added to the configuration file
-   Ensure all required files in the `resources/input` directory are present and properly formatted

### Running the Application

```
./mvnw spring-boot:run
```

-   Output files will be generated in the `output` directory
-   Remember: Do not attempt to run as a JAR file

## [Changelog](https://github.com/Arshtyi/RoyaleAnalyze-2.0/blob/main/CHANGELOG.md)

## Supported Features

-   [x] Update clan names, player names, and clan affiliations
-   [x] Clear output
-   [x] Query current clan war contributions
-   [x] Query donation status of clan members
-   [x] Query last active time of clan members
-   [x] Query clan war contributions for the past month
-   [ ] ~~Query donation status for the past month~~ (No longer possible to implement)
-   [x] Query total contributions for the past month
-   [ ] ~~Query recent clan membership changes~~ (No longer possible to implement)
-   [x] Format files

## Completed Improvements

-   [x] Restructured core components
-   [x] Optimized resource management

## License

This project is licensed under the terms of this [LICENSE](https://github.com/Arshtyi/RoyaleAnalyze-2.0/blob/main/LICENSE).

## Contact Information

-   QQ: 640006128
-   Email: arshtyi_trantor@outlook.com
