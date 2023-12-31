/*
 * Copyright 2020-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.connector.http;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.RequestLine;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;
import org.connector.exceptions.CouchDBException;

import java.io.IOException;

public class ThrowingInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse response, HttpContext context) throws IOException {
        switch (response.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_CREATED:
            case HttpStatus.SC_ACCEPTED:
            case HttpStatus.SC_NOT_MODIFIED:
                break;
            //Report all unknown states
            default:
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                RequestLine requestLine = clientContext.getRequest().getRequestLine();
                String reason = response.getStatusLine().getReasonPhrase();
                if (response.getEntity() != null) {
                    reason = new String(response.getEntity().getContent().readAllBytes());
                }
                throw new CouchDBException(reason);
        }
    }
}
