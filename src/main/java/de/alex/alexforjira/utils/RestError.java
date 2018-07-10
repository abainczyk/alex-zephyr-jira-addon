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

package de.alex.alexforjira.utils;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

/** Error data that can be send to the frontend. */
@XmlRootElement
public class RestError {

    /** The HTTP status. */
    private int status;

    /** The reason of the error. */
    private String message;

    public RestError() {
    }

    public RestError(Response.Status status, String message) {
        this.status = status.getStatusCode();
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
