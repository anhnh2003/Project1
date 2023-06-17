package com.simplilearn.mavenproject;

import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class AtomicRedTeamMetadataExtractor {

    public static void main(String[] args) throws IOException {
        AtomicRedTeamMetadataExtractor metadataExtractor = new AtomicRedTeamMetadataExtractor();
        List<String> techniqueIds = metadataExtractor.crawlTechniqueIds();
        for (String techniqueId : techniqueIds) {
            AtomicRedTeamTechnique technique = metadataExtractor.fetchTechnique(techniqueId);
            System.out.println(technique.formatAsText());
        }
    }

    public List<String> crawlTechniqueIds() throws IOException {
        return AtomicRedTeamTechniques.crawlTechniqueIds();
    }

    public AtomicRedTeamTechnique fetchTechnique(String techniqueId) throws IOException {
        String yamlUrl = "https://raw.githubusercontent.com/redcanaryco/atomic-red-team/master/atomics/" + techniqueId + "/" + techniqueId + ".yaml";
        Map<String, Object> yamlMap = fetchYaml(yamlUrl);
        String techniqueName = (String) yamlMap.get("display_name");
        List<Map<String, Object>> atomicTestMaps = (List<Map<String, Object>>) yamlMap.get("atomic_tests");
        List<AtomicRedTeamAtomicTest> atomicTests = parseAtomicTests(atomicTestMaps);
        return new AtomicRedTeamTechnique(techniqueId, techniqueName, atomicTests);
    }

    private List<AtomicRedTeamAtomicTest> parseAtomicTests(List<Map<String, Object>> atomicTestMaps) {
        List<AtomicRedTeamAtomicTest> atomicTests = new ArrayList<>();
        for (Map<String, Object> atomicTestMap : atomicTestMaps) {
            String atomicTestName = (String) atomicTestMap.get("name");
            List<String> supportedPlatforms = (List<String>) atomicTestMap.get("supported_platforms");
            atomicTests.add(new AtomicRedTeamAtomicTest(atomicTestName, supportedPlatforms));
        }
        return atomicTests;
    }

    private Map<String, Object> fetchYaml(String yamlUrl) throws IOException {
        // Fetch the YAML file from the given URL and parse it using SnakeYAML
        URL url = new URL(yamlUrl);
        Yaml yaml = new Yaml();
        return (Map<String, Object>) yaml.load(url.openStream());
    }

    private static class AtomicRedTeamTechnique {
        private String techniqueId;
        private String techniqueName;
        private List<AtomicRedTeamAtomicTest> atomicTests;

        public AtomicRedTeamTechnique(String techniqueId, String techniqueName, List<AtomicRedTeamAtomicTest> atomicTests) {
            this.techniqueId = techniqueId;
            this.techniqueName = techniqueName;
            this.atomicTests = atomicTests;
        }

        public String formatAsText() {
            StringBuilder sb = new StringBuilder();
            sb.append("TechniqueID: ").append(techniqueId).append("\n");
            sb.append("TechniqueName: ").append(techniqueName).append("\n");
            sb.append("Atomic_tests: {\n");
            for (AtomicRedTeamAtomicTest atomicTest : atomicTests) {
                sb.append("\t").append(atomicTest.getName()).append(": {\n");
                sb.append("\t\tSupported_platforms: ").append(atomicTest.getSupportedPlatforms()).append("\n");
                sb.append("\t}\n");
            }
            sb.append("}\n\n");
            return sb.toString();
        }
    }

    private static class AtomicRedTeamAtomicTest {
        private String name;
        private List<String> supportedPlatforms;

        public AtomicRedTeamAtomicTest(String name, List<String> supportedPlatforms) {
            this.name = name;
            this.supportedPlatforms = supportedPlatforms;
        }

        public String getName() {
            return name;
        }

        public List<String> getSupportedPlatforms() {
            return supportedPlatforms;
        }
    }
}

