package com.mahghuuuls.zenworks.docs;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Keeps the catalog index and the entries in step: every name in the index has an entry, every
 * entry is in the index, and every entry has a runnable example. Reads the real files under
 * {@code docs/methods}; Gradle runs tests from the project folder.
 */
class CatalogIndexTest {

    private static final File METHODS = new File("docs/methods");
    private static final Pattern INDEX_ROW = Pattern.compile("^\\| \\[?`([A-Za-z]+)`");
    private static final Pattern LINK = Pattern.compile("\\]\\(([a-z-]+\\.md)#([a-z]+)\\)");
    private static final Pattern ENTRY_HEADING = Pattern.compile("^#{2,3} ([A-Za-z]+)$");

    @Test
    void indexAndEntriesNameTheSameMethods() throws IOException {
        Set<String> indexed = new TreeSet<>();
        for (String line : read(new File(METHODS, "index.md"))) {
            Matcher row = INDEX_ROW.matcher(line);
            if (row.find()) {
                assertTrue(indexed.add(row.group(1)), "index lists " + row.group(1) + " twice");
                Matcher link = LINK.matcher(line);
                assertTrue(link.find(), "index row for " + row.group(1) + " has no page link");
                assertEquals(row.group(1).toLowerCase(java.util.Locale.ROOT), link.group(2), "index row for " + row.group(1) + " links to the wrong anchor");
                assertTrue(new File(METHODS, link.group(1)).isFile(), "index row for " + row.group(1) + " links to a missing page");
            }
        }
        Set<String> entries = new TreeSet<>();
        for (File page : pages()) {
            for (String line : read(page)) {
                Matcher heading = ENTRY_HEADING.matcher(line);
                if (heading.matches()) {
                    entries.add(heading.group(1));
                }
            }
        }
        assertFalse(indexed.isEmpty(), "no index rows found; the parse is broken");
        assertEquals(indexed, entries);
    }

    @Test
    void everyEntryHasARunnableExample() throws IOException {
        for (File page : pages()) {
            List<String> lines = read(page);
            int start = -1;
            String entry = null;
            for (int i = 0; i <= lines.size(); i++) {
                String line = i < lines.size() ? lines.get(i) : "## end";
                if (line.startsWith("## ") || line.startsWith("### ")) {
                    if (entry != null) {
                        List<String> section = lines.subList(start, i);
                        assertFalse(ExampleScriptGenerator.extract(section, page.getName()).isEmpty(),
                                page.getName() + ": entry " + entry + " has no runnable example");
                    }
                    Matcher heading = ENTRY_HEADING.matcher(line);
                    entry = heading.matches() ? heading.group(1) : null;
                    start = i;
                }
            }
        }
    }

    private static List<File> pages() {
        List<File> pages = new ArrayList<>();
        File[] files = METHODS.listFiles((dir, name) -> name.endsWith(".md") && !name.equals("index.md"));
        assertTrue(files != null && files.length > 0, "no catalog pages found in " + METHODS.getAbsolutePath());
        for (File file : files) {
            pages.add(file);
        }
        return pages;
    }

    private static List<String> read(File file) throws IOException {
        return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    }
}
