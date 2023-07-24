package com.simplilearn.mavenproject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

public class AtomicRedTeamTechniqueId {
	private static final String ATOMICS_URL = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics";

	public List<String> crawlTechniqueIds() throws IOException {
	    List<String> techniqueIds = new ArrayList<>();
	    
	    // Fetch the web page and parse it using jsoup
	    Document doc = Jsoup.connect(ATOMICS_URL).get();

	    // Select the JSON payload
	    Element scriptElement = doc.select("body")
	                    .select("script[type='application/json']")
	                    .first();

	    if (scriptElement != null) {
	        String jsonStr = scriptElement.html().trim();

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
	                techniqueIds.add(name);
	            }
	        }
	    }
	    return techniqueIds;
	}

    public String extractTechniqueId(String fileName, Pattern idPattern) {
        Matcher matcher = idPattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }  
    }
}
