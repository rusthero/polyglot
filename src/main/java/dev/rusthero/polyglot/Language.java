package dev.rusthero.polyglot;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a language/translation read from resources. Contains an HashMap read from the corresponding translation
 * file to get strings using field name.
 */
public class Language {
    /**
     * The locale code associated with this language.
     */
    public final String localeCode;

    /**
     * Basically the JSON in a parsed form to retrieve strings using field names.
     */
    private final HashMap<String, String> strings = new HashMap<>();

    /**
     * Constructs a Language using locale code. The language is going to be read from /lang/{localeCode} in JAR.
     *
     * @param localeCode    Name of language file with `.json` extension.
     * @param resourceClass The resource class to use for loading the language file.
     * @throws IOException If the language file for the given locale code does not exist or is unable to be read.
     */
    protected Language(String localeCode, Class<?> resourceClass) throws IOException {
        this.localeCode = localeCode;

        try (InputStream stream = resourceClass.getResourceAsStream(Polyglot.LANG_PATH + localeCode)) {
            if (stream == null) throw new IOException("Unsupported language!");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

            final TypeToken<Map<String, String>> typeToken = new TypeToken<>() {};
            final Map<String, String> jsonMap = new Gson().fromJson(reader, typeToken.getType());
            reader.close();

            if (jsonMap == null) throw new NullPointerException("Empty language file for " + localeCode);
            strings.putAll(jsonMap);
        } catch (JsonSyntaxException e) {
            throw new JsonSyntaxException("Invalid JSON syntax for " + localeCode, e);
        }
    }

    /**
     * Returns the string mapped to the given field.
     *
     * @param field the field name mapped to the string
     * @return the string mapped to the given field
     */
    public String getString(String field) {
        return strings.get(field);
    }
}