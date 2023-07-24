package com.simplilearn.mavenproject;
import java.util.List;

public class AtomicRedTeamTechnique {
    private String techniqueId;
    private String techniqueName;
    private List<AtomicRedTeamTestCase> atomicTests;

    public AtomicRedTeamTechnique(String techniqueId, String techniqueName, List<AtomicRedTeamTestCase> atomicTests) {
        this.techniqueId = techniqueId;
        this.techniqueName = techniqueName;
        this.atomicTests = atomicTests;
    }

    public String formatAsText() {
        StringBuilder sb = new StringBuilder();
        sb.append("TechniqueID: ").append(techniqueId).append("\n");
        sb.append("TechniqueName: ").append(techniqueName).append("\n");
        sb.append("Atomic_tests: {\n");
        for (AtomicRedTeamTestCase atomicTest : atomicTests) {
            sb.append("\t").append(atomicTest.getName()).append(": {\n");
            sb.append("\t\tSupported_platforms: ").append(atomicTest.getSupportedPlatforms()).append("\n");
            sb.append("\t}\n");
        }
        sb.append("}\n\n");
        return sb.toString();
    }

	public String getTechniqueName() {
		return techniqueName;
	}

	public List<AtomicRedTeamTestCase> getAtomicTests() {
	
		return atomicTests;
	}
}
