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
package net.ljcomputing.camelinsurancespike.camel.route;

import net.ljcomputing.camelinsurancespike.model.Sample;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/** Direct route definitions. */
@Component
public class DirectRoutes extends RouteBuilder {

    /** Configure route enpoints. */
    @Override
    public void configure() throws Exception {
        from("direct:jsonToSample")
                .routeId("DirectJsonToSample")
                .unmarshal()
                .json(Sample.class)
                .log("DirectJsonToSample - body: ${body}")
                .end();

        // from("direct:sample")
        //         .routeId("DirectSample")
        //         .tracing()
        //         .log("DirectSimple: ${body}")
        //         .transform()
        //         .simple("${body}")
        //         .to(ExchangePattern.InOnly, "jms:queue:sample?pubSubNoLocal=true");
    }
}
