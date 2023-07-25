package com.simplilearn.mavenproject;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class AtomicRedTeamDataExtractor {

    public AtomicRedTeamTechnique fetchTechnique(String techniqueId) throws IOException {
        String yamlUrl = "https://raw.githubusercontent.com/redcanaryco/atomic-red-team/master/atomics/" + techniqueId + "/" + techniqueId + ".yaml";
        Map<String, Object> yamlMap = fetchYaml(yamlUrl);
        String techniqueName = (String) yamlMap.get("display_name");
        List<Map<String, Object>> atomicTestMaps = (List<Map<String, Object>>) yamlMap.get("atomic_tests");
        List<AtomicRedTeamTestCase> atomicTests = parseAtomicTests(atomicTestMaps);
        return new AtomicRedTeamTechnique(techniqueId, techniqueName, atomicTests);
    }
    
    private List<AtomicRedTeamTestCase> parseAtomicTests(List<Map<String, Object>> atomicTestMaps) {
        List<AtomicRedTeamTestCase> atomicTests = new ArrayList<>();
        for (Map<String, Object> atomicTestMap : atomicTestMaps) {
            String atomicTestName = (String) atomicTestMap.get("name");
            List<String> supportedPlatforms = (List<String>) atomicTestMap.get("supported_platforms");
            atomicTests.add(new AtomicRedTeamTestCase(atomicTestName, supportedPlatforms));
        }
        return atomicTests;
    }
    
    protected Map<String, Object> fetchYaml(String yamlUrl) throws IOException {
        // Fetch the YAML file from the given URL and parse it using SnakeYAML
        URL url = new URL(yamlUrl);
        Yaml yaml = new Yaml();
        return (Map<String, Object>) yaml.load(url.openStream());
    }   
}

