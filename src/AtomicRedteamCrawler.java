import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
public class AtomicRedteamCrawler {        
    private String urlStr = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics";
    public static void main(String[] args) {
    }
    private ArrayList<String> crawFolder(String url){

    }
    private String crawData(String url){
        
    }
    private String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
    
}
