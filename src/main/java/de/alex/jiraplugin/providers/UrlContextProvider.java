/*
 * Copyright 2018 Alexander Bainczyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.alex.jiraplugin.providers;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.ContextProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import de.alex.jiraplugin.servlets.Config;

import javax.inject.Inject;
import java.util.Map;

/**
 * Provides the URL of the adapter that is defined in the configuration for the plugin. This way, it can be used in web
 * item links by using $url in velocity templates.
 */
@Scanned
public class UrlContextProvider implements ContextProvider {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public UrlContextProvider(final PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public void init(final Map<String, String> ctx) throws PluginParseException {
    }

    @Override
    public Map<String, Object> getContextMap(final Map<String, Object> ctx) {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = (String) settings.get(Config.URL_PROPERTY);
        ctx.put("url", url);
        return ctx;
    }
}
