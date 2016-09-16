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
package org.wso2.identity.cloud.constants;


public class LDAPConstants {
    public static String BUNDLE_NAME = "net.tirasa.connid.bundles.ldap";
    public static String BUNDLE_VERSION = "1.5.1";
    public static String CONNECTOR_NAME = "net.tirasa.connid.bundles.ldap.LdapConnector";
    public static String JAR_NAME = "ldapcon.jar";
    public static String HOST = "host";
    public static String PORT = "port";
    public static String SSL = "ssl";
    public static String PRINCIPAL = "principal";
    public static String CREDENTIALS = "credentials";
    public static String BASE_CONTEXTS = "baseContexts";
    public static String PASSWORD_ATTRIBUTE = "passwordAttribute";
    public static String SEARCH_BASE = "searchBase";
    public static String UID = "uid";
    public static String USER_NAME = "username";



    public static class UserStoreProperties{
        public static String HOST = "localhost";
        public static int PORT = 10389;
        public static boolean SSL = false;
        public static String PRINCIPAL = "uid=admin,ou=system";
        public static String CREDENTIALS = "lakshan";
        public static String[] BASE_CONTEXTS = new String[]{"ou=system"};
        public static String PASSWORD_ATTRIBUTE = "userPassword";
        public static String SEARCH_BASE = "ou=system";
    }


}
