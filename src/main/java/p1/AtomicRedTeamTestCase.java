package p1;
import java.util.List;

public class AtomicRedTeamTestCase {
    private String name;
    private List<String> supportedPlatforms;

    public AtomicRedTeamTestCase(String name, List<String> supportedPlatforms) {
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
