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

package de.alex.alexforjira.entities;

import javax.xml.bind.annotation.XmlRootElement;

/** The entity for a URL in an ALEX project. */
@XmlRootElement
public class AlexProjectUrl {

    /** The id of the URL in the database of ALEX. */
    private Long id;

    /** The name of the URL. */
    private String name;

    /** The URL of the target system. */
    private String url;

    /** If the URL is the one that is selected by default for executing tests. */
    private boolean defaultUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDefault() {
        return defaultUrl;
    }

    public void setDefault(boolean defaultUrl) {
        this.defaultUrl = defaultUrl;
    }
}
