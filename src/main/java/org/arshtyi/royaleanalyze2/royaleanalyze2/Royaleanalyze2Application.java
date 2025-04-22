/**
 * A tool for analyzing Clash Royale game data.
 * 
 * Development information:
 * @author Arshtyi
 * @contact
 *     GitHub: https://github.com/Arshtyi
 *     QQ: 640006128
 *     Email: arshtyi_trantor@outlook.com
 * @repository https://github.com/Arshtyi/RoyaleAnalyze-2.0
 * @basedOn https://github.com/Arshtyi/RoyaleAnalyze (A simple Python script predecessor of this 2.0 version)
 * @dependencies Spring Boot, JDK 21, Maven, and other libraries listed in pom.xml
 * @api Clash Royale API (https://developer.clashroyale.com/#/)
 * @startDate 2024/02/06
 * @completionDate 2024/04/22
 * @version 0.0.5
 * @license Apache License 2.0 (https://github.com/Arshtyi/RoyaleAnalyze-2.0/blob/main/LICENSE)
 * @documentation Please refer to README.md in the project root directory
 * @changeLog Please refer to CHANGELOG.md in the project root directory
 * 
 * Usage restrictions:
 * - Personal use is permitted and encouraged
 * - Commercial use is not permitted
 * 
 * For ideas, suggestions, or bug reports, please contact the author using the provided contact information.
 * 
 * By Arshtyi
 * On 2024/04/22
 */
package org.arshtyi.royaleanalyze2.royaleanalyze2;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.arshtyi.royaleanalyze2.royaleanalyze2.check.Check;
import org.arshtyi.royaleanalyze2.royaleanalyze2.updateinformation.CreateInformation;
import org.arshtyi.royaleanalyze2.royaleanalyze2.extern.Externs;
import org.arshtyi.royaleanalyze2.royaleanalyze2.user.Judge;

/**
 * Main class for the Royaleanalyze2 application.
 * Initializes the Spring Boot application and orchestrates the main application
 * logic.
 * 
 * @SpringBootApplication indicates that this is a Spring Boot application.
 */
@SpringBootApplication
public class Royaleanalyze2Application {
    /**
     * Main entry point for the application.
     * Initializes the Spring Boot application with banner mode disabled,
     * and executes the main application logic sequence.
     * 
     * @param args
     *             Command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Royaleanalyze2Application.class);
        springApplication.setBannerMode(Banner.Mode.OFF);

        // Initialize and run the application context
        ConfigurableApplicationContext context = springApplication.run(args);
        System.out.println("Royaleanalyze2 application started successfully...");

        try {
            /**
             * Perform environment validation:
             * 1. Check the log path configuration
             * 2. Validate the overall environment configuration
             * 
             * If critical configuration issues are detected, the application will exit.
             * If configurations are valid or can be automatically corrected, execution
             * continues.
             */
            Check.checkLog();
            Check.main();

            /**
             * Initialize and create configuration information used throughout the
             * application.
             * This establishes the external information needed for API interactions.
             */
            CreateInformation.main();

            /**
             * Execute all operations sequentially.
             * This design supports the headless nature of the tool, making it suitable
             * for automated execution without requiring GUI interaction.
             */
            String[] ops = new String[Externs.getOperationsDefinitions().size()];
            int index = 0;
            for (String op : Externs.getOperationsDefinitions().keySet()) {
                ops[index++] = op;
            }

            // Process all operations sequentially
            Judge.main(ops);

        } catch (Exception e) {
            // Log any exceptions that occur during execution
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            // Perform cleanup and resource management
            Check.checkResources();

            // Gracefully exit the application
            System.out.println("Exiting application...");
            SpringApplication.exit(context, () -> 0);
        }
    }
}