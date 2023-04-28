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
package net.ljcomputing.camelinsurancespike;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import net.ljcomputing.camelinsurancespike.constants.InsuredSql;
import net.ljcomputing.camelinsurancespike.model.Insured;
import net.ljcomputing.camelinsurancespike.model.Sample;
import net.ljcomputing.camelinsurancespike.utils.JsonUtils;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@CamelSpringBootTest
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
class CamelInsuranceSpikeApplicationTests {
    private static final Logger log =
            LoggerFactory.getLogger(CamelInsuranceSpikeApplicationTests.class);

    @Autowired private CamelContext camelContext;

    @Autowired private ProducerTemplate producertemplate;

    @EndpointInject("mock:result")
    private MockEndpoint mockEndpoint;

    @Autowired private JdbcTemplate jdbcTemplate;

    private static final Sample getSample() {
        Sample sample = new Sample();
        sample.setKey1("KEY_ONE");
        sample.setKey2("KEY_TWO");
        log.debug("Sample used in test: {}", sample);
        return sample;
    }

    private static final String getExpected(final String filename) throws IOException {
        Path templateXml =
                Path.of(
                        System.getProperty("user.dir"),
                        "src",
                        "test",
                        "resources",
                        "test",
                        filename + ".xml");
        String result = Files.readString(templateXml);
        log.debug("expected: [{}]", result);
        return result;
    }

    private Integer getInsuredRecordCnt() {
        Integer cnt =
                (Integer) jdbcTemplate.queryForObject(InsuredSql.SELECT_COUNT.sql(), Integer.class);
        log.debug("insured cnt: [{}]", cnt);
        return cnt;
    }

    @Test
    @Order(1)
    void contextLoads() {
        assertNotNull(camelContext);
        assertNotNull(producertemplate);
        assertNotNull(mockEndpoint);
        assertNotNull(jdbcTemplate);
    }

    @Test
    @Order(10)
    void testDirectSampleTemplate() throws Exception {
        String routeId = "DirectSampleTemplate";
        String routeUri = "direct:sampletemplate";

        AdviceWith.adviceWith(
                camelContext,
                routeId,
                r -> {
                    r.replaceFromWith(routeUri);
                    r.weaveAddLast().to(mockEndpoint);
                    r.weaveAddLast().log("endpoint: ${body}");
                });

        mockEndpoint.expectedBodiesReceived(getExpected(routeId));
        mockEndpoint.setResultWaitTime(600);
        mockEndpoint.setAssertPeriod(600);

        producertemplate.sendBody(routeUri, getSample());

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    @Order(11)
    // works
    void testDirectJsonToSample() throws Exception {
        String routeId = "DirectJsonToSample";
        String routeUri = "direct:jsonToSample";

        AdviceWith.adviceWith(
                camelContext,
                routeId,
                r -> {
                    r.replaceFromWith(routeUri);
                    r.weaveAddLast().to(mockEndpoint);
                    r.weaveAddLast().log("endpoint: ${body}");
                });

        mockEndpoint.expectedBodiesReceived(getSample());
        mockEndpoint.setResultWaitTime(600);
        mockEndpoint.setAssertPeriod(600);

        producertemplate.sendBody(routeUri, JsonUtils.pojoToJson(getSample()));

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    @Order(12)
    void testDirectXjToXml() throws Exception {
        String routeId = "DirectXjToXml";
        String routeUri = "direct:xjtoxml";

        AdviceWith.adviceWith(
                camelContext,
                routeId,
                r -> {
                    r.replaceFromWith(routeUri);
                    r.weaveAddLast().to(mockEndpoint);
                    r.weaveAddLast().log("endpoint: ${body}");
                });

        mockEndpoint.expectedBodiesReceived(getExpected(routeId));
        mockEndpoint.setResultWaitTime(600);
        mockEndpoint.setAssertPeriod(600);

        producertemplate.sendBody(routeUri, JsonUtils.pojoToJson(getSample()));

        mockEndpoint.assertIsSatisfied();
    }

    @Test
    @Order(13)
    void testDirectJdbcInsuredSelectCnt() throws Exception {
        String routeId = "DirectJdbcInsuredSelectCnt";
        String routeUri = "direct:jdbcinsuredselectcnt";

        AdviceWith.adviceWith(
                camelContext,
                routeId,
                r -> {
                    r.replaceFromWith(routeUri);
                    r.weaveAddLast().to(mockEndpoint);
                    r.weaveAddLast().log("endpoint: ${body}");
                });

        mockEndpoint.setResultWaitTime(600);
        mockEndpoint.setAssertPeriod(600);

        producertemplate.sendBody(routeUri, ExchangePattern.InOut, "");
        Object received = mockEndpoint.getReceivedExchanges().get(0).getIn().getBody();
        log.debug("received: [{}]", received);
        @SuppressWarnings("unchecked")
        List<Map<String, Long>> receivedList = (List<Map<String, Long>>) received;
        log.debug("receivedList: [{}]", receivedList);
        Integer resultCnt = receivedList.get(0).get("cnt").intValue();
        assertEquals(getInsuredRecordCnt(), resultCnt);
    }

    @Test
    @Order(14)
    void testDirectJdbcInsuredInsert() throws IOException, InterruptedException {
        Insured insured = new Insured();
        insured.setGivenName("James");
        insured.setMiddleName("George");
        insured.setSurname("Willmore");

        Insured obj =
                (Insured)
                        producertemplate.sendBody(
                                "direct:jdbcinsuredinsert", ExchangePattern.InOut, insured);
        log.debug("-->> obj: {}", obj);

        assertNotNull(obj.getId());
        insured.setId(obj.getId());
        assertEquals(insured, obj);
    }
}
