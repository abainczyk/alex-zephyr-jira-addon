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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The configuration entity.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Config {

    public static final String URL_PROPERTY = "de.alex.alexforjira.config.url";

    public static final String EMAIL_PROPERTY = "de.alex.alexforjira.config.email";

    public static final String PASSWORD_PROPERTY = "de.alex.alexforjira.config.password";

    public static final String ISSUE_TYPE_PROPERTY = "de.alex.alexforjira.config.issueType";

    public static final String PROJECT_KEY_PROPERTY = "de.alex.alexforjira.config.projectKey";

    /** The URL where the adapter can be accessed. */
    @XmlElement
    private String url;

    /** The email that is used to authorize with the adapter. */
    @XmlElement
    private String email;

    /** The password that is used to authorize with the adapter. */
    @XmlElement
    private String password;

    /** The type of the issue where the ALEX button is displayed. */
    @XmlElement
    private String issueType;

    /** The key of the project where the ALEX button is displayed. */
    @XmlElement
    private String projectKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }
}
