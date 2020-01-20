/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.shiro.test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ContainerIntegrationArquillianIT {

    private static String archiveName;

    @Deployment(testable = false)
    public static Archive<?> app() {
        archiveName = System.getProperty("project.artifactId") + "-" + System.getProperty("project.version");
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/" + archiveName + ".war"));
    }

    @ArquillianResource
    private URL base;

    protected final WebClient webClient = new WebClient();

    protected String getBaseUri() {
        return base.toExternalForm();
    }

    @Test
    public void logIn() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {

        HtmlPage page = webClient.getPage(getBaseUri() + "login.jsp");
        HtmlForm form = page.getFormByName("loginform");
        form.<HtmlInput>getInputByName("username").setValueAttribute("root");
        form.<HtmlInput>getInputByName("password").setValueAttribute("secret");
        page = form.<HtmlInput>getInputByName("submit").click();
        // This'll throw an expection if not logged in
        page.getAnchorByHref("/"+ archiveName + "/logout");
    }
}
