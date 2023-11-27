package com.example.signlingual;

import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocaleConfig {

    Map<String, String> languages = new HashMap<>();
    private Locale currentLang;
    // Possible future Sign Language configurations
    private String currentSign;

    Resources resources;
    public LocaleConfig(Resources resources) {
        this.resources = resources;
        getLangPreferenceDropdownEntries();
        //TODO: Pull current languages so app starts with selected language
        // for older versions of android
    }

    // There might be a way to do this without passing Resources
    private LocaleListCompat getLocaleLanguages() {
        List<String> tagsList = new ArrayList<>();
        try {
            XmlPullParser xpp = resources.getXml(R.xml.locales_config);
            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    String s = xpp.getName();
                    if(s.equals("locale")) {
                        String strName = xpp.getAttributeValue(0);
                        tagsList.add(strName);
                    }
                }
            }
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
        return LocaleListCompat.forLanguageTags(String.join(",", tagsList));
    }

    public Map<String, String> getLangPreferenceDropdownEntries() {
        LocaleListCompat localeList = getLocaleLanguages();

        for (int i = 0; i < localeList.size(); i++) {
            Locale lang = localeList.get(i);
            languages.put(lang.toLanguageTag(), lang.getDisplayName());
        }
        return languages;
    }

    public String[] getLangDropdownValues() {
        String[] result = new String[languages.size()];
        languages.keySet().toArray(result);
        return result;
    }
    public String[] getLangDropdownEntries() {
        String[] result = new String[languages.size()];
        languages.values().toArray(result);
        return result;
    }

    public void setLocaleLang(String langTag) {
        //TODO: May need app to restart
        // See https://developer.android.com/guide/topics/resources/app-languages#groovy
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(langTag);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }
}
