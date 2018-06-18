package ut.de.alex.jiraplugin.plugin;

import de.alex.jiraplugin.plugin.api.AlexForJiraComponent;
import de.alex.jiraplugin.plugin.impl.AlexForJiraComponentImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlexForJiraUnitTest {
    @Test
    public void testMyName() {
        AlexForJiraComponent component = new AlexForJiraComponentImpl(null);
        assertEquals("names do not match!", "alexForJiraComponent", component.getName());
    }
}
