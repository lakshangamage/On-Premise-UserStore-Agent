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

package org.wso2.identity.cloud.resources;

import org.json.simple.JSONObject;
import org.wso2.identity.cloud.manager.ldap.LDAPUserStoreManager;
import org.wso2.identity.cloud.manager.UserStoreManager;
import org.wso2.identity.cloud.object.AttributeList;
import org.wso2.identity.cloud.object.User;
import org.wso2.identity.cloud.object.UserList;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1
 */
@Path("/users")
public class UserResource {

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JSONObject getUserAttributes(@PathParam("username") String username, AttributeList attributeList){

        try {
            LDAPUserStoreManager ldapConnector = new LDAPUserStoreManager();
            return new JSONObject(ldapConnector.getUserAttributes(username,attributeList));

        } catch (IOException e) {
            return new JSONObject();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public UserList getAllUserNames(){

        try {
            UserStoreManager ldapConnector = new LDAPUserStoreManager();
            List<User>  users = ldapConnector.getAllUserNames();
            ArrayList<String> usernames = new ArrayList<>();
            for (User user :
                    users) {
                usernames.add(user.getUsername());
            }
            return new UserList(usernames);

        } catch (IOException e) {
            return new UserList();
        }
    }
}
