package base;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Dimension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
public class TestProperties extends Properties {
    public String browser;
    public String webdriverHub;
    public String webdriverPort;
    public Integer threadCount;
    public Boolean runRemote;
    public List<String> includedGroups;
    public String environment;
    public String customBaseUrl;
    public List<String> excludedGroups;
    public Boolean imageCheckerEnabled;
    public String className;
    public String methodName;
    public Boolean webdriverProfilingEnabled;
    public Boolean exitCodesEnabled;
    public String reportsFolder;
    private static final String prefix = "testng.";
    private static final String defaultBrowser = "Chrome";
    private static final String defaultWebdriverHub = "";
    private static final String defaultThreadCount = "5";
    private static final String defaultWebdriverPort = "4444";
    private static final String defaultRunRemote = "false";
    private static final String defaultGroups = null;
    private static final String defaultExcludedGroups = "";
    private static final String defaultEnvironment = null;
    private static final String defaultImageCheckerEnabled = "false";
    private static final String defaultWebdriverProfilingEnabled = "true";
    private static final String defaultExitCodesEnabled = "false";
    private static final String defaultReportsFolder = "test-output";

    // CONSTANTS
    public static List<String> EXCLUDED_GROUPS = Arrays.asList("DEPRECATED", "KNOWNBUG",
            "QUARANTINE");
    public static org.openqa.selenium.Dimension BROWSER_SIZE = new Dimension(360, 800);

    public TestProperties(Properties defaults) {
        super(defaults);
        browser = this.getProperty(prefix + "browser", defaultBrowser);

        // TODO Make it a little bit more clean code way...
        if (browser.trim().isEmpty())
            browser = defaultBrowser;

        webdriverHub = this.getProperty(prefix + "webdriverHub", defaultWebdriverHub);

        // TODO Make it a little bit more clean code way...
        if (webdriverHub.trim().isEmpty())
            webdriverHub = defaultWebdriverHub;

        webdriverPort = this.getProperty(prefix + "webdriverPort", defaultWebdriverPort);

        // TODO Make it a little bit more clean code way...
        String sThreadCount = this.getProperty(prefix + "threadCount", "");
        if (sThreadCount.trim().isEmpty())
            threadCount = Integer.parseInt(defaultThreadCount);
        else
            threadCount = Integer.parseInt(sThreadCount);

        runRemote = Boolean.parseBoolean(this.getProperty(prefix + "runRemote", defaultRunRemote));

        String groupList = this.getProperty(prefix + "groups", defaultGroups);
        if (groupList == null || groupList.isEmpty()) {
            includedGroups = null;
        } else {
            includedGroups = Arrays.asList(groupList.split("\\s*,\\s*"));
        }

        environment = this.getProperty(prefix + "environment", defaultEnvironment);

        // TODO Make it a little bit more clean code way...
        if (environment == null || environment.trim() == null || environment.trim().isEmpty())
            environment = defaultEnvironment;

        customBaseUrl = this.getProperty(prefix + "customBaseUrl");

        excludedGroups = new ArrayList<String>();
        excludedGroups.addAll(EXCLUDED_GROUPS);
        List<String> excludedGroupsFromParams = Arrays.asList(this
                .getProperty(prefix + "excludedGroups", defaultExcludedGroups).split("\\s*,\\s*"));
        for (String group : excludedGroupsFromParams) {
            if (group != null && !group.trim().isEmpty()) {
                excludedGroups.add(group.trim());
            }
        }

        imageCheckerEnabled = Boolean.parseBoolean(
                this.getProperty(prefix + "imageCheckerEnabled", defaultImageCheckerEnabled));

        className = StringUtils.trimToNull(this.getProperty(prefix + "className"));

        methodName = StringUtils.trimToNull(this.getProperty(prefix + "methodName"));

        webdriverProfilingEnabled = Boolean.parseBoolean(this.getProperty(
                prefix + "webdriverProfilingEnabled", defaultWebdriverProfilingEnabled));

        exitCodesEnabled = Boolean.parseBoolean(
                this.getProperty(prefix + "exitCodesEnabled", defaultExitCodesEnabled));
        if ((this.environment != null && this.environment.equals("CI"))
                || (this.getProperty("APP_ENV") != null
                        && this.getProperty("APP_ENV").equals("build"))) {
            exitCodesEnabled = true;
            this.environment = "build";
        }

        reportsFolder = StringUtils
                .trimToNull(this.getProperty(prefix + "reportsFolder", defaultReportsFolder));
    }

    public boolean isTestEnvironment() {
        return !(environment == null || environment.isEmpty());
    }
}