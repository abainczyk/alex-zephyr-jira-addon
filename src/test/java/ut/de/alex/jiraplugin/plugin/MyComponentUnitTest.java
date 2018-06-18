package ut.de.alex.jiraplugin.plugin;

import org.junit.Test;
import de.alex.jiraplugin.plugin.api.MyPluginComponent;
import de.alex.jiraplugin.plugin.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}