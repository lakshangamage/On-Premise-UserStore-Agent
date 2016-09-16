/*
 * Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.identity.cloud.connector;

import org.identityconnectors.common.IOUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.identity.cloud.config.UserStoreConfigurationXMLProcessor;
import org.wso2.identity.cloud.constants.LDAPConstants;
import org.wso2.identity.cloud.util.UserStoreUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class LDAPConnector{
    private static Logger log = LoggerFactory.getLogger(LDAPConnector.class);
    private static ConnectorFacade connection;
    private static LDAPConnector connectorInstance= new LDAPConnector();

    private LDAPConnector(){
        try {
            init();
        }catch (IOException ex){
            log.error(ex.getMessage());
        }
    }
    public static synchronized LDAPConnector getConnector(){

        if(connectorInstance==null){
            connectorInstance = new LDAPConnector();
        }

            return connectorInstance;
    }

    private void init() throws IOException{
        configureProperties();
        // Use the ConnectorInfoManager to retrieve a ConnectorInfo object for the connector
        ConnectorInfoManagerFactory fact = ConnectorInfoManagerFactory.getInstance();
        File bundleDirectory = new File(UserStoreUtils.getConfigDirPath());
        URL url = IOUtil.makeURL(bundleDirectory, LDAPConstants.JAR_NAME);
        ConnectorInfoManager manager = fact.getLocalManager(url);
        ConnectorKey key = new ConnectorKey(LDAPConstants.BUNDLE_NAME, LDAPConstants.BUNDLE_VERSION, LDAPConstants.CONNECTOR_NAME);
        ConnectorInfo connectorInfo = manager.findConnectorInfo(key);
        // From the ConnectorInfo object, create the default APIConfiguration.
        APIConfiguration apiConfig = connectorInfo.createDefaultAPIConfiguration();
        // From the default APIConfiguration, retrieve the ConfigurationProperties.
        ConfigurationProperties properties = apiConfig.getConfigurationProperties();
        // Set all of the ConfigurationProperties needed by the connector.
        properties.setPropertyValue(LDAPConstants.HOST, LDAPConstants.UserStoreProperties.HOST);
        properties.setPropertyValue(LDAPConstants.PORT, LDAPConstants.UserStoreProperties.PORT);
        properties.setPropertyValue(LDAPConstants.SSL, LDAPConstants.UserStoreProperties.SSL);
        properties.setPropertyValue(LDAPConstants.PRINCIPAL, LDAPConstants.UserStoreProperties.PRINCIPAL);
        properties.setPropertyValue(LDAPConstants.CREDENTIALS, new GuardedString(LDAPConstants.UserStoreProperties.CREDENTIALS.toCharArray()));
        properties.setPropertyValue(LDAPConstants.BASE_CONTEXTS, LDAPConstants.UserStoreProperties.BASE_CONTEXTS);
        properties.setPropertyValue(LDAPConstants.PASSWORD_ATTRIBUTE, LDAPConstants.UserStoreProperties.PASSWORD_ATTRIBUTE);
        // Use the ConnectorFacadeFactory's newInstance() method to get a new connector.
        connection = ConnectorFacadeFactory.getInstance().newInstance(apiConfig);
        // Make sure we have set up the Configuration properly
        connection.validate();
    }

    public ConnectorFacade getConnection() {
        return connection;
    }

    public void configureProperties(){
        UserStoreConfigurationXMLProcessor userStoreConfigurationXMLProcessor =  new UserStoreConfigurationXMLProcessor();
        Map<String,String> configProperties  = userStoreConfigurationXMLProcessor.buildUserStoreConfigurationFromFile();
        LDAPConstants.UserStoreProperties.HOST = configProperties.get(LDAPConstants.HOST);
        LDAPConstants.UserStoreProperties.PORT = Integer.parseInt(configProperties.get(LDAPConstants.PORT));
        LDAPConstants.UserStoreProperties.SSL = Boolean.valueOf(configProperties.get(LDAPConstants.SSL));
        LDAPConstants.UserStoreProperties.PRINCIPAL = configProperties.get(LDAPConstants.PRINCIPAL);
        LDAPConstants.UserStoreProperties.CREDENTIALS = configProperties.get(LDAPConstants.CREDENTIALS);
        LDAPConstants.UserStoreProperties.SEARCH_BASE = configProperties.get(LDAPConstants.SEARCH_BASE);
        LDAPConstants.UserStoreProperties.BASE_CONTEXTS = configProperties.get(LDAPConstants.BASE_CONTEXTS).split(",");
    }

}
