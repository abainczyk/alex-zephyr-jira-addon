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

package de.alex.jiraplugin.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/** Utilities for the HTTP client. */
public class ClientUtils {

    /** The connect timeout interval in ms. */
    private static final Integer CONNECT_TIMEOUT = 3000;

    /** The read timeout interval in ms. */
    private static final Integer READ_TIMEOUT = 3000;

    /**
     * Creates a new HTTP client.
     *
     * @return The client.
     */
    public static Client createDefaultClient() {
        final Client client = new Client();
        client.setConnectTimeout(CONNECT_TIMEOUT);
        client.setReadTimeout(READ_TIMEOUT);
        return client;
    }

    public static WebResource.Builder createDefaultResource(Client client, String url) {
        return client.resource(url)
                .header("Content-Type", "application/json;charset=UTF-8")
                .accept("application/json", "text/plain", "*/*");
    }
}
