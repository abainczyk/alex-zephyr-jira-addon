package it.de.alex.jiraplugin.plugin;

import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.atlassian.sal.api.ApplicationProperties;
import de.alex.jiraplugin.plugin.api.AlexForJiraComponent;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class AlexForJiraWiredTest {
    private final ApplicationProperties applicationProperties;
    private final AlexForJiraComponent myPluginComponent;

    public AlexForJiraWiredTest(ApplicationProperties applicationProperties, AlexForJiraComponent myPluginComponent) {
        this.applicationProperties = applicationProperties;
        this.myPluginComponent = myPluginComponent;
    }

    @Test
    public void testMyName() {
        assertEquals("names do not match!", "alexForJiraComponent:" + applicationProperties.getDisplayName(), myPluginComponent.getName());
    }
}
