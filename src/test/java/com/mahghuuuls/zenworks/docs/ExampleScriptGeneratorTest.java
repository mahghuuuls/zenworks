package com.mahghuuuls.zenworks.docs;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExampleScriptGeneratorTest {

    private static final List<String> FIXTURE = Arrays.asList(
            "# Fixture",
            "```zenscript",
            "signature only, not runnable",
            "```",
            "```zenscript example=first-one",
            "val a as int = 1;",
            "",
            "print(\"a \" ~ a);",
            "```",
            "Text.",
            "```zenscript example=second",
            "print(\"b\");",
            "```");

    @Test
    void onlyTaggedFencesAreExamples() {
        List<ExampleScriptGenerator.Example> examples = ExampleScriptGenerator.extract(FIXTURE, "fixture.md");
        assertEquals(2, examples.size());
        assertEquals("first-one", examples.get(0).id);
        assertEquals(Arrays.asList("val a as int = 1;", "", "print(\"a \" ~ a);"), examples.get(0).lines);
        assertEquals("second", examples.get(1).id);
    }

    @Test
    void eachExampleHasItsOwnFunctionAndHandler() {
        List<String> script = ExampleScriptGenerator.script(ExampleScriptGenerator.extract(FIXTURE, "fixture.md"));
        String text = String.join("\n", script);
        assertEquals(1, count(text, "function zwExample_first_one(player as IPlayer, target as IEntity) {"));
        assertEquals(1, count(text, "function zwExample_second("));
        assertEquals(1, count(text, "zwExample_first_one(event.player, event.target);"));
        assertEquals(2, count(text, "events.onPlayerInteractEntity(function(event as PlayerInteractEntityEvent) {"));
        assertEquals(1, count(text, "] first-one begin\");"));
        assertEquals(1, count(text, "] second end\");"));
        assertTrue(text.contains("print(\"[ZW-EX] loaded 2 examples\");"));
        assertTrue(script.contains("    print(\"a \" ~ a);"));
    }

    @Test
    void triggerIsMainHandStickAndSideIsTagged() {
        String text = String.join("\n", ExampleScriptGenerator.script(ExampleScriptGenerator.extract(FIXTURE, "fixture.md")));
        assertTrue(text.contains("return event.hand == \"MAIN_HAND\" && !isNull(event.item) && <minecraft:stick>.matches(event.item);"));
        assertTrue(text.contains("return event.world.remote ? \"client\" : \"server\";"));
        assertEquals(2, count(text, "    if (zwExampleTriggered(event)) {"));
    }

    @Test
    void duplicateIdsAreRefused() {
        List<String> doubled = Arrays.asList(
                "```zenscript example=same", "print(1);", "```",
                "```zenscript example=same", "print(2);", "```");
        assertThrows(IllegalArgumentException.class,
                () -> ExampleScriptGenerator.script(ExampleScriptGenerator.extract(doubled, "doubled.md")));
    }

    @Test
    void unclosedExampleIsRefused() {
        List<String> open = Arrays.asList("```zenscript example=open", "print(1);");
        assertThrows(IllegalArgumentException.class, () -> ExampleScriptGenerator.extract(open, "open.md"));
    }

    @Test
    void malformedExampleFenceIsRefused() {
        assertThrows(IllegalArgumentException.class,
                () -> ExampleScriptGenerator.extract(Arrays.asList("```zenscript example=Foo_Bar", "print(1);", "```"), "bad.md"));
        assertThrows(IllegalArgumentException.class,
                () -> ExampleScriptGenerator.extract(Arrays.asList("  ```zenscript example=indented", "print(1);", "```"), "bad.md"));
    }

    private static int count(String text, String part) {
        int n = 0;
        for (int i = text.indexOf(part); i >= 0; i = text.indexOf(part, i + 1)) {
            n++;
        }
        return n;
    }
}
