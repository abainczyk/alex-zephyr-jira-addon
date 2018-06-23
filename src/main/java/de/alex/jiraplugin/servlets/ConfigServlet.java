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
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The servlet that displays the configuration page.
 */
@Scanned
public class ConfigServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ConfigServlet.class);

    @ComponentImport
    private final TemplateRenderer renderer;

    @Inject
    public ConfigServlet(final TemplateRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=utf-8");

        // get all available issue types in Jira that will be displayed in a select input.
        final List<IssueType> issueTypes = new ArrayList<>(ComponentAccessor.getConstantsManager().getAllIssueTypeObjects());

        // set the template variable.
        final Map<String, Object> map = new HashMap<>();
        map.put("issueTypes", issueTypes);

        renderer.render("templates/config/config.vm", map, resp.getWriter());
    }
}
