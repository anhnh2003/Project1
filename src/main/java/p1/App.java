package com.simplilearn.mavenproject;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        // Construct the Atomic Red Team and Mitre ATT&CK crawlers class
        MitreATTCKCrawler mitre = new MitreATTCKCrawler();
        AtomicRedTeamDataExtractor dataExtractor = new AtomicRedTeamDataExtractor();
        AtomicRedTeamTechniqueId atomicTechniqueId = new AtomicRedTeamTechniqueId();
        List<String> techniqueIds = null; // initialize to null, in case of exception
        
        try {
            techniqueIds = atomicTechniqueId.crawlTechniqueIds();
            mitre.crawlMitreTechniques();
            List<List<Object>> redteamData = new ArrayList<>();
        } catch (UnknownHostException e) {
            // Handle the exception
            logger.error("Failed to fetch technique IDs. Please check your internet connection and try again.");
            return; // Exit the program or handle the error appropriately
        }
        
        List<List<Object>> redteamData = new ArrayList<>();
        // Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Add a boolean variable to track if option 5 has been selected
        boolean option5Selected = false;

        // Add a while loop to ask the user for choices until they choose 6
        int choice = 0;
        while (choice != 6) {
            // Display the options to the user
            System.out.println("===============================================================");
            System.out.println("Welcome to the Atomic Red Team and Mitre ATT&CK Crawler!");
            System.out.println("Please select an option:");
            System.out.println("1. Display Atomic Red Team Data.");
            System.out.println("2. Display Mitre ATT&CK.");
            System.out.println("3. Export Atomic Red Team to Excel.");
            System.out.println("4. Export Mitre ATT&CK to Excel");
            if (!option5Selected) {
                System.out.println("5. Compare Atomic Red Team and Mitre ATT&CK");
            } else {
                System.out.println("5. Compare Atomic Red Team and Mitre ATT&CK (already selected)");
            }
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        System.out.println("Crawling Atomic Red Team...");
                        for (String techniqueId : techniqueIds) {
                            System.out.println("Atomic Technique ID:" + techniqueId);
                        }
                        break;
                    case 2:
                        System.out.println("Crawling Mitre ATT&CK...");
                        for (MitreTechnique technique : mitre.getMitreTechniques()) {
                            System.out.println(String.format("MitreTechniqueID: %s\n", technique.getId()));
                        }
                        break;
                    case 3:
                        System.out.println("Exporting Atomic Red Team to Excel...");
                        // add headers of the excel file
                        redteamData.add(List.of("Technique ID", "Technique Name", "Test Case", "Supported Platforms"));
                        for (String techniqueId : techniqueIds) {
                            AtomicRedTeamTechnique technique = dataExtractor.fetchTechnique(techniqueId);
                            String techniqueName = technique.getTechniqueName();
                            // add data to each row
                            for (AtomicRedTeamTestCase atomicTest : technique.getAtomicTests()) {
                                List<Object> row = new ArrayList<>();
                                row.add(techniqueId);
                                row.add(techniqueName);
                                row.add(atomicTest.getName());
                                row.add(String.join(", ", atomicTest.getSupportedPlatforms()));
                                redteamData.add(row);
                            }
                        }
                        String redteamFilename = "atomic_red_team_data.xlsx";
                        try {
                            AtomicExcelExporter.exportToExcel(redteamData, redteamFilename);
                            System.out.println("Atomic Red Team data exported to " + redteamFilename);
                        } catch (IOException e) {
                            logger.error("An error occurred while exporting Mitre techniques to Excel: {}", e.getMessage(), e);
                        } catch (RuntimeException e) {
                            logger.error("An error occurred while exporting Mitre techniques to Excel.", e);
                        }
                        break;
                    case 4:
                        System.out.println("Exporting Mitre ATT&CK to Excel...");
                        List<List<Object>> mitreTechniqueData = new ArrayList<>();
                        mitreTechniqueData.add(List.of("Mitre Technique ID", "Mitre Technique Name"));
                        for (MitreTechnique technique : mitre.getMitreTechniques()) {
                            List<Object> row = new ArrayList<>();
                            row.add(technique.getId());
                            row.add(technique.getName());
                            mitreTechniqueData.add(row);
                        }

                        String mitreFilename = "mitre_techniques.xlsx";
                        try {
                            AtomicExcelExporter.exportToExcel(mitreTechniqueData, mitreFilename);
                            System.out.println("Mitre techniques exported to " + mitreFilename);
                        } catch (IOException e) {
                            logger.error("An error occurred while exporting Mitre techniques to Excel: {}", e.getMessage(), e);
                        } catch (RuntimeException e) {
                            logger.error("An error occurred while exporting Mitre techniques to Excel.", e);
                        } 
                                break;
                    case 5:
                        if (!option5Selected) {
                        	List<String> mitreTechnique = new ArrayList<>() ;
                            for ( MitreTechnique i : mitre.getMitreTechniques()) {
                                mitreTechnique.add(i.getId());        	
                            }
                            System.out.println("Drawing Coverage Chart of Atomic Red Team and Mitre ATT&CK...");
                            System.out.println("Widen the pop-up window to see the full chart.");
                            Chart chart = new Chart(mitreTechnique, techniqueIds);
                            chart.drawChart();
                            option5Selected = true;
                        } else {
                            System.out.println("You have already selected option 5.");
                        }
                        break;
                    case 6:
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        // Close the scanner
        scanner.close();
    }
}

