/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.drools.experiment.rule_unit.iothome;

import static org.drools.core.DataSource.asDataSource;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.drools.compiler.Person;
import org.drools.core.datasources.ListDataSource;
import org.drools.core.event.DefaultAgendaEventListener;
import org.drools.core.impl.DynamicallyBoundSessionImpl;
import org.drools.core.ruleunit.RuleUnitFactory;
import org.drools.core.time.SessionPseudoClock;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleTest {
    static final Logger LOG = LoggerFactory.getLogger(RuleTest.class);

    @Test
    public void test() {
        KieServices kieServices = KieServices.Factory.get();

        KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
            LOG.info("{}", m);
        }

        LOG.info("Creating kieBase");
        KieBase kieBase = kContainer.getKieBase();

        LOG.info("There should be rules: ");
        for ( KiePackage kp : kieBase.getKiePackages() ) {
            for (Rule rule : kp.getRules()) {
                LOG.info("kp " + kp + " rule " + rule.getName());
            }
        }

        LOG.info("Creating kieSession");
        DynamicallyBoundSessionImpl session = new DynamicallyBoundSessionImpl();
        session.bind( kieBase );

        RuleUnitFactory factory = new RuleUnitFactory()
//                .bindVariable( "persons", persons )
                ;
        session.setRuleUnitFactory( factory );
        
        session.addEventListener(new DefaultAgendaEventListener(){
            public void afterMatchFired(AfterMatchFiredEvent event) {
                System.out.println("fired: "+event.getMatch().getRule().getName());
            }
        });
        session.addEventListener(new DebugAgendaEventListener());

        System.out.println("STEP INIT");
        session.insert(new WiFiDevice("mobile"));
        session.fireAllRules();

        System.out.println("STEP 1");
        session.insert(new MotionDetected("LivingRoom"));
        session.fireAllRules();
    }
}