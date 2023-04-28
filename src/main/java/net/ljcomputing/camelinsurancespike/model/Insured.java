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
package net.ljcomputing.camelinsurancespike.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** Insured entity. */
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "insured")
public class Insured implements Serializable {
    private static final long serialVersionUID = -6852426499153687162L;

    /** Entity id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private Long id;

    /** Insured's given name. */
    @Column(name = "given_name")
    private String givenName;

    /** Insured's middlename. */
    @Column(name = "middle_name")
    private String middleName;

    /** Insured's surname. */
    @Column(name = "surname")
    private String surname;

    /** Insured's suffix. */
    @Column(name = "suffix")
    private String suffix;
}
