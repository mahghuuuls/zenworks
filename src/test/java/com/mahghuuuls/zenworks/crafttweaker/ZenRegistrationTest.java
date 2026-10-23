package com.mahghuuuls.zenworks.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import org.junit.jupiter.api.Test;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenExpansion;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CraftTweaker only registers classes that carry {@code @ZenRegister}; without it a class's
 * methods silently do not exist for scripts. This checks every ZenScript class in this package.
 */
class ZenRegistrationTest {

    @Test
    void everyZenScriptClassIsRegistered() throws Exception {
        List<Class<?>> zenClasses = new ArrayList<>();
        for (Class<?> cls : classesInThisPackage()) {
            if (cls.isAnnotationPresent(ZenExpansion.class) || cls.isAnnotationPresent(ZenClass.class)) {
                zenClasses.add(cls);
            }
        }
        assertFalse(zenClasses.isEmpty(), "no ZenScript classes found; the scan is broken");
        for (Class<?> cls : zenClasses) {
            assertTrue(cls.isAnnotationPresent(ZenRegister.class), cls.getName() + " lacks @ZenRegister");
        }
    }

    private static List<Class<?>> classesInThisPackage() throws URISyntaxException, ClassNotFoundException {
        String pkg = ZenRegistrationTest.class.getPackage().getName();
        URL url = PotionExpansion.class.getResource(PotionExpansion.class.getSimpleName() + ".class");
        assertNotNull(url);
        File dir = new File(url.toURI()).getParentFile();
        List<Class<?>> classes = new ArrayList<>();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".class") && !name.contains("$"));
        assertNotNull(files);
        for (File file : files) {
            String name = file.getName().substring(0, file.getName().length() - ".class".length());
            classes.add(Class.forName(pkg + "." + name, false, ZenRegistrationTest.class.getClassLoader()));
        }
        return classes;
    }
}
