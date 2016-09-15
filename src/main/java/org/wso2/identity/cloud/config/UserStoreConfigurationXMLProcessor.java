/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.identity.cloud.config;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.util.Map;


public class UserStoreConfigurationXMLProcessor {
    protected static String USERSTORE_CONFIG_FILE = "userstore-config.xml";
    InputStream inStream = null;
    public Map<String, String> buildRealmConfigurationFromFile(){
        OMElement realmElement;
        try {
            realmElement = getRealmElement();

            RealmConfiguration realmConfig = buildRealmConfiguration(realmElement);

            if (inStream != null) {
                inStream.close();
            }
            return realmConfig;
        } catch (Exception e) {
            String message = "Error while reading realm configuration from file";
            if (log.isDebugEnabled()) {
                log.debug(message, e);
            }
            throw new UserStoreException(message, e);
        }

    }

    private OMElement getRootElement() throws XMLStreamException, IOException{
        StAXOMBuilder builder = null;

        File profileConfigXml = new File(UserStoreUtils.getConfigDirPath(),
                USERSTORE_CONFIG_FILE);
        if (profileConfigXml.exists()) {
            inStream = new FileInputStream(profileConfigXml);
        }

        String warningMessage = "";
        if (inStream == null) {
            URL url;
            if (bundleContext != null) {
                if ((url = bundleContext.getBundle().getResource(REALM_CONFIG_FILE)) != null) {
                    inStream = url.openStream();
                } else {
                    warningMessage = "Bundle context could not find resource "
                            + REALM_CONFIG_FILE
                            + " or user does not have sufficient permission to access the resource.";
                }
            } else {
                if ((url = ClaimBuilder.class.getResource(REALM_CONFIG_FILE)) != null) {
                    inStream = url.openStream();
                    log.error("Using the internal realm configuration. Strictly for non-production purposes.");
                } else {
                    warningMessage = "ClaimBuilder could not find resource "
                            + REALM_CONFIG_FILE
                            + " or user does not have sufficient permission to access the resource.";
                }
            }
        }

        if (inStream == null) {
            String message = "Profile configuration not found. Cause - " + warningMessage;
            if (log.isDebugEnabled()) {
                log.debug(message);
            }
            throw new FileNotFoundException(message);
        }

        try {
            inStream = CarbonUtils.replaceSystemVariablesInXml(inStream);
        } catch (CarbonException e) {
            throw new UserStoreException(e.getMessage(), e);
        }
        builder = new StAXOMBuilder(inStream);
        OMElement documentElement = builder.getDocumentElement();

        setSecretResolver(documentElement);

        OMElement realmElement = documentElement.getFirstChildWithName(new QName(
                UserCoreConstants.RealmConfig.LOCAL_NAME_REALM));

        return realmElement;
    }
}
