package com.github.kingwaggs.productmanager.coupang.sdk.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class ResourceBundleFactory {
    private static Locale defaultLocale = new Locale("ko", "KR");

    public ResourceBundleFactory() {
    }

    public static ResourceBundle createResourceBundle(String basename, String language) {
        Locale locale = getLocale(language);
        ResourceBundleFactory.UTF8Control utf8Control = new ResourceBundleFactory.UTF8Control();
        return ResourceBundle.getBundle(basename, locale, utf8Control);
    }

    private static Locale getLocale(String language) {
        Locale locale = defaultLocale;
        if (language.equalsIgnoreCase("English")) {
            locale = new Locale("en", "US");
        }

        return locale;
    }

    private static class UTF8Control extends ResourceBundle.Control {
        private UTF8Control() {
        }

        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException {
            String bundleName = this.toBundleName(baseName, locale);
            String resourceName = this.toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }

            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }

            return bundle;
        }
    }
}
