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

package de.alex.alexforjira.plugin.impl;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;
import de.alex.alexforjira.plugin.api.AlexForJiraComponent;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({AlexForJiraComponent.class})
@Named("alexForJiraComponent")
public class AlexForJiraComponentImpl implements AlexForJiraComponent {

    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public AlexForJiraComponentImpl(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "alexForJiraComponent:" + applicationProperties.getDisplayName();
        }

        return "alexForJiraComponent";
    }
}
