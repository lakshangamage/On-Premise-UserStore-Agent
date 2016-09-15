package org.wso2.identity.cloud.manager;

import org.apache.commons.collections.FastHashMap;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.api.ConnectorFacade;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;
import org.json.simple.JSONObject;
import org.wso2.identity.cloud.connector.LDAPConnector;
import org.wso2.identity.cloud.object.AttributeList;
import org.wso2.identity.cloud.object.Credentials;
import java.io.IOException;
import java.util.Map;
//Header
public class LDAPUserStoreManager {
    ConnectorFacade connection = null;
    ConnectorObject connectorObject;

    //ConnectorObject connectorObject = conn.getObject(ObjectClass.ACCOUNT,userUid,null);

    public boolean authenticate(Credentials credentials) throws IOException {
        connection = LDAPConnector.getConnector().getConnection();
        try {
            connection.authenticate(ObjectClass.ACCOUNT, credentials.getUsername(), new GuardedString(credentials.getPassword().toCharArray()), null);
            return true;
        }catch (RuntimeException ex){
            return false;
        }
    }

    public JSONObject getUserAttributes(String username, AttributeList attributeList) throws IOException {
        Map<String,Object> userAttributes = new FastHashMap();
        connection = LDAPConnector.getConnector().getConnection();
        Filter userNameFilter = FilterBuilder.equalTo(new Name("uid="+username+",ou=system")); //Configurable

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
        return new JSONObject(userAttributes);
    }
}
