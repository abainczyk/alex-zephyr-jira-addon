package ut.de.alex.jiraplugin.plugin;

import de.alex.alexforjira.plugin.api.AlexForJiraComponent;
import de.alex.alexforjira.plugin.impl.AlexForJiraComponentImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AlexForJiraUnitTest {
    @Test
    public void testMyName() {
        AlexForJiraComponent component = new AlexForJiraComponentImpl(null);
        assertEquals("names do not match!", "alexForJiraComponent", component.getName());
    }
}
