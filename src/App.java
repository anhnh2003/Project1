package com.simplilearn.mavenproject;

import java.util.List;
import java.util.ArrayList;


public class App {
    public static void main(String[] args) throws Exception {
        AtomicRedTeamDataExtractor dataExtractor = new AtomicRedTeamDataExtractor();
      
        AtomicRedTeamTechniqueId techniques = new AtomicRedTeamTechniqueId();

        List<List<Object>> data = new ArrayList<>();

        // Add header row
        data.add(List.of("Technique ID", "Technique Name", "Test Case", "Supported Platforms"));

        List<String> techniqueIds = techniques.crawlTechniqueIds();
        for (String techniqueId : techniqueIds) {
            AtomicRedTeamTechnique technique = dataExtractor.fetchTechnique(techniqueId);
            String techniqueName = technique.getTechniqueName();

            for (AtomicRedTeamTestCase atomicTest : technique.getAtomicTests()) {
                List<Object> row = new ArrayList<>();
                row.add(techniqueId);
                row.add(techniqueName);
                row.add(atomicTest.getName());
                row.add(String.join(", ", atomicTest.getSupportedPlatforms()));
                data.add(row);
        }
    }
        
        AtomicExcelExporter.exportToExcel(data, "C:\\Users\\Admin\\Documents\\atomic_red_team_data.xlsx");

  }
}
   

