package com.simplilearn.mavenproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);	
    public static void main(String[] args) throws IOException, MitreATTCKCrawler {
        MitreATTCKCrawler mitre = new MitreATTCKCrawler();
        mitre.crawlMitreTechniques();
        // Print the IDs and names for Mitre data
      /*  for (MitreTechnique technique : mitre.getMitreTechniques()) {
            System.out.println(String.format("TechniqueID: %s\n", technique.getId()));
        }*/
        

        // Export Mitre techniques to Excel file
        List<List<Object>> mitreTechniqueData = new ArrayList<>();
        mitreTechniqueData.add(List.of("Technique ID", "Technique Name"));
        for (MitreTechnique technique : mitre.getMitreTechniques()) {
            List<Object> row = new ArrayList<>();
            row.add(technique.getId());
            row.add(technique.getName());
            mitreTechniqueData.add(row);
        }

        String mitreFilename = "mitre_techniques.xlsx";
        try {
            AtomicExcelExporter.exportToExcel(mitreTechniqueData, mitreFilename);
        } catch (IOException e) {
            // Handle the exception
        } catch (Exception e) {
            logger.error("An error occurred while exporting Mitre techniques to Excel.", e);     
        } 
        String excelFilePath = "C:\\Users\\Admin\\Documents\\atomic_red_team_data.xlsx";
        int columnIndex = 0; // Assuming Technique ID is in the first column
        List<String> techniqueIds = ExcelDataImporter.importDataFromExcel(excelFilePath, columnIndex);

        AtomicRedTeamDataExtractor dataExtractor = new AtomicRedTeamDataExtractor();
        AtomicRedTeamTechniqueId techniques = new AtomicRedTeamTechniqueId();
        List<List<Object>> arteamData = new ArrayList<>();

        // Add header row for Atomic Red Team data
        arteamData.add(List.of("Technique ID", "Technique Name", "Test Case", "Supported Platforms"));

        List<String> atomicTechniqueIds = techniques.crawlTechniqueIds();
     /*    for (String techniqueId : atomicTechniqueIds) {
            AtomicRedTeamTechnique technique = dataExtractor.fetchTechnique(techniqueId);
            String techniqueName = technique.getTechniqueName();
            for (AtomicRedTeamTestCase atomicTest : technique.getAtomicTests()) {
                List<Object> row = new ArrayList<>();
                row.add(techniqueId);
                row.add(techniqueName);
                row.add(atomicTest.getName());
                row.add(String.join(", ", atomicTest.getSupportedPlatforms()));
                arteamData.add(row);     
        }

        // Export Atomic Red Team data to Excel file
        String arteamFilename = "atomic_red_team_data.xlsx";
        try {
            AtomicExcelExporter.exportToExcel(arteamData, arteamFilename);
            System.out.println("Atomic Red Team data exported to " + arteamFilename);
        } catch (IOException e) {
            System.err.println("An error occurred while exporting Atomic Red Team data to Excel: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("An error occurred while exporting Atomic Red Team data to Excel.");
            e.printStackTrace();
        } */
        List<String> mitreTechnique = new ArrayList<>() ;
        for ( MitreTechnique i : mitre.getMitreTechniques()) {
        	mitreTechnique.add(i.getId());        	
        }
        mitre.crawlMitreTechniques();
        System.out.println(mitre.getMitreTechniques());
        Chart chart = new Chart(mitreTechnique, techniqueIds);
        chart.drawChart();

    }
  }


