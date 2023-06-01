
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
public class App {
    public static void main(String[] args) throws Exception {
        Document page = Jsoup.connect("https://github.com/redcanaryco/atomic-red-team/tree/master/atomics").get();
        Elements pageElement = page.select("a,p,h1,h2,h3,h4,h5,h6");
        System.out.println(pageElement);
    }
}
