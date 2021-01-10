package org.test4j.module.database.environment.normalise;

/**
 * Utility class for normalising identifiers.
 */
public class NameNormaliser {
    private NameNormaliser() {
        // utility classes should not be instanciated
    }

    private static String replaceIllegalCharactersWithSpacesRegex = "[^a-zA-Z0-9_.#]";

    private static String replaceIllegalCharacters(final String name) {
        return name.replaceAll(replaceIllegalCharactersWithSpacesRegex, "");
    }

}