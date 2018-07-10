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

package de.alex.jiraplugin.utils;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;
import de.alex.jiraplugin.servlets.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

/** Endpoints to the adapter. */
@Scanned
@Component
public class Endpoints {

    private static final Logger log = LoggerFactory.getLogger(Endpoints.class);

    /** The connect timeout interval in ms. */
    private static final Integer CONNECT_TIMEOUT = 3000;

    /** The read timeout interval in ms. */
    private static final Integer READ_TIMEOUT = 3000;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    /** The HTTP client. */
    private final Client client;

    @Inject
    public Endpoints(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.client = new Client();
        this.client.setConnectTimeout(CONNECT_TIMEOUT);
        this.client.setReadTimeout(READ_TIMEOUT);
    }

    /**
     * Build a request to execute a test.
     *
     * @param projectId
     *         The ID of the project.
     * @param testId
     *         The ID of the test.
     * @return The builder for the request.
     */
    public WebResource.Builder executeTest(Long projectId, Long testId) {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = getUrl(settings) + "/rest/jira/projects/" + projectId + "/tests/" + testId + "/execute";
        log.info("send request to {}", url);
        return client.resource(url)
                .header(HttpHeaders.AUTHORIZATION, getAuth(settings))
                .header("Content-Type", "application/json;charset=UTF-8")
                .accept("application/json", "text/plain", "*/*");
    }

    /**
     * Build a request to get a project in ALEX.
     *
     * @param projectId
     *         The ID of the project.
     * @return The builder for the request.
     */
    public WebResource.Builder alexProject(Long projectId) {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = getUrl(settings) + "/rest/alex/projects/byJiraProject/" + projectId;
        log.info("send request to {}", url);
        return client.resource(url)
                .header(HttpHeaders.AUTHORIZATION, getAuth(settings))
                .header("Content-Type", "application/json;charset=UTF-8")
                .accept("application/json", "text/plain", "*/*");
    }

    /**
     * Build a request to send an issue event to the adapter.
     *
     * @return The builder for the request.
     */
    public WebResource.Builder issueEvents() {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = getUrl(settings) + "/rest/wh/jira/issues";
        log.info("send request to {}", url);
        return client.resource(url)
                .header(HttpHeaders.AUTHORIZATION, getAuth(settings))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .accept(MediaType.MEDIA_TYPE_WILDCARD);
    }

    /**
     * Build a request to send an project event to the adapter.
     *
     * @return The builder for the request.
     */
    public WebResource.Builder projectEvents() {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = getUrl(settings) + "/rest/wh/jira/projects";
        log.info("send request to {}", url);
        return client.resource(url)
                .header(HttpHeaders.AUTHORIZATION, getAuth(settings))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .accept(MediaType.MEDIA_TYPE_WILDCARD);
    }

    private String getUrl(PluginSettings settings) {
        return (String) settings.get(Config.URL_PROPERTY);
    }

    private String getAuth(PluginSettings settings) {
        final String email = (String) settings.get(Config.EMAIL_PROPERTY);
        final String password = (String) settings.get(Config.PASSWORD_PROPERTY);
        return "Basic " + new String(Base64.encode(email + ":" + password));
    }
}
