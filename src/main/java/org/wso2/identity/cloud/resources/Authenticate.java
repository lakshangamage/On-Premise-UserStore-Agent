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
package org.wso2.identity.cloud.resources;

import org.apache.commons.collections.FastHashMap;
import org.json.simple.JSONObject;
import org.wso2.identity.cloud.manager.UserStoreManager;
import org.wso2.identity.cloud.manager.ldap.LDAPUserStoreManager;
import org.wso2.identity.cloud.object.Credentials;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;

@Path("/authenticate")
public class Authenticate {
        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public JSONObject authenticate(Credentials credentials) {
            Boolean isAuthenticated = false;
            Map<String , Boolean> returnMap = new FastHashMap();
            try {
                UserStoreManager ldapUserStoreManager = new LDAPUserStoreManager();
                isAuthenticated = ldapUserStoreManager.authenticate(credentials);
            } catch (IOException e) {
                isAuthenticated = false;
            }
            returnMap.put("authenticated", isAuthenticated);
            return new JSONObject(returnMap);
        }
}
