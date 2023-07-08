package p1;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.URL;
public class test {

        public static void main(String[] args) throws IOException {
        String url = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics";
        String html = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        String[] lines = html.split("\n");
        for (String line : lines) {
            if (line.contains("aria-describedby=\"item-type-")) {
                int start = line.indexOf("item-type-") + 10;
                int end = line.indexOf("\"", start);
                System.out.println(line.substring(start, end));
            }
        }
    }
}