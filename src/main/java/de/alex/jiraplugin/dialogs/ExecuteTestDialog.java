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

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import de.alex.jiraplugin.entities.Project;
import de.alex.jiraplugin.servlets.Config;
import de.alex.jiraplugin.utils.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@Scanned
public class ExecuteTestDialog extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(ExecuteTestDialog.class);

    /** The HTTP client. */
    private final Client client;

    /** The error message to display. */
    private String errorMessage = "";

    /** The project in ALEX that is used for displaying the target URLs. */
    private Project alexProject;

    /** The id of the current jira project. */
    private String jiraProjectId;

    /** The id of the current test. */
    private String testId;

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @ComponentImport
    private final IssueManager issueManager;

    @Inject
    public ExecuteTestDialog(PluginSettingsFactory pluginSettingsFactory, IssueManager issueManager) {
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.issueManager = issueManager;
        this.client = ClientUtils.createDefaultClient();
    }

    public String doExecute() {
        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = (String) settings.get(Config.class.getName() + ".url");

        if (url == null) {
            errorMessage = "The plugin has not been configured yet.";
            return "error";
        }

        testId = getHttpRequest().getParameter("testId");
        jiraProjectId = getHttpRequest().getParameter("projectId");

        final Issue issue = issueManager.getIssueObject(Long.parseLong(testId));
        if (issue.getAssigneeId() == null) {
            errorMessage = "The test is not assigned to a person yet.";
            return "error";
        }

        try {
            // get ALEX project that is mapped to the current project
            final ClientResponse response = ClientUtils.createDefaultResource(client, url + "/rest/alex/projects/byJiraProject/" + jiraProjectId)
                    .get(ClientResponse.class);

            alexProject = response.getEntity(Project.class);

            return "dialog";
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            return "error";
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Project getAlexProject() {
        return alexProject;
    }

    public String getJiraProjectId() {
        return jiraProjectId;
    }

    public String getTestId() {
        return testId;
    }

}
