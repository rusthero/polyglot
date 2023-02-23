package dev.rusthero.polyglot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;

/**
 * Polyglot is a Java library that allows for easy localization and translation of Java applications.
 */
public class Polyglot {
    /**
     * The path where language files are stored.
     */
    protected static final String LANG_PATH = "/lang/";

    /**
     * A HashSet containing all loaded Language objects.
     */
    private final HashSet<Language> languages = new HashSet<>();

    /**
     * Constructs a Polyglot object using a resource class to search for language files.
     *
     * @param resourceClass The Class object to use for searching for language files.
     * @throws IOException Thrown when the lang directory cannot be found in the specified JAR file.
     */
    public Polyglot(Class<?> resourceClass) throws IOException {
        try (InputStream stream = resourceClass.getResourceAsStream(LANG_PATH)) {
            if (stream == null) throw new IOException("Missing lang directory in JAR!");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

            String fileName = reader.readLine();
            while (fileName != null) {
                fileName = fileName.toLowerCase();
                if (fileName.endsWith(".json")) languages.add(new Language(fileName, resourceClass));
                fileName = reader.readLine();
            }
        }
    }

    /**
     * Gets a Language object by its locale code.
     *
     * @param localeCode The locale code of the desired Language.
     * @return An Optional containing the Language if found, or an empty Optional if not.
     */
    public Optional<Language> getLanguage(String localeCode) {
        return languages.stream().filter(language -> localeCode.equalsIgnoreCase(language.localeCode)).findAny();
    }

    /**
     * Gets a HashSet of all loaded Language objects.
     *
     * @return A HashSet containing all loaded Language objects.
     */
    public HashSet<Language> getLanguages() {
        return languages;
    }
}
