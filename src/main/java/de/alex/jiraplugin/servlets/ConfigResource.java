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

package de.alex.jiraplugin.servlets;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.project.Project;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.sal.api.user.UserProfile;
import de.alex.jiraplugin.utils.RestError;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * The resource for using the plugin configuration.
 */
@Path("/config")
@Scanned
public class ConfigResource {

    @ComponentImport
    private final UserManager userManager;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @ComponentImport
    private final TransactionTemplate transactionTemplate;

    @Inject
    public ConfigResource(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
                          TransactionTemplate transactionTemplate) {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * Get the plugin configuration.
     *
     * @param request
     *         The HTTP request from the page.
     * @return The plugin configuration.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context final HttpServletRequest request) {
        final UserProfile user = userManager.getRemoteUser(request);
        if (user == null || user.getUsername() == null || !userManager.isSystemAdmin(user.getUserKey())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        final List<IssueType> issueTypes = new ArrayList<>(ComponentAccessor.getConstantsManager().getAllIssueTypeObjects());

        return Response.ok(transactionTemplate.execute(() -> {
            final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
            final Config config = new Config();

            final String url = (String) settings.get(Config.URL_PROPERTY);
            config.setUrl(url);

            final String email = (String) settings.get(Config.EMAIL_PROPERTY);
            config.setEmail(email);

            final String password = (String) settings.get(Config.PASSWORD_PROPERTY);
            config.setPassword(password);

            final String issueTypeId = (String) settings.get(Config.ISSUE_TYPE_PROPERTY);
            if (issueTypeId != null) {
                config.setIssueType(issueTypeId);
            } else if (issueTypes.size() > 0) {

                // check if there is a type called "Test" from the Zephyr Plugin and use that per default.
                boolean testTypeExists = false;
                for (IssueType issueType : issueTypes) {
                    if (issueType.getName().equals("Test")) {
                        testTypeExists = true;
                        config.setIssueType(issueType.getId());
                    }
                }

                if (!testTypeExists) {
                    config.setIssueType(issueTypes.get(0).getId());
                }
            }

            final String projectKey = (String) settings.get(Config.PROJECT_KEY_PROPERTY);
            config.setProjectKey(projectKey);

            return config;
        })).build();
    }

    /**
     * Update the plugin configuration.
     *
     * @param config
     *         The updated configuration object.
     * @param request
     *         The HTTP request.
     * @return 204 on success.
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(final Config config, @Context final HttpServletRequest request) {
        final UserProfile user = userManager.getRemoteUser(request);
        if (user == null || user.getUsername() == null || !userManager.isSystemAdmin(user.getUserKey())) {
            final RestError error = new RestError(Response.Status.UNAUTHORIZED, "Unauthorized.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(error).build();
        }

        if (config.getUrl() == null || config.getUrl().trim().equals("")) {
            final RestError error = new RestError(Response.Status.BAD_REQUEST, "The URL may not be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (config.getEmail() == null || config.getEmail().trim().equals("")) {
            final RestError error = new RestError(Response.Status.BAD_REQUEST, "The email may not be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (config.getPassword() == null || config.getPassword().trim().equals("")) {
            final RestError error = new RestError(Response.Status.BAD_REQUEST, "The password may not be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (config.getIssueType() == null) {
            final RestError error = new RestError(Response.Status.BAD_REQUEST, "The issue type may not be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        if (config.getProjectKey() == null || config.getProjectKey().equals("")) {
            final RestError error = new RestError(Response.Status.BAD_REQUEST, "The project key may not be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        } else {
            final Project project = ComponentAccessor.getProjectManager().getProjectByCurrentKey(config.getProjectKey());
            if (project == null) {
                final RestError error = new RestError(Response.Status.BAD_REQUEST, "A project with the key '" + config.getProjectKey() + "' could not be found.");
                return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
            }
        }

        final List<IssueType> issueTypes = new ArrayList<>(ComponentAccessor.getConstantsManager().getAllIssueTypeObjects());
        final boolean issueTypeExists = issueTypes.stream().anyMatch(t -> t.getId().equals(config.getIssueType()));

        if (!issueTypeExists) {
            final RestError error = new RestError(Response.Status.BAD_REQUEST, "The issue type does not exist.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        transactionTemplate.execute(() -> {
            String url = config.getUrl();
            // all requests to the REST API are made relative to the provided URL, so we remove a trailing slash here.
            url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;

            PluginSettings pluginSettings = pluginSettingsFactory.createGlobalSettings();
            pluginSettings.put(Config.URL_PROPERTY, url);
            pluginSettings.put(Config.EMAIL_PROPERTY, config.getEmail());
            pluginSettings.put(Config.PASSWORD_PROPERTY, config.getPassword());
            pluginSettings.put(Config.ISSUE_TYPE_PROPERTY, config.getIssueType());
            pluginSettings.put(Config.PROJECT_KEY_PROPERTY, config.getProjectKey());

            return null;
        });

        return Response.noContent().build();
    }
}
