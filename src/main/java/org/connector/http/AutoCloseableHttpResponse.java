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

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.connector.exceptions.CouchDBException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;


public class AutoCloseableHttpResponse implements AutoCloseable {

    private HttpResponse response;

    /**
     * @param response which should be wrapped and automatically closed. Must not be {@literal null}
     */
    public void set(@NonNull HttpResponse response) {
        Assert.notNull(response, "Response must not be null");
        this.response = response;
    }

    public @Nullable HttpResponse get() {
        return response;
    }

    @Override
    public void close() {
        if (response != null && response.getEntity() != null) {
            try {
                response.getEntity().getContent().close();
            } catch (IOException e) {
                throw new CouchDBException("Unable to close input stream");
            }
        }
    }

}
