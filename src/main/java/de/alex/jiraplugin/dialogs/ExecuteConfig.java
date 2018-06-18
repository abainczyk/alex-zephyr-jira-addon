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

import javax.xml.bind.annotation.XmlRootElement;

/** The configuration class for executing the test in ALEX. */
@XmlRootElement
public class ExecuteConfig {

    /** The id of the project in Jira that contains the test. */
    private String jiraProjectId;

    /** The id of the test to execute. */
    private String testId;

    /** The id of the URL entity in ALEX where the test is being executed on. */
    private Long urlId;

    public String getJiraProjectId() {
        return jiraProjectId;
    }

    public void setJiraProjectId(String jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

}
