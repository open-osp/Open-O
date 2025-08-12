// src/main/java/oscar/login/AppOAuth1Config.java
package oscar.login;

import java.util.Map;
import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class AppOAuth1Config {
    private static final Logger logger = MiscUtils.getLogger();

    private String consumerKey;
    private String consumerSecret;
    private String baseUrl;
    private String callbackURI;
    private String applicationURI;

    public String getConsumerKey()       { return consumerKey; }
    public void   setConsumerKey(String k)   { this.consumerKey = k; }

    public String getConsumerSecret()    { return consumerSecret; }
    public void   setConsumerSecret(String s){ this.consumerSecret = s; }

    public String getBaseUrl()           { return baseUrl; }
    public void   setBaseUrl(String u)      { this.baseUrl = u; }

    public String getCallbackURI()       { return callbackURI; }
    public void   setCallbackURI(String c)    { this.callbackURI = c; }

    public String getApplicationURI()    { return applicationURI; }
    public void   setApplicationURI(String a) { this.applicationURI = a; }

    public String getBaseURL() { return baseUrl; }

    /**
     * Parse an OAuth1 config from a “document” that might be a Map, a
     * java.util.Properties, or some other POJO with getters.
     */
    public static AppOAuth1Config fromDocument(Object configDoc) {
        if (configDoc == null) {
            logger.warn("Configuration document is null");
            return null;
        }
        try {
            AppOAuth1Config cfg = new AppOAuth1Config();

            if (configDoc instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String,Object> m = (Map<String,Object>)configDoc;
                cfg.consumerKey     = toStr(m.get("oauth.consumer.key"));
                cfg.consumerSecret  = toStr(m.get("oauth.consumer.secret"));
                cfg.baseUrl         = toStr(m.get("oauth.base.url"));
                cfg.callbackURI     = toStr(m.get("oauth.callback.uri"));
                cfg.applicationURI  = toStr(m.get("oauth.application.uri"));

            } else if (configDoc instanceof Properties) {
                Properties p = (Properties)configDoc;
                cfg.consumerKey     = p.getProperty("oauth.consumer.key");
                cfg.consumerSecret  = p.getProperty("oauth.consumer.secret");
                cfg.baseUrl         = p.getProperty("oauth.base.url");
                cfg.callbackURI     = p.getProperty("oauth.callback.uri");
                cfg.applicationURI  = p.getProperty("oauth.application.uri");

            } else {
                // fallback: reflection on POJO getters
                cfg = parseViaReflection(configDoc);
            }

            // validate required fields
            if (cfg.consumerKey == null || cfg.consumerSecret == null) {
                logger.error("Missing required OAuth configuration: consumer key or secret");
                return null;
            }
            if (cfg.baseUrl == null) {
                logger.error("Missing required OAuth configuration: base URL");
                return null;
            }

            logger.debug("Successfully parsed OAuth configuration");
            return cfg;

        } catch (Exception e) {
            logger.error("Failed to parse OAuth configuration: {}", e.getMessage(), e);
            return null;
        }
    }

    private static String toStr(Object o) {
        return (o == null ? null : o.toString());
    }

    private static AppOAuth1Config parseViaReflection(Object doc) {
        AppOAuth1Config cfg = new AppOAuth1Config();
        Class<?> cls = doc.getClass();

        cfg.consumerKey    = invokeGetter(doc, cls, "getConsumerKey", "getOauthConsumerKey");
        cfg.consumerSecret = invokeGetter(doc, cls, "getConsumerSecret", "getOauthConsumerSecret");
        cfg.baseUrl        = invokeGetter(doc, cls, "getBaseUrl", "getOauthBaseUrl");
        cfg.callbackURI    = invokeGetter(doc, cls, "getCallbackURI", "getOauthCallbackUri");
        cfg.applicationURI = invokeGetter(doc, cls, "getApplicationURI", "getOauthApplicationUri");

        return cfg;
    }

    private static String invokeGetter(Object obj, Class<?> cls, String... names) {
        for (String name : names) {
            try {
                java.lang.reflect.Method m = cls.getMethod(name);
                Object v = m.invoke(obj);
                if (v != null) return v.toString();
            } catch (Exception ignored) { }
        }
        return null;
    }
}
