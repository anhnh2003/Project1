package com.simplilearn.mavenproject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MitreATTCKCrawler {
    private static final String URL = "https://attack.mitre.org/techniques/enterprise/";

    private List<MitreTechnique> techniques;

    public MitreATTCKCrawler() {
        techniques = new ArrayList<>();
    }

    public void crawlMitreTechniques() {
        try {
            // Validate and sanitize URL
            Connection.Response response = Jsoup.connect(URL)
                    .timeout(5000)
                    .followRedirects(true)
                    .execute();

            // Fetch the HTML content
            Document doc = response.parse();

            // Find all technique and sub-technique rows
            Elements rows = doc.select("tr.technique, tr.sub");

            // Process each row
            String previousID = "";
            String previousName = "";
            for (Element row : rows) {
                // Extract the ID and name
                String id = extractText(row, "td a[href^='/techniques/']");
                String name = "";

                if (row.hasClass("sub")) {
                    name = extractText(row, "td:nth-child(3) a");
                } else {
                    name = extractText(row, "td:nth-child(2) a");
                }

                // Modify ID and name if necessary
                if (id.startsWith(".")) {
                    if (previousID.startsWith("T")) {
                        id = previousID +"."+ id.substring(1);
                        name = previousName + ": " + name;
                    }
                } else {
                    previousID = id;
                    previousName = name;
                }

                // Create Technique object and add it to the list
                MitreTechnique technique = new MitreTechnique(id, name);
                techniques.add(technique);
            }
        } catch (IOException e) {
            // Handle any IOException appropriately without revealing sensitive information
            System.err.println("An error occurred while connecting to the website.");
            e.printStackTrace();
        }
    }

    // Helper method to extract text from HTML element
    private String extractText(Element element, String selector) {
        Element link = element.selectFirst(selector);
        return link != null ? link.text().trim() : "";
    }

    public List<MitreTechnique> getMitreTechniques() {
        return techniques;
    }

}

// Technique class to represent a technique
class MitreTechnique {
    private String id;
    private String name;

    public MitreTechnique(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

