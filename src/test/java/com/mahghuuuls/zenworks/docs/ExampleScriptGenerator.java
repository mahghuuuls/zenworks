package com.mahghuuuls.zenworks.docs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Turns the catalog's runnable examples into one development script, so the examples are written
 * once, in the catalog, and still run against the build they document.
 *
 * <p>A runnable example is a fenced block opened with {@code ```zenscript example=<id>} at the start
 * of a line. Its code may use {@code player} (the {@code IPlayer} who clicked) and {@code target}
 * (the clicked {@code IEntity}). When a player right-clicks an entity with a stick in the main
 * hand, every example runs on each side, in its own event handler so one failing example does not
 * stop the others, between {@code [ZW-EX][<side>] <id> begin} and {@code end} lines.
 *
 * <p>Development only: this class lives in the test source set and never ships in the mod jar.
 */
public final class ExampleScriptGenerator {

    private static final Pattern OPENING = Pattern.compile("^```zenscript\\s+example=([a-z0-9-]+)\\s*$");
    private static final String CLOSING = "```";

    private ExampleScriptGenerator() {
    }

    /** One runnable example: its id and its code lines. */
    static final class Example {
        final String id;
        final List<String> lines;

        Example(String id, List<String> lines) {
            this.id = id;
            this.lines = lines;
        }
    }

    /**
     * Reads the runnable examples of one Markdown document, in order. A fence that mentions
     * {@code example} but does not match the convention is refused rather than skipped, so a
     * mistyped tag cannot silently drop an example from the run.
     */
    static List<Example> extract(List<String> markdown, String source) {
        List<Example> examples = new ArrayList<>();
        for (int i = 0; i < markdown.size(); i++) {
            String line = markdown.get(i);
            Matcher opening = OPENING.matcher(line);
            if (!opening.matches()) {
                if (line.trim().startsWith("```") && line.contains("example")) {
                    throw new IllegalArgumentException(source + ":" + (i + 1) + ": malformed example fence: " + line.trim());
                }
                continue;
            }
            List<String> code = new ArrayList<>();
            int j = i + 1;
            while (j < markdown.size() && !markdown.get(j).trim().equals(CLOSING)) {
                code.add(markdown.get(j));
                j++;
            }
            if (j == markdown.size()) {
                throw new IllegalArgumentException(source + ":" + (i + 1) + ": example '" + opening.group(1) + "' has no closing fence");
            }
            examples.add(new Example(opening.group(1), code));
            i = j;
        }
        return examples;
    }

    /** Writes the development script for the given examples. */
    static List<String> script(List<Example> examples) {
        Set<String> ids = new HashSet<>();
        for (Example example : examples) {
            if (!ids.add(example.id)) {
                throw new IllegalArgumentException("duplicate example id '" + example.id + "'");
            }
        }
        List<String> out = new ArrayList<>();
        out.add("#loader crafttweaker");
        out.add("// Generated from docs/methods by the exampleScript Gradle task. Do not edit.");
        out.add("import crafttweaker.event.PlayerInteractEntityEvent;");
        out.add("import crafttweaker.entity.IEntity;");
        out.add("import crafttweaker.player.IPlayer;");
        out.add("");
        out.add("function zwExampleTriggered(event as PlayerInteractEntityEvent) as bool {");
        out.add("    return event.hand == \"MAIN_HAND\" && !isNull(event.item) && <minecraft:stick>.matches(event.item);");
        out.add("}");
        out.add("");
        out.add("function zwExampleSide(event as PlayerInteractEntityEvent) as string {");
        out.add("    return event.world.remote ? \"client\" : \"server\";");
        out.add("}");
        out.add("");
        for (Example example : examples) {
            out.add("function " + functionName(example.id) + "(player as IPlayer, target as IEntity) {");
            for (String line : example.lines) {
                out.add(line.isEmpty() ? "" : "    " + line);
            }
            out.add("}");
            out.add("");
        }
        out.add("print(\"[ZW-EX] loaded " + examples.size() + " examples\");");
        for (Example example : examples) {
            out.add("");
            out.add("events.onPlayerInteractEntity(function(event as PlayerInteractEntityEvent) {");
            out.add("    if (zwExampleTriggered(event)) {");
            out.add("        print(\"[ZW-EX][\" ~ zwExampleSide(event) ~ \"] " + example.id + " begin\");");
            out.add("        " + functionName(example.id) + "(event.player, event.target);");
            out.add("        print(\"[ZW-EX][\" ~ zwExampleSide(event) ~ \"] " + example.id + " end\");");
            out.add("    }");
            out.add("});");
        }
        return out;
    }

    static String functionName(String id) {
        return "zwExample_" + id.replace('-', '_');
    }

    /** Arguments: the catalog folder and the script file to write. */
    public static void main(String[] args) throws IOException {
        File folder = new File(args[0]);
        File output = new File(args[1]);
        File[] documents = folder.listFiles((dir, name) -> name.endsWith(".md"));
        if (documents == null) {
            throw new IOException("not a folder: " + folder);
        }
        Arrays.sort(documents);
        List<Example> examples = new ArrayList<>();
        for (File document : documents) {
            examples.addAll(extract(Files.readAllLines(document.toPath(), StandardCharsets.UTF_8), document.getName()));
        }
        File parent = output.getParentFile();
        if (parent != null && !parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("cannot create " + parent);
        }
        Files.write(output.toPath(), script(examples), StandardCharsets.UTF_8);
        System.out.println("Wrote " + examples.size() + " examples to " + output);
    }
}
