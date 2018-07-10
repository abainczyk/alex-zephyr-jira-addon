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

package de.alex.alexforjira.events;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.jersey.api.client.ClientResponse;
import de.alex.alexforjira.servlets.Config;
import de.alex.alexforjira.utils.Endpoints;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Event listener that sends issue (test) related events to the adapter.
 */
@Scanned
@Component
public class IssueEventListener implements InitializingBean, DisposableBean {

    @ComponentImport
    private final EventPublisher eventPublisher;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    private final Endpoints endpoints;

    @Inject
    public IssueEventListener(EventPublisher eventPublisher,
                              PluginSettingsFactory pluginSettingsFactory,
                              Endpoints endpoints) {
        this.eventPublisher = eventPublisher;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.endpoints = endpoints;
    }

    @Override
    public void afterPropertiesSet() {
        eventPublisher.register(this);
    }

    @Override
    public void destroy() {
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onIssueEvent(final IssueEvent issueEvent) {
        final Issue issue = issueEvent.getIssue();

        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String issueTypeId = (String) settings.get(Config.ISSUE_TYPE_PROPERTY);
        final String projectKey = (String) settings.get(Config.PROJECT_KEY_PROPERTY);

        if (issueTypeId == null
                || issue.getIssueTypeId() == null
                || !issue.getIssueTypeId().equals(issueTypeId)
                || !issue.getProjectObject().getKey().equals(projectKey)) {
            return;
        }

        final String eventType;
        final Long eventTypeId = issueEvent.getEventTypeId();
        if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
            eventType = "ISSUE_CREATED";
        } else if (eventTypeId.equals(EventType.ISSUE_UPDATED_ID)) {
            eventType = "ISSUE_UPDATED";
        } else if (eventTypeId.equals(EventType.ISSUE_DELETED_ID)) {
            eventType = "ISSUE_DELETED";
        } else {
            return;
        }

        final String data = "{"
                + "\"type\": \"" + eventType + "\""
                + ",\"issueId\": " + issue.getId()
                + ",\"issueSummary\": \"" + issue.getSummary() + "\""
                + ",\"projectId\": " + issue.getProjectId()
                + ",\"timestamp\": \"" + Timestamp.from(Instant.now()).toString() + "\""
                + "}";

        new Thread(() -> endpoints.issueEvents()
                .entity(data)
                .post(ClientResponse.class)
        ).start();
    }

}
