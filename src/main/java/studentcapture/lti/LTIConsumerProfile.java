package studentcapture.lti;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jwestin on 2016-05-17.
 * @deprecated New class created.
 * @see studentcapture.lti.LTIConsumerProfileHandler
 */
public class LTIConsumerProfile {

    JSONObject theProfile;

    public LTIConsumerProfile(JSONObject profile) throws JSONException {
        System.err.println("LTIConsumerProfile");
        theProfile = profile;
        /*Iterator<JSONObject> it = profile.keys();
        while(it.hasNext())
        {
            String key = it.next().toString();
            String value = profile.get(key).toString();
            if (key.equals("@context")) {
                setContext(value);
            }

            System.err.println("\n\t"+ key + ": " + value);
        }


        // Get services.
        JSONArray so = (JSONArray) profile.get("service_offered");
        System.err.println("FOUND SERVICE_offered");
        System.err.println(so.toString());
        */


    }

    /**
     *
     * @param id    The name of the services requested.
     * @return      A JSONObject of the services.
     * The JSONObject returned:
     *      "@type" : "RestService",
     *      "@id" : "tcp:ToolProxy.collection",
     *      "endpoint" : "http://localhost:8080/imsblis/lti2/tc_registration/5692e425-d380-4e3f-a31f-dc6081a0c932",
     *      "format" : [ "application/vnd.ims.lti.v2.toolproxy+json" ],
     *      "action" : [ "POST" ]
     *      //"tcp:ToolProxy.collection"
     */
    public JSONObject getService(String id) {
        String serviceObjStr = "service_offered";
        try {
            JSONArray so;
            so = (JSONArray) theProfile.get(serviceObjStr);
            for (int i = 0; i < so.length(); i++) {
                JSONObject key = so.getJSONObject(i);
                if (key.has("@id") && key.getString("@id").equals(id)) {
                    return key;
                }
            }
        } catch (JSONException ignored) {

        }
        return null;

    }

    @Override
    public String toString() {
        return "LTIConsumerProfile{" +
                "theProfile=" + theProfile +
                '}';
    }


/**
    "@context" : [ "http://purl.imsglobal.org/ctx/lti/v2/ToolConsumerProfile", {
        "tcp" : "http://localhost:8080/imsblis/lti2/"
    } ],
            "@type" : "ToolConsumerProfile",
            "lti_version" : "LTI-2p0",
            "guid" : "5692e425-d380-4e3f-a31f-dc6081a0c932",
            "product_instance" : {
        "guid" : "sakai.school.edu",
                "product_info" : {
            "product_name" : {
                "default_value" : "Sakai",
                        "key" : "product.name"
            },
            "product_version" : "10.0",
                    "description" : {
                "default_value" : "Sakai 10.0",
                        "key" : "product.vendor.description"
            },
            "product_family" : {
                "code" : "sakai",
                        "vendor" : {
                    "code" : "sakai",
                            "vendor_name" : {
                        "default_value" : "Sakai Project",
                                "key" : "product.vendor.name"
                    },
                    "description" : {
                        "default_value" : "Sakai is an Apereo Project.",
                                "key" : "product.vendor.description"
                    },
                    "website" : "http://www.sakaiproject.org",
                            "contact" : {
                        "email" : "info@sakaiproject.org"
                    }
                }
            }
        },
        "support" : {
            "email" : "sakai-support@school.edu"
        },
        "service_owner" : {
            "@id" : "https://sakai.school.edu",
                    "service_owner_name" : {
                "default_value" : "ETS",
                        "key" : "product.name"
            },
            "description" : {
                "default_value" : "The Ed. Tech Services Division.",
                        "key" : "product.vendor.description"
            },
            "support" : {
                "email" : "ets-support@sakai.school.edu"
            }
        },
        "service_provider" : {
            "@id" : "https://www.asca.edu/",
                    "service_provider_name" : {
                "default_value" : "ASCA, Inc.",
                        "key" : "product.name"
            },
            "description" : {
                "default_value" : "A Sakai Commercial Affiliate",
                        "key" : "product.vendor.description"
            },
            "support" : {
                "email" : "sales@asca.com"
            }
        }
    },
            "capability_offered" : [ "basic-lti-launch-request", "User.id", "User.image", "CourseSection.sourcedId", "Person.sourcedId", "Membership.role", "Person.email.primary", "User.username", "Person.name.fullname", "Person.name.given", "Person.name.family", "Person.name.full", "Result.sourcedId", "Result.autocreate", "Result.url" ],
            "service_offered" : [ {
        "@type" : "RestService",
                "@id" : "tcp:ToolProxy.collection",
                "endpoint" : "http://localhost:8080/imsblis/lti2/tc_registration/5692e425-d380-4e3f-a31f-dc6081a0c932",
                "format" : [ "application/vnd.ims.lti.v2.toolproxy+json" ],
        "action" : [ "POST" ]
    }, {
        "@type" : "RestService",
                "@id" : "tcp:Result.item",
                "endpoint" : "http://localhost:8080/imsblis/lti2/Result/{lis_result_sourcedid}",
                "format" : [ "application/vnd.ims.lis.v2.result+json" ],
        "action" : [ "GET", "PUT" ]
    }, {
        "@type" : "RestService",
                "@id" : "tcp:LTI_1_1_ResultService",
                "endpoint" : "http://localhost:8080/imsblis/service/",
                "format" : [ "application/vnd.ims.lti.v1.outcome+xml" ],
        "action" : [ "POST" ]
    }, {
        "@type" : "RestService",
                "@id" : "tcp:SakaiOutcomeForm",
                "endpoint" : "http://localhost:8080/imsblis/service/",
                "format" : [ "application/vnd.sakai.lti.v1.outcome+form" ],
        "action" : [ "POST" ]
    } ]

*/
}
