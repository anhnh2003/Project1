package p1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        /// Construct the Atomic Red Team and Mitre ATT&CK crawlers class
        MitreATTCKCrawler mitre = new MitreATTCKCrawler();
        AtomicRedTeamDataExtractor dataExtractor = new AtomicRedTeamDataExtractor();
        AtomicRedTeamTechniqueId atomicTechniqueId = new AtomicRedTeamTechniqueId();
        List<String> techniqueIds = atomicTechniqueId.crawlTechniqueIds();
        mitre.crawlMitreTechniques();
        List<List<Object>> redteamData = new ArrayList<>();
        ///ask user what they want to do
        //1. crawl atomic
        //2. crawl mitre
        //3. export atomic to excel
        //4. export mitre to excel
        //5. compare atomic and mitre
        //6. exit
        System.out.println("Welcome to the Atomic Red Team and Mitre ATT&CK Crawler!");
        System.out.println("Please select an option:");
        System.out.println("1. Display Atomic Red Team Data");
        System.out.println("2. Display Mitre ATT&CK");
        System.out.println("3. Export Atomic Red Team to Excel");
        System.out.println("4. Export Mitre ATT&CK to Excel");
        System.out.println("5. Compare Atomic Red Team and Mitre ATT&CK");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(System.console().readLine());
        do {
            if (choice < 1 || choice > 6) {
                System.out.println("Invalid choice!");
                System.out.print("Enter your choice: ");
                choice = Integer.parseInt(System.console().readLine());
            }
        } while (choice < 1 || choice > 6);
        switch (choice) {
            case 1:
                System.out.println("Crawling Atomic Red Team...");
                System.out.println(techniqueIds);
                break;
            case 2:
                System.out.println("Crawling Mitre ATT&CK...");
                for (MitreTechnique technique : mitre.getMitreTechniques()) {
                    System.out.println(String.format("MitreTechniqueID: %s\n", technique.getId()));
                }
                break;
            case 3:
                System.out.println("Exporting Atomic Red Team to Excel...");
                /// add headers of the excel file
                redteamData.add(List.of("Technique ID", "Technique Name", "Test Case", "Supported Platforms"));
                for (String techniqueId : techniqueIds) {
                    AtomicRedTeamTechnique technique = dataExtractor.fetchTechnique(techniqueId);
                    String techniqueName = technique.getTechniqueName();
                    /// add data to each row
                    for (AtomicRedTeamTestCase atomicTest : technique.getAtomicTests()) {
                        List<Object> row = new ArrayList<>();
                        row.add(techniqueId);
                        row.add(techniqueName);
                        row.add(atomicTest.getName());
                        row.add(String.join(", ", atomicTest.getSupportedPlatforms()));
                        redteamData.add(row);
                    
                }}
                String redteamFilename = "atomic_red_team_data.xlsx";
                try {
                    AtomicExcelExporter.exportToExcel(redteamData, redteamFilename);
                    System.out.println("Atomic Red Team data exported to " + redteamFilename);
                } catch (IOException e) {
                    System.err.println("An error occurred while exporting Atomic Red Team data to Excel: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.err.println("An error occurred while exporting Atomic Red Team data to Excel.");
                    e.printStackTrace();
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
                    System.err.println("An error occurred while exporting Mitre techniques to Excel: " + e.getMessage());
                } catch (RuntimeException e) {
                    System.err.println("An error occurred while exporting Mitre techniques to Excel.");
                    e.printStackTrace();
                } 
                        break;
            case 5:
                List<String> mitreTechnique = new ArrayList<>() ;
                for ( MitreTechnique i : mitre.getMitreTechniques()) {
                    mitreTechnique.add(i.getId());        	
                }
                System.out.println("Drawing Coverage Chart of Atomic Red Team and Mitre ATT&CK...");
                System.out.println("Widen the pop-up window to see the full chart.");
                Chart chart = new Chart(mitreTechnique, techniqueIds);
                chart.drawChart();
                break;
            case 6:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }

    }

}