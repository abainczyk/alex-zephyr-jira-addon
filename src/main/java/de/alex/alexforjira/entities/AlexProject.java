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
import java.util.ArrayList;
import java.util.List;

/** Entity for a project in ALEX. */
@XmlRootElement
public class AlexProject {

    /** The id of the project in the db. */
    private Long id;

    /** The name of the project. */
    private String name;

    /** The id of the user that belongs to the project. */
    private Long user;

    /** The list of URLs that are registered to the project. */
    private List<AlexProjectUrl> urls;

    public AlexProject() {
        this.urls = new ArrayList<>();
    }

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

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public List<AlexProjectUrl> getUrls() {
        return urls;
    }

    public void setUrls(List<AlexProjectUrl> urls) {
        this.urls = urls;
    }
}
