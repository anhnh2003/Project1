package com.simplilearn.mavenproject;

import java.util.List;


public class App {
    public static void main(String[] args) throws Exception {
        AtomicRedTeamDataExtractor dataExtractor = new AtomicRedTeamDataExtractor();
      
        AtomicRedTeamTechniqueId techniques = new AtomicRedTeamTechniqueId();
        
       /* List<String> techniqueIds = techniques.crawlTechniqueIds();
        System.out.println("All technique and sub-technique IDs:");
        int count = 0;
        for (String id : techniqueIds) {
            System.out.println(id);
            count += 1;
        }
        System.out.print("Number of techniques and subtechniques in AtomicRedTeam: " + count);
        */
        
        List<String> techniqueIds = techniques.crawlTechniqueIds();
        for (String techniqueId : techniqueIds) {
            AtomicRedTeamTechnique technique = dataExtractor.fetchTechnique(techniqueId);
            System.out.println(technique.formatAsText());
        }
    }
}

