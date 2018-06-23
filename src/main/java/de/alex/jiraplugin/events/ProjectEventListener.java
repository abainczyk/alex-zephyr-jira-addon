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

package de.alex.jiraplugin.events;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.ProjectDeletedEvent;
import com.atlassian.jira.project.Project;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import de.alex.jiraplugin.servlets.Config;
import de.alex.jiraplugin.utils.ClientUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.time.Instant;

/** Event listener that sends project related events to the adapter. */
@Scanned
@Component
public class ProjectEventListener implements InitializingBean, DisposableBean {

    @ComponentImport
    private final EventPublisher eventPublisher;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    /** The HTTP client. */
    private final Client client;

    @Inject
    public ProjectEventListener(EventPublisher eventPublisher, PluginSettingsFactory pluginSettingsFactory) {
        this.eventPublisher = eventPublisher;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.client = ClientUtils.createDefaultClient();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() throws Exception {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onProjectDeletedEvent(final ProjectDeletedEvent projectEvent) {
        final Project project = projectEvent.getProject();
        final String data = "{"
                + "\"type\": \"PROJECT_DELETED\","
                + ",\"projectId\": " + project.getId()
                + ",\"projectName\": \"" + project.getName() + "\""
                + ",\"timestamp\": \"" + Timestamp.from(Instant.now()).toString() + "\""
                + "}";

        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = (String) settings.get(Config.class.getName() + ".url");

        new Thread(() -> client.resource(url + "/rest/wh/jira/projects")
                .entity(data)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .accept(MediaType.MEDIA_TYPE_WILDCARD)
                .post(ClientResponse.class)
        ).start();
    }

}
