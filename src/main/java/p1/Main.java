package p1;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
  public static void main(String[] args) throws Exception {
    String url = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics";
    Document doc = Jsoup.connect(url).get();

    // Select the JSON payload
    String jsonStr = doc.select("body")
                       .select("script[type='application/json']")
                       .first()
                       .html()
                       .trim();

    // Parse the JSON payload as a JSONObject
    JSONObject json = new JSONObject(jsonStr);

    // Get the tree items as a JSONArray
    JSONArray treeItems = json.getJSONObject("payload")
                             .getJSONObject("tree")
                             .getJSONArray("items");

    // Extract the IDs from the tree items
    for (int i = 0; i < treeItems.length(); i++) {
      JSONObject item = treeItems.getJSONObject(i);
      String name = item.getString("name");
      if (name.matches("T\\d+(\\.\\d+)?")) {
        System.out.println(name);
      }
    }
  }
}