package studentcapture.lti;

import org.imsglobal.aspect.Lti;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import studentcapture.submission.Submission;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Will take care of all the communication towards any LTI-related usage.
 * This will test the register.
 * @version 0.1, 04/29/16
 */
@RestController
@RequestMapping(value = "lti")
public class LTICommunicatorRegister {


    @Autowired
    private RestTemplate requestSender;


    /**
     * Using the feedback the grade will be set in the assignments feedback
     * grade.
     * @param submission  The feedback that will be graded in the LMS.
     * @throws LTINullPointerException      No LTI is valid for this grading.
     * @throws LTIInvalidGradeException     The given grade in feedback was
     *                                      invalid in LMS.
     * @throws LTISignatureException        LTI communication not set up.
     */
    @CrossOrigin
    @RequestMapping(value = "setGrade", method = RequestMethod.GET)
    public static void setGrade(Submission submission)
            throws LTINullPointerException, LTIInvalidGradeException,
            LTISignatureException {

        //TODO: Find the LMS of the feedback.
        //TODO: Find the Student LMS token.
        //TODO: Set grade in LMS with LTI.
    }

    @Lti
    @RequestMapping(value = "", method = RequestMethod.POST
            ,headers = "content-type=application/x-www-form-urlencoded"
    )
    /*@ResponseBody
    public String settingLTIup(HttpServletRequest request, HttpServletResponse response) {
        LTI2Servlet ls = new LTI2Servlet();

        String profile_id = "TheProfileId";

        try {
            ls.registerToolProviderProfile(request, response, profile_id);
            System.out.println("getServiceURL:"+ls.getServiceURL(request));

        } catch (IOException e) {
            System.err.println("serr ioexception");
        }


        return "dunno";
    }
*/
    /**
     * Takes in LTIRegister
     * @see LTIRegister
     */
    public String setup(LTIRegister register) {


        /** 6.1.1 Request Access to a Tool
         * To register a new Tool within a Tool Consumer system, the Tool Consumer administrator
         * submits a so-called Tool Proxy Registration Request.  The Tool Consumer
         * administrator must know the web address (URL) to which this request should be
         * submitted within the Tool Provider system.  The LTI specification does not say how
         * the Tool Consumer administrator discovers this URL.  Typically, the URL will be
         * published somewhere, or the administrator might receive it in an email from the
         * Tool’s vendor or a service provider.
         *
         * Upon receiving a Tool Proxy Registration Request, the Tool Provider may optionally
         * require the user to authenticate himself or herself.  LTI does not prescribe any
         * particular authentication procedure.
         *
         * See Section 4.5 for a description of the fields in the Tool Proxy Registration Request.
         */
        System.err.println("LTICommunicator.setup");
        System.out.println( register.toString() );
/*
        LtiKeySecretService lkss = key -> "sekret";

        LtiVerifier ltiv = new LtiVerifier() {

            public LtiVerificationResult verify(HttpServletRequest request, String secret) throws LtiVerificationException {
                return null;
            }


            public LtiVerificationResult verifyParameters(Map<String, String> parameters, String url, String method, String secret) throws LtiVerificationException {
                return null;
            }
        };

        LtiLaunchVerifier llv = new LtiLaunchVerifier(lkss, ltiv);
        Lti lti = new Lti() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public boolean rejectIfBad() {
                System.err.println("Error lti.rejectifbad");
                return false;
            }

            @Override
            public String keyService() {
                return lkss.getSecretForKey("");
            }
        };
        //llv.verifyLtiLaunch()
*/


        /**
         * 6.1.2 Fetch Tool Consumer Profile
         *
         * The Tool Provider retrieves the Tool Consumer Profile by issuing an HTTP GET
         * request to the URL specified by the tc_profile_url parameter. The Tool Provider
         * specifies the version of LTI that it intends to use for the integration contract
         * by adding an lti_version query parameter to the URL.
         *
         * @see LTIRegister#getTc_profile_url
         * TODO: Need to confirm that grade can be set before accepting the Consumer.
         */
        String dbURI = register.getTc_profile_url();
        URI targetLTIConsumerProfile = UriComponentsBuilder.fromUriString(dbURI).build().toUri();
        JSONObject jsonObj = null;
        JSONObject toolProxyConsumer = null;
        String response = "";
        String uriLTIRegistration = "";
        try {
            response = requestSender.getForObject(targetLTIConsumerProfile, String.class);

            LTIConsumerProfile ltiCP = new LTIConsumerProfile(new JSONObject(response));
            toolProxyConsumer = ltiCP.getService("tcp:ToolProxy.collection");
            System.err.println("\n\nToolproxy:" + toolProxyConsumer);



           /*
            Iterator it = jsonObj.keys();
            System.err.println(targetLTIConsumerProfile);
            while(it.hasNext())
            {
                String key = it.next().toString();
                String value = jsonObj.get(key).toString();
                System.err.println("\t"+ key + ": " + value);
            }

            // Get services.
            JSONArray so = (JSONArray) jsonObj.get("service_offered");
            System.err.println("FOUND SERVICE_offered");
            System.err.println(so.toString());

            for (int i = 0; i < so.length(); i++) {

                JSONObject key = so.getJSONObject(i);
                //System.err.println("\tservice_offered -> " + key );//+ ": " + key2.toString());
                if (key.has("@id") && key.getString("@id").equals("tcp:ToolProxy.collection")) {
                    uriLTIRegistration = key.getString("endpoint");
                    System.out.println("HTTP:::: "+ uriLTIRegistration);
                } else {
                    String key3 = key.getString("endpoint");
                    System.out.println("!HTTP:::: "+key3);

                }

            }*/
        } catch (JSONException e1) {
            System.err.println(e1.getMessage());
            e1.printStackTrace();
        } catch (RestClientException e) {
            //TODO Maybe not good to send exceptions to browser?

            response += ("error:" + e.getMessage());
        }

        /**
         * 6.1.3 Register a Tool Proxy
         * As discussed above, Tool Consumer advertises in the Tool Consumer Profile those
         * services and capabilities that it is offering to the Tool Provider, and the Tool
         * Provider chooses a subset of those services and capabilities that it wishes to
         * enable. Once the Tool Provider has determined the services and capabilities that
         * will be used within the context of the integration contract, it constructs a Tool
         * Proxy as described in Section 5 and submits it to the Tool Consumer via an
         * HTTP request posted to the Tool Proxy REST service.  For more information about
         * this service, see Section 10.1.
         *
         * At this point, the Tool Proxy is registered with the Tool Consumer, but it is
         * not yet available for use.
         */

        String registerResponse = "No response.";
        HttpEntity httpEntity = new HttpEntity(null);
        if (toolProxyConsumer != null && toolProxyConsumer.has("endpoint")) {
            System.err.println("\n\n" + "############# uriLTIRegistration ############");
            try {
                uriLTIRegistration = toolProxyConsumer.get("endpoint").toString().replaceAll("localhost:8080", "int-nat.cs.umu.se:20003");
                System.err.println("URI:"+uriLTIRegistration);
                URI targetLTIRegister = UriComponentsBuilder.fromUriString(uriLTIRegistration).build().toUri();
                Map uriVariables = new HashMap<>();
                uriVariables.put("key", "fuck");
                RestTemplate restTemplate = new RestTemplate() {
                    @Override
                    protected ClientHttpRequest createRequest( URI url, HttpMethod method ) throws IOException {
                        ClientHttpRequest request = super.createRequest( url, method );
                        return request;
                    }
                };


                registerResponse = restTemplate.postForObject(uriLTIRegistration,
                                                                httpEntity,
                                                                String.class,
                                                                uriVariables
                                                                );

                System.err.println(registerResponse);
            } catch (HttpClientErrorException e) {
                //400 cause of IMSJSONRequest.loadFromRequest
                System.err.println("error requestSender2:"+ e.getMessage());
                System.err.println("error requestSender2:"+ e.getCause());
                System.err.println("error requestSender2:"+ e.getClass());
                System.err.println("error requestSender2:"+e.getResponseBodyAsString());
                System.err.println("error requestSender2:"+ registerResponse);
                System.err.println("error requestSender2:"+ httpEntity.toString());
                System.err.println("error requestSender2:"+ Arrays.toString(e.getStackTrace()).substring(0, 300));

            } catch (Exception e) {
                System.err.println("error requestSender2:"+ e.getMessage());
                System.err.println("error requestSender2:"+ e.getCause());
                System.err.println("error requestSender2:"+ e.getClass());
                System.err.println("error requestSender2:"+ Arrays.toString(e.getStackTrace()).substring(0, 300));
            }
        }

        /**
         * 6.1.4 Make the Tool Proxy Available
         * Before the Tool Proxy can be used within the Tool Consumer, it must be placed into
         * the “available” state.  The Tool Provider will not have access to any Tool Consumer
         * services until such access has been authorized by the Tool Consumer administrator.
         *
         * Once the Tool Proxy has been registered, the Tool Provider redirects the user’s
         * browser back to the Tool Consumer system at the URL specified by the
         * launch_presentation_return_url parameter of the Tool Proxy Registration Request.
         *
         * To this URL, the Tool Provider appends the following HTTP query parameters:
         *      status = success
         *      tool_proxy_guid = <globally unique identifier for the Tool Proxy>
         * where the value for the tool_proxy_guid parameter is obtained from the response
         * returned by the Tool Proxy REST service.  Typically, this action redirects the
         * browser to a page within the Tool Consumer system where the Tool Proxy can be
         * made available.
         *
         * At this point, the Tool Consumer should display a warning about the security
         * implications of making the Tool Proxy available. For each data type (personal
         * information, course information, grades, etc.) the Tool Consumer should disclose
         * the access permissions being requested by the Tool Provider. As a general rule,
         * the Tool Consumer should not merely report the specific web service operations and
         * that the Tool Provider will be authorized to access since this information may not
         * be meaningful to the typical administrator.  Instead, for each data type, the Tool
         * Consumer should disclose whether or not the Tool Provider will be able to create,
         * read, update or delete that kind of data.
         *
         * These details are derived from the security contract and the variables that are
         * referenced in the Tool Proxy. For example, suppose the Tool Proxy reveals that
         * the Tool Provider intends to utilize the Person.name.given variable. In this case,
         * the Tool Consumer should disclose that the Tool will be granted permission to read
         * personally identifiable information. Similarly, if the Tool Proxy reveals that the
         * Tool Provider intends to use the Result Service, the Tool Consumer should disclose
         * that the Tool will be granted read/write permissions for scores in the online
         * grade book. This disclosure step is a best practice.
         * It is not required for LTI compliance.
         *
         * Once the Tool Consumer administrator has reviewed the disclosures, he or she should
         * perform an action to make the Tool Proxy available within the Tool Consumer system
         * – typically by clicking a button on the web page.
         */

        return register.toString() + "\n\n" + response + "\n\n" + "\n\n" + registerResponse.toString();


        /** 6.2 Enabling Tools within a Learning Context
         * Typically, Tools that are available within a Tool Consumer are not automatically enabled
         * for use within every course section or learning context managed by the Tool Consumer.
         * Instead, Tools must be enabled within each individual learning context.
         *
         * The practice of enabling Tools within a learning context serves several purposes:
         * -Allows users to choose which version of a Tool is appropriate for a given learning
         *  context (assuming that the Tool Consumer supports multiple, side-by-side versions of
         *  a Tool).
         * -Allows the Tool Consumer to track Tool usage.
         * -Allows users to choose the right set of Tools for the learning context so that they are
         * not overwhelmed by options for Tools that are completely irrelevant.
         *
         * The Tool Consumer’s user interface should provide some way for Instructors and Course
         * Builders to view a list of all available Tools and then enable or disable the ones
         * that are of interest to them in any given learning context. The Tool Consumer may
         * choose to automatically enable certain Tools in every learning context by default.
         *
         *
         * The association of a Tool Proxy with a learning context is called a Tool Proxy Binding.
         * LTI 2.0 does not define an explicit representation for a Tool Proxy Binding, but
         * conceptually it contains the following attributes:
         * Identifier for a learning context
         * Identifier for a Tool Proxy
         * Status of the Tool Proxy within the learning context (enabled or disabled).
         */
    }

}
