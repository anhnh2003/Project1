import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
/* 
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
*/
public class test {
/* 
    public static void main(String[] args) throws Exception{
        /*Document doc = Jsoup.connect("https://github.com/redcanaryco/atomic-red-team/tree/master/atomics").get();
        Elements elements = doc.select("a.Link--primary[href*='/atomics/']");
        ArrayList<String> text = new ArrayList<String>();
        for (Element element : elements) {
            text.add(element.text());
        }
        ArrayList<String> res = new ArrayList<String>();
        for(String i : text){
            String url = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics/"+ i;
            Document d = Jsoup.connect(url).get();
            String cssquery = "a.Link--primary[href*='/atomics/"+i+"/']";
            Elements e = d.select(cssquery);
            res.add(e.text() + "\n");
        }*/
        /* 
        String u = "https://raw.githubusercontent.com/redcanaryco/atomic-red-team/master/atomics/T1003.001/T1003.001.md";
        // đây là dạng link lưu trữ file github dưới dạng rawtext -> toàn bộ thông tin text đều đc lưu trữ trong thẻ <pre> của file html này
        System.out.println(Jsoup.connect(u).get()); // lấy toàn bộ nội dung html 
    }
    /**
    đang tính tạo hàm này để lấy thông tin từ link raw ở trên
    tuy nhiên bằng 1 cách nào đó mà nó k lấy đc thẻ <pre>. Đã tư duy & tra gg+AI and đang khắc phục
    Nếu thành công thì mình đã lấy được nội dung url dưới dạng raw text. Nhưng vẫn lặp đệ quy đến các link con để lấy tiếp content của các link đấy.
     */
  /*   public static String rawText(String url) throws Exception{
        Document document = Jsoup.connect(url).get();
        Elements rawText = document.select("pre");
        return rawText.text();
    }
}*/
public static void main(String[] args) {
    String urlStr = "https://raw.githubusercontent.com/redcanaryco/atomic-red-team/master/atomics/T1003.001/T1003.001.md";
    try {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        InputStream content = connection.getInputStream();
        String fileContent = inputStreamToString(content);
        System.out.println(fileContent);
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static String inputStreamToString(InputStream is) throws IOException {
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
