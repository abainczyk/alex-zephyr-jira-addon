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

package de.alex.jiraplugin.conditions;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.project.Project;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.web.Condition;
import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import de.alex.jiraplugin.servlets.Config;

import javax.inject.Inject;
import java.util.Map;

/**
 * Display sections for ALEX only if the current issue type matches the one that has been configured.
 */
@Scanned
public class DisplayAlexDropdownMenuCondition implements Condition {

    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;

    @Inject
    public DisplayAlexDropdownMenuCondition(final PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }

    @Override
    public void init(final Map<String, String> map) throws PluginParseException {
    }

    @Override
    public boolean shouldDisplay(final Map<String, Object> context) {
        final JiraHelper helper = (JiraHelper) context.get("helper");
        final Issue issue = (Issue) helper.getContextParams().get("issue");
        final Project project = (Project) helper.getContextParams().get("project");

        // do not display the section if we are not on a zephyr test page
        if (issue == null || issue.getIssueType() == null) {
            return false;
        }

        final PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
        final String url = (String) settings.get(Config.URL_PROPERTY);
        final String issueType = (String) settings.get(Config.ISSUE_TYPE_PROPERTY);
        final String projectKey = (String) settings.get(Config.PROJECT_KEY_PROPERTY);

        // return true if the current issue type equals the configured one and if the right project is used
        return url != null
                && issueType.equals(issue.getIssueTypeId())
                && projectKey != null
                && projectKey.equals(project.getKey());
    }
}
