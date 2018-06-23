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

package de.alex.jiraplugin.dialogs;

import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import de.alex.jiraplugin.servlets.Config;
import de.alex.jiraplugin.utils.ClientUtils;
import de.alex.jiraplugin.utils.RestError;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** The class that contains REST endpoints for executing tests in ALEX. */
@Path("/execute")
@Scanned
public class ExecuteResource {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    /** The HTTP client. */
    private final Client client;

    @Inject
    public ExecuteResource(PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.client = ClientUtils.createDefaultClient();
    }

    /**
     * Execute a single test case.
     *
     * @param config
     *         The configuration that is used for the execution.
     * @return 200, 400 if the test could not be executed.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response execute(ExecuteConfig config) {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = (String) settings.get(Config.class.getName() + ".url");

        if (url == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new RestError(Response.Status.BAD_REQUEST, "The adapter has not been configured yet."))
                    .build();
        }

        try {
            final ClientResponse response = ClientUtils.createDefaultResource(client, url + "/rest/jira/projects/" + config.getJiraProjectId() + "/tests/" + config.getJiraTestId() + "/execute")
                    .entity(config)
                    .post(ClientResponse.class);

            if (response.getStatus() == 200) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(response.getEntity(String.class))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new RestError(Response.Status.BAD_REQUEST, e.getMessage()))
                    .build();
        }
    }
}
