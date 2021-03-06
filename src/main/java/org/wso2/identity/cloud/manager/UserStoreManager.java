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
package org.wso2.identity.cloud.manager;

import org.wso2.identity.cloud.object.AttributeList;
import org.wso2.identity.cloud.object.Credentials;
import org.wso2.identity.cloud.object.User;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserStoreManager {
    public boolean authenticate(Credentials credentials) throws IOException;
    public Map<String,Object> getUserAttributes(String username, AttributeList attributeList) throws IOException;
    public List<User> getAllUserNames() throws IOException;
}
