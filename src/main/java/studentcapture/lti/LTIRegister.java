package studentcapture.lti;

/**
 * Tool Proxy Registration Request
 * The following information is taken from imsglobal documentation for implementation.
 * @link https://www.imsglobal.org/specs/ltiv2p0/implementation-guide
 */
public class LTIRegister {
    /** external (Irrelevant submitButton id) **/
    String ext_basiclti_submit;

    /** The Tool Consumer generates a set of credentials (key and password) that
      * the Tool Provider uses to register a new Tool Proxy within the Tool Consumer.
     * These credentials are valid for one use only, and they expire after a short time,
     * typically about one hour.  When the Tool Provider posts a new Tool Proxy to the
     * Tool Consumer, these parameters are used as the oauth_consumer_key and
     * oauth_consumer_secret respectively to digitally sign the HTTP request in
     * accordance with the OAuth protocol.
     *
     * The Tool Consumer may use the reg_key as the GUID for the new Tool Proxy,
     * but there is no requirement to do so. The reg_password is valid for one use only.
     * Any attempt to use the key and password again after a successful submission should
     * result in a failure with a 401 (Unauthorized) status code.
     * */
    String reg_key;
    String reg_password;

    /**
     * This URL specifies the address where the Tool Provider can retrieve the
     * Tool Consumer Profile. This URL must be retrievable by a GET request by
     * the Tool Provider.  If the URL is protected from retrieval in general,
     * the Tool Consumer must append the necessary parameters to allow the Tool Provider
     * to retrieve the URL with nothing more than a GET request.  It is legal for this
     * URL to contain a security token that is changed for each ToolProxyRegistrationRequest
     * so the Tool Provider must retrieve the tc_profile_url on each request.
     */
    String tc_profile_url;

    /**
     * The Tool Provider redirects the user's browser back to this URL when it has completed
     * its part of the tool deployment process.  The Tool Provider should redirect to this
     * URL regardless of whether the deployment process succeeds or fails.  In addition to
     * the log and message parameters (described above for the Basic LTI Launch Request
     * message type) the Tool Provider appends the following HTTP query parameters

     status=success | failure

     tool_guid=<globally unique identifier for the Tool Proxy>

     where the value for the tool_guid parameter is given by the return value from the Tool
     Proxy creation request  if the operation was successful.  Typically, this action redirects
     the administrator’s browser to the Tool Console within the Tool Consumer system where
     the Tool Proxy can be made available.
     */
    String launch_presentation_return_url;

    String ToolProxyRegistrationRequest;
    String lti_version;


    public String getExt_basiclti_submit() {
        return ext_basiclti_submit;
    }

    public void setExt_basiclti_submit(String ext_basiclti_submit) {
        this.ext_basiclti_submit = ext_basiclti_submit;
    }

    public String getLaunch_presentation_return_url() {
        return launch_presentation_return_url;
    }

    public void setLaunch_presentation_return_url(String launch_presentation_return_url) {
        this.launch_presentation_return_url = launch_presentation_return_url;
    }

    public String getToolProxyRegistrationRequest() {
        return ToolProxyRegistrationRequest;
    }

    public void setToolProxyRegistrationRequest(String toolProxyRegistrationRequest) {
        ToolProxyRegistrationRequest = toolProxyRegistrationRequest;
    }

    public String getLti_version() {
        return lti_version;
    }

    public void setLti_version(String lti_version) {
        this.lti_version = lti_version;
    }

    public String getReg_key() {
        return reg_key;
    }

    public void setReg_key(String reg_key) {
        this.reg_key = reg_key;
    }

    public String getReg_password() {
        return reg_password;
    }

    public void setReg_password(String reg_password) {
        this.reg_password = reg_password;
    }

    public String getTc_profile_url() {
        return tc_profile_url;
    }

    public void setTc_profile_url(String tc_profile_url) {
        this.tc_profile_url = tc_profile_url;
    }

    @Override
    public String toString() {
        return "LTIRegister{" +
                "\n\text_basiclti_submit='" + ext_basiclti_submit + '\'' +
                "\n\t, launch_presentation_return_url='" + launch_presentation_return_url + '\'' +
                "\n\t, ToolProxyRegistrationRequest='" + ToolProxyRegistrationRequest + '\'' +
                "\n\t, lti_version='" + lti_version + '\'' +
                "\n\t, reg_key='" + reg_key + '\'' +
                "\n\t, reg_password='" + reg_password + '\'' +
                "\n\t, tc_profile_url='" + tc_profile_url + '\'' + "\n" +
                '}';
    }
}
