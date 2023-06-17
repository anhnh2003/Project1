import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class MitreATTCrawler{
    private String urlStr = "";
    private Map<String, String> technique = new HashMap<>();
    public Map<String,String> getTechnique(){
        return this.technique;
    }
    public void webCrawler(String url) throws Exception{
        Document doc = Jsoup.connect(url).get();
        Elements e = doc.selectFirst("table[class='table-techniques']").select("tr[class=technique]").select("td").select("a[href*='/techniques/']");
        String[] techniqueName = e.text().split(" ");
        for(int i = 0; i<techniqueName.length; i+=3){
            technique.put(techniqueName[i], (techniqueName[i+1]+techniqueName[i+2]));
        }
        System.out.println(e.text());
        System.out.println(technique);
    }
}
