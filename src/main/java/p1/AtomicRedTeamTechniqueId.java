package com.simplilearn.mavenproject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtomicRedTeamTechniqueId {
    private static final String ATOMICS_URL = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics";
    private static final Pattern TECHNIQUE_ID_PATTERN = Pattern.compile("T\\d+(\\.\\d+)?");
    private static final Logger LOGGER = LoggerFactory.getLogger(AtomicRedTeamTechniqueId.class.getName());

    public List<String> crawlTechniqueIds() throws IOException {
        List<String> techniqueIds = new ArrayList<>();

        try {
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
                    if (TECHNIQUE_ID_PATTERN.matcher(name).matches()) {
                        techniqueIds.add(name);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error( "Failed to fetch web page from URL " + ATOMICS_URL + ". Please check your internet connection and try again.");
            throw e;
        }

        return techniqueIds;
    }

    public String extractTechniqueId(String fileName) {
        Matcher matcher = TECHNIQUE_ID_PATTERN.matcher(fileName);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }  
    }
}
