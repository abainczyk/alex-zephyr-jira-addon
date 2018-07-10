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

package de.alex.alexforjira.dialogs;

import javax.xml.bind.annotation.XmlRootElement;

/** The configuration class for executing the test in ALEX. */
@XmlRootElement
public class ExecuteConfig {

    /** The id of the project in Jira that contains the test. */
    private Long jiraProjectId;

    /** The id of the test to execute. */
    private Long jiraTestId;

    /** The id of the URL entity in ALEX where the test is being executed on. */
    private Long alexUrlId;

    public Long getJiraProjectId() {
        return jiraProjectId;
    }

    public void setJiraProjectId(Long jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }

    public Long getJiraTestId() {
        return jiraTestId;
    }

    public void setJiraTestId(Long jiraTestId) {
        this.jiraTestId = jiraTestId;
    }

    public Long getAlexUrlId() {
        return alexUrlId;
    }

    public void setAlexUrlId(Long alexUrlId) {
        this.alexUrlId = alexUrlId;
    }
}
