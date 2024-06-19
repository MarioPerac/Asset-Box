package org.unibl.etf.mr.assetbox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String DEFAULT_LANGUAGE = "en";

    public static Context setLocale(@NonNull Context context) {
        return updateResources(context, getLanguage(context));
    }

    public static Context setLocale(@NonNull Context context, @NonNull String language) {
        persistLanguage(context, language);
        return updateResources(context, language);
    }

    private static Context updateResources(@NonNull Context context, @NonNull String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);

        context = context.createConfigurationContext(configuration);

        return context;
    }

    public static void setLanguage(@NonNull Context context, @NonNull String language) {
        persistLanguage(context, language);
    }

    private static void persistLanguage(@NonNull Context context, @NonNull String language) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        preferences.edit().putString(SELECTED_LANGUAGE, language).apply();
    }

    public static String getLanguage(@NonNull Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.getString(SELECTED_LANGUAGE, DEFAULT_LANGUAGE);
    }

    public static Context onAttach(@NonNull Context context) {
        String lang = getLanguage(context);
        return setLocale(context, lang);
    }

    public static Context onAttach(@NonNull Context context, @NonNull String defaultLanguage) {
        String lang = getLanguage(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static String getLanguage(@NonNull Context context, @NonNull String defaultLanguage) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }
}
