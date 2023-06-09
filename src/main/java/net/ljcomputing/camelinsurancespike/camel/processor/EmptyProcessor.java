/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

James G Willmore - LJ Computing - (C) 2023
*/
package net.ljcomputing.camelinsurancespike.camel.processor;

import lombok.NoArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Empty processor. */
@Component
@NoArgsConstructor
public class EmptyProcessor implements Processor {
    private static final Logger log = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    /**
     * Print the body of the Exchange.
     *
     * @param exchange
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        log.debug("{} starting", EmptyProcessor.class.getName());
        log.debug("body: [{}]", exchange.getIn().getBody());
        log.debug("{} exiting", EmptyProcessor.class.getName());
    }
}
