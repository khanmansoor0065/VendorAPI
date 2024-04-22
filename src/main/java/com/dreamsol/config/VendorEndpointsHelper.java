package com.dreamsol.config;

import com.dreamsol.entities.EndpointMappings;
import com.dreamsol.repositories.EndPointsMappingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class VendorEndpointsHelper {

    @Autowired
    private EndPointsMappingRepo endPointsMappingRepo;

    private static final Map<String, String> vendorEndpoints = new HashMap<>();
    private static final String[] endpointKeys = {
            "ADD_VENDOR",
            "UPDATE_VENDOR",
            "DELETE_VENDOR",
            "GET_ALL_VENDORS",
            "GET_SINGLE_VENDOR_BY_ID",
            "DOWNLOAD_FILE",
            "VALIDATE_EXCEL_LIST",
            "EXCEL_DOWNLOAD",
            "GET_DETAILS_BY_PRODUCT",
            "POST_BULK_API",
            "SAVE_EXCEL_DATA",
            "EXCEL_FORMAT_DOWNLOAD"
    };
    private static final String[] endpointLinks = {
            "/vendor/add",
            "/vendor/update/",
            "/vendor/delete/",
            "/vendor/list",
            "/vendor/get-ById/",
            "/vendor/download/",
            "/vendor/validate-excel-list",
            "/vendor/excel-download",
            "/vendor/product/",
            "/vendor/bulkData",
            "/vendor/save-excel-data",
            "/vendor/excel-format-download"
    };

    static {
        for (int i = 0; i < endpointKeys.length; i++) {
            vendorEndpoints.put(endpointKeys[i], endpointLinks[i]);
        }
    }

    public Map<String, String> getVendorEndpoints() {
        return vendorEndpoints;
    }
}
