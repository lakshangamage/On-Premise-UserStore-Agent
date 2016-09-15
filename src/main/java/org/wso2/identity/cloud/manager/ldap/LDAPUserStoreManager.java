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
package org.wso2.identity.cloud.manager.ldap;

import org.apache.commons.collections.FastHashMap;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;
import org.json.simple.JSONObject;
import org.wso2.identity.cloud.connector.LDAPConnector;
import org.wso2.identity.cloud.manager.UserStoreManager;
import org.wso2.identity.cloud.object.AttributeList;
import org.wso2.identity.cloud.object.Credentials;
import org.wso2.identity.cloud.object.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LDAPUserStoreManager implements UserStoreManager{
    ConnectorFacade connection = null;
    ConnectorObject connectorObject;
    List<User> users;

    public boolean authenticate(Credentials credentials) throws IOException {
        connection = LDAPConnector.getConnector().getConnection();
        try {
            connection.authenticate(ObjectClass.ACCOUNT, credentials.getUsername(), new GuardedString(credentials.getPassword().toCharArray()), null);
            return true;
        }catch (RuntimeException ex){
            return false;
        }
    }

    public Map<String,Object> getUserAttributes(String username, AttributeList attributeList) throws IOException {
        Map<String,Object> userAttributes = new FastHashMap();
        connection = LDAPConnector.getConnector().getConnection();
        Filter userNameFilter = FilterBuilder.equalTo(new Name("uid="+username+",ou=system"));

        ResultsHandler handler = new ResultsHandler() {
            public boolean handle(ConnectorObject obj) {
                connectorObject = obj;
                return true;
            }
        };

        connection.search(ObjectClass.ACCOUNT, userNameFilter, handler,null);

        if(connectorObject != null){
            Attribute attribute;
            userAttributes.put("username",username);
            for (String attributeName : attributeList.getAttributes()) {
                attribute = connectorObject.getAttributeByName(attributeName);
                if(attribute!=null){
                    userAttributes.put(attribute.getName(),attribute.getValue());
                }

            }
        }
        return userAttributes;
    }

    public List<User> getAllUserNames() throws IOException {
        users = new ArrayList<User>();
        connection = LDAPConnector.getConnector().getConnection();
        ResultsHandler handler = new ResultsHandler() {
            public boolean handle(ConnectorObject obj) {
                User user = new User();
                user.setUsername(obj.getAttributeByName("uid").getValue().toArray()[0].toString());
                users.add(user);
                return true;
            }
        };

        connection.search(ObjectClass.ACCOUNT, null, handler,null);
        return users;
    }
}
