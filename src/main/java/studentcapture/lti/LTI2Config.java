package studentcapture.lti;

/**
 * Capture the information needed to build a tool profile
 */
public class LTI2Config implements org.imsglobal.lti2.LTI2Config {
    // A globally unique identifier for the service.  The domain name is typical.
    // The scope for this is tenant/customer

    @Override
    public String getGuid() {
        return "Student-Capture";
    }

    @Override
    public String getSupport_email() {
        return "getSupport_email";
    }
    // In a multi-tenant environment this data describes the tenant / customer.
    @Override
    public String getService_owner_id() {
        return "public String getService_owner_id";
    }

    @Override
    public String getService_owner_owner_name() {
        return "Svensson";
    }

    @Override
    public String getService_owner_description() {
        return "Service Owner Descript";
    }

    @Override
    public String getService_owner_support_email() {
        return "owner email";
    }
    // This represents the service provider that hosts a product.
    // If this is self hosted, it is reasonable that these values
    // are the same as the "owner" values above.
    @Override
    public String getService_provider_id() {
        return "Service provider id";
    }

    @Override
    public String getService_provider_provider_name() {
        return "getService_provider_provider_name";
    }

    @Override
    public String getService_provider_description() {
        return "getService_provider_description";
    }

    @Override
    public String getService_provider_support_email() {
        return "getService_provider_support_email";
    }

    // This section is general information about the software product
    @Override
    public String getProduct_family_product_code() {
        return "getProduct_family_product_code";
    }

    @Override
    public String getProduct_family_vendor_code() {
        return "getProduct_family_vendor_code";
    }

    @Override
    public String getProduct_family_vendor_name() {
        return "getProduct_family_vendor_name";
    }

    @Override
    public String getProduct_family_vendor_description() {
        return "getProduct_family_vendor_description";
    }

    @Override
    public String getProduct_family_vendor_website() {
        return "getProduct_family_vendor_website";
    }

    @Override
    public String getProduct_family_vendor_contact() {
        return "getProduct_family_vendor_contact";
    }

    // This is about one particular version of a product
    @Override
    public String getProduct_info_product_name() {
        return "getProduct_info_product_name";
    }

    @Override
    public String getProduct_info_product_version() {
        return "getProduct_info_product_version";
    }

    @Override
    public String getProduct_info_product_description() {
        return "getProduct_info_product_description";
    }
}
