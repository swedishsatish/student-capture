/**
 * Copyright 2014 Unicon (R)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studentcapture.ltistarter.database;

import studentcapture.ltistarter.config.ApplicationConfig;
import studentcapture.ltistarter.model.LtiKeyEntity;
import studentcapture.ltistarter.model.LtiUserEntity;
import studentcapture.ltistarter.model.ProfileEntity;
import studentcapture.ltistarter.repository.LtiKeyRepository;
import studentcapture.ltistarter.repository.LtiUserRepository;
import studentcapture.ltistarter.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Check if the database has initial data in it,
 * if it is empty on startup then we populate it with some initial data
 */
@Component
@Profile("!testing")
// only load this when running the application (not for unit tests which have the 'testing' profile active)
public class DatabasePreload {

    final static Logger log = LoggerFactory.getLogger(DatabasePreload.class);

    @Autowired
    public ApplicationConfig applicationConfig;

    @Autowired
    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "SpringJavaAutowiringInspection"})
    public LtiKeyRepository ltiKeyRepository;
    @Autowired
    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "SpringJavaAutowiringInspection"})
    public LtiUserRepository ltiUserRepository;
    @Autowired
    @SuppressWarnings({"SpringJavaAutowiredMembersInspection", "SpringJavaAutowiringInspection"})
    public ProfileRepository profileRepository;

    @PostConstruct
    public void init() {
        if (ltiKeyRepository.count() > 0) {
            // done, no preloading
            log.info("INIT - no preload");
        } else {
            // preload the sample data
            log.info("INIT - preloaded keys and user");
            // create our sample key
            ltiKeyRepository.save(new LtiKeyEntity("key", "secret"));
            // create our sample user
            LtiUserEntity user = ltiUserRepository.save(new LtiUserEntity("azeckoski", null));
            ProfileEntity profile = profileRepository.save(new ProfileEntity("AaronZeckoski", null, "azeckoski@test.com"));
            // now add profile to the user
            user.setProfile(profile);
            profile.getUsers().add(user);
            ltiUserRepository.save(user);
        }
    }

}
