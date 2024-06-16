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
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
     * TODO: determine the supported locales from the language files at runtime.
     */
    @Getter
    private final List<Locale> SUPPORTED_LOCALES = Arrays.asList(Locale.GERMANY, Locale.US);

    /**
     * The current locale.
     */
    @Getter
    private Locale locale = Locale.GERMANY;

    @PostConstruct
    private void init() {
        log.info("Initializing I18n");
        setLocale(Locale.forLanguageTag(userSettings.getString(Setting.LANGUAGE)));
    }

    /**
     * ObjectProperty to allow bindings
     *
     * @return The ObjectProperty
     */
    public ObjectProperty<ResourceBundle> bundleProperty() {
        return bundle;
    }

    /**
     * Getter for the current ResourceBundle.
     *
     * @return the current ResourceBundle
     */
    public ResourceBundle getBundle() {
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
        return Bindings.createStringBinding(() -> i18n(key, args), bundleProperty());
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
        this.locale = locale;
        userSettings.putString(Setting.LANGUAGE, locale.toLanguageTag());
        // why do I need the following line in order to make the binding work?
        log.info("old {}, new {}", bundle.get(), new MessageSourceResourceBundle(messageSource, locale));
        bundleProperty().set(new MessageSourceResourceBundle(messageSource, locale));
    }
}