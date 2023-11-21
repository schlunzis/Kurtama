package org.schlunzis.kurtama.client.util;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Tooltip;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Utility-Class to support internationalization.
 *
 * <br>
 * Some research pointed out the eclipse solution for i18n. For further
 * information have a look in the <a href=
 * "https://www.eclipse.org/eclipse/platform-core/documents/3.1/message_bundles.html">documentation</a>.
 * <br>
 * Nevertheless, it is not used, as it is a completely different approach then
 * previously used and does not bring that much of an advantage. <br>
 * Further best practices have been discussed in <a href=
 * "https://stackoverflow.com/questions/6992977/where-to-place-i18n-key-strings-in-java">this</a>
 * Stack Overflow article.
 *
 * @author Jonas Pohl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nUtils {

    /**
     * The resource bundle on which elements can bind in order to be updated on
     * language change.
     */
    private static final ObjectProperty<ResourceBundle> bundle = new SimpleObjectProperty<>();

    /**
     * The current locale. Will be initially set to {@link Locale#UK} in JavaFX init
     * Method.
     */
    @Getter
    private static Locale locale;

    static {
        // TODO: load locale at login from server
        setLocale(Locale.GERMANY);
    }

    /**
     * ObjectProperty to allow bindings
     *
     * @return The ObjectProperty
     */
    public static ObjectProperty<ResourceBundle> bundleProperty() {
        return bundle;
    }

    /**
     * Getter for the current locale.
     *
     * @return the current locale
     */
    public static ResourceBundle getBundle() {
        return bundle.get();
    }

    /**
     * Setter for the current resourceBundle.
     *
     * @param bundle the new bundle
     */
    public static void setBundle(ResourceBundle bundle) {
        bundleProperty().set(bundle);
    }

    /**
     * Returns the String mapped to the provided key in the current locale.
     *
     * @param key  the key to be mapped
     * @param args the arguments to be inserted into the String
     * @return the localized String for the key
     */
    public static String i18n(String key, final Object... args) {
        return MessageFormat.format(getBundle().getString(key), args);
    }

    /**
     * Helper to create a new String Binding for the provided key.
     *
     * @param key  the key to be mapped on the resources
     * @param args the arguments to be inserted into the String
     * @return a binding for the provided key
     */
    public static StringBinding createBinding(String key, final Object... args) {
        return Bindings.createStringBinding(() -> i18n(key, args), bundleProperty());
    }

    /**
     * Method to create a tooltip for the given key and object parameters
     *
     * @param key  the key for the language file
     * @param args the arguments for the language file
     * @return a i18n tooltip
     */
    public static Tooltip createTooltip(String key, final Object... args) {
        Tooltip tt = new Tooltip();
        tt.textProperty().bind(I18nUtils.createBinding(key, args));
        return tt;
    }

    /**
     * Sets the bundle to the given locale.
     *
     * @param locale the new locale for the resourceBundle
     */
    public static void setLocale(Locale locale) {
        I18nUtils.locale = locale;
        Locale.setDefault(locale);
        setBundle(ResourceBundle.getBundle("lang.messages", locale));
    }

    /**
     * Setter for the current locale by the String representation of the locale.
     *
     * @param locale the locale as String
     */
    public static void setLocale(String locale) {
        try {
            String[] parts = locale.split("_");
            setLocale(new Locale(parts[0], parts[1]));
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            setLocale(Locale.UK);
        }
    }
}
