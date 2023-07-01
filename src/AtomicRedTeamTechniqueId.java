
package com.simplilearn.mavenproject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtomicRedTeamTechniqueId {
	private static final String ATOMICS_URL = "https://github.com/redcanaryco/atomic-red-team/tree/master/atomics";
	private static final String TECHNIQUE_ID_PATTERN = "^T\\d{4}$";
	private static final String SUBTECHNIQUE_ID_PATTERN = "^T\\d{4}\\.\\d{3}$";
    private static final String IGNORED_FILES_PATTERN_START = "^00-";
    private static final String IGNORED_FILES_PATTERN_END = "^mordor_";


    public List<String> crawlTechniqueIds() throws IOException {
        List<String> techniqueIds = new ArrayList<>();

        // Fetch the web page and parse it using jsoup
        Document doc = Jsoup.connect(ATOMICS_URL).get();

        // Extract all anchor tags with the "js-navigation-open" class
        Elements links = doc.select("a.js-navigation-open");

        // Loop through all the links and extract the technique and sub-technique IDs from the file name
        Pattern techIdPattern = Pattern.compile(TECHNIQUE_ID_PATTERN);
        Pattern subtechIdPattern = Pattern.compile(SUBTECHNIQUE_ID_PATTERN);
        Pattern ignoredFilesPatternStart = Pattern.compile(IGNORED_FILES_PATTERN_START);
        Pattern ignoredFilesPatternEnd = Pattern.compile(IGNORED_FILES_PATTERN_END);
     
        boolean shouldContinue = false;
        for (Element link : links) {
            String href = link.attr("href");

            // Filter out the first and last files
            if (href.equals("/redcanaryco/atomic-red-team/tree/master/atomics") || href.equals("/redcanaryco/atomic-red-team/tree/master/atomics/")) {
                shouldContinue = true;
            }

            // Filter out files that match the ignored files patterns
            String fileName = Paths.get(href).getFileName().toString();
            if (!shouldContinue && (ignoredFilesPatternStart.matcher(fileName).find() || ignoredFilesPatternEnd.matcher(fileName).find())) {
                shouldContinue = true;
            }

            if (!shouldContinue) {
                // Extract the technique and sub-technique IDs from the file name
                String techniqueId = extractTechniqueId(fileName, techIdPattern);
                String subtechniqueId = extractTechniqueId(fileName, subtechIdPattern);
                if (techniqueId != null) {
                    techniqueIds.add(techniqueId);
                }
                if (subtechniqueId != null) {
                    techniqueIds.add(subtechniqueId);
                }
            }

            shouldContinue = false;
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
