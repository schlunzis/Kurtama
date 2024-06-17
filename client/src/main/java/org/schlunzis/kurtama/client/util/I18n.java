package org.schlunzis.kurtama.client.util;

import jakarta.annotation.PostConstruct;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.schlunzis.kurtama.client.settings.Setting;
import org.schlunzis.kurtama.client.settings.UserSettings;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Utility-Class to support internationalization.
 *
 * @author Jonas Pohl
 * @since 0.0.1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class I18n {

    private final UserSettings userSettings;
    private final MessageSource messageSource;

    /**
     * The resource bundle on which elements can bind in order to be updated on
     * language change.
     */
    private final ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

    /*
     * The supported locales.
     */
    @Getter
    private List<Locale> SUPPORTED_LOCALES = Collections.emptyList();

    /**
     * The current locale.
     */
    @Getter
    private Locale locale = Locale.GERMANY;

    @PostConstruct
    private void init() {
        log.info("Initializing I18n");

        // Extract all language files from the resources
        List<Locale> supportedLocales = new ArrayList<>();
        try {
            ClassLoader cl = this.getClass().getClassLoader();
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
            // note the underscore (_) in the pattern. This way we skip the default messages.properties file which is
            // not language specific and causes trouble when trying to extract the locale from the filename
            Resource[] resources = resolver.getResources("classpath*:/lang/messages_*.properties");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null)
                    continue;
                Locale locale = extractLocaleFromFilename(filename);
                supportedLocales.add(locale);
            }
        } catch (Exception e) {
            log.error("Failed to load language files");
        }
        SUPPORTED_LOCALES = Collections.unmodifiableList(supportedLocales);
        if (SUPPORTED_LOCALES.isEmpty()) {
            log.error("Critical error: No language files found. Exiting");
            System.exit(1);
        }

        setLocale(Locale.forLanguageTag(userSettings.getString(Setting.LANGUAGE)));
    }

    /**
     * Extracts the locale from the filename of a language file.
     * <p>
     * The filename is expected to be in the format "messages_{languageTag}.properties".
     * Example: "messages_de_DE.properties" -> {@link Locale.GERMANY}
     *
     * @param filename the filename of the language file
     * @return the locale extracted from the filename
     */
    private Locale extractLocaleFromFilename(String filename) {
        // Extract the part of the filename between "messages_" and ".properties"
        String languageTag = filename.substring("messages_".length(), filename.indexOf(".properties"));
        // Replace any underscores with hyphens to match the format expected by Locale.forLanguageTag
        languageTag = languageTag.replace('_', '-');
        // Convert the language tag to a Locale
        return Locale.forLanguageTag(languageTag);
    }

    /**
     * Getter for the current ResourceBundle.
     *
     * @return the current ResourceBundle
     */
    public ResourceBundle getResourceBundle() {
        return bundle.get();
    }

    /**
     * Returns the String mapped to the provided key in the current locale.
     *
     * @param key  the key to be mapped
     * @param args the arguments to be inserted into the String
     * @return the localized String for the key
     */
    public String i18n(String key, final Object... args) {
        return messageSource.getMessage(key, args, locale);
    }

    /**
     * Helper to create a new String Binding for the provided key.
     *
     * @param key  the key to be mapped on the resources
     * @param args the arguments to be inserted into the String
     * @return a binding for the provided key
     */
    public StringBinding createBinding(String key, final Object... args) {
        return Bindings.createStringBinding(() -> i18n(key, args), bundle);
    }

    /**
     * Method to create a tooltip for the given key and object parameters
     *
     * @param key  the key for the language file
     * @param args the arguments for the language file
     * @return a i18n tooltip
     */
    public Tooltip createTooltip(String key, final Object... args) {
        Tooltip tt = new Tooltip();
        tt.textProperty().bind(createBinding(key, args));
        return tt;
    }

    /**
     * Sets the bundle to the given locale.
     *
     * @param locale the new locale for the resourceBundle
     */
    public void setLocale(Locale locale) {
        if (!SUPPORTED_LOCALES.contains(locale)) {
            log.error("Unsupported locale: {}", locale.toLanguageTag());
            Locale fallback = SUPPORTED_LOCALES.getFirst();
            log.error("Falling back to default: {}", fallback.toLanguageTag());
            setLocale(fallback);
        }
        this.locale = locale;
        userSettings.putString(Setting.LANGUAGE, locale.toLanguageTag());
        bundle.set(new MessageSourceResourceBundle(messageSource, locale));
        // NOTE: the following call to `bundle.get()' is necessary to trigger the update of the bindings
        // see https://github.com/schlunzis/Kurtama/pull/204#issuecomment-2173895002 for more information
        // TODO: find alternative for this whole dilemma
        log.debug("Updated bundle to {}", bundle.get());
    }
}