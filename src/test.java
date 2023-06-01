import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class test {
    public static void main(String[] args) throws Exception{
        Document doc = Jsoup.connect("https://github.com/redcanaryco/atomic-red-team/tree/master/atomics").get();
        Elements elements = doc.select("a.Link--primary[href*='/atomics/']");
        ArrayList<String> text = new ArrayList<String>();
        for (Element element : elements) {
            text.add(element.text());
        }
        System.out.println(text);
    String res = "";
    for(String i : text){
        String url = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics/"+ i;
        Document d = Jsoup.connect(url).get();
        String cssquery = "a.Link--primary[href*='/atomics/"+i+"/']";
        Elements e = d.select(cssquery);
        res += e.text() + "\n";
    }
    System.out.println(res);
}
}