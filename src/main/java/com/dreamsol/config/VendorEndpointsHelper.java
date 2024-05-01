package com.dreamsol.config;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class VendorEndpointsHelper {
    private static final Map<String, String> vendorEndpoints = new HashMap<>();
    static {
        // Vendor API
        vendorEndpoints.put("ACCESS_VENDOR_ADD", "/vendor/add");
        vendorEndpoints.put("ACCESS_VENDOR_UPDATE", "/vendor/update/*");
        vendorEndpoints.put("ACCESS_VENDOR_DELETE", "/vendor/delete/*");
        vendorEndpoints.put("ACCESS_VENDOR_GET_ALL", "/vendor/list");
        vendorEndpoints.put("ACCESS_VENDOR_GET_SINGLE_BY_ID", "/vendor/get-ById/*");
        vendorEndpoints.put("ACCESS_VENDOR_DOWNLOAD_FILE", "/vendor/download/*");
        vendorEndpoints.put("ACCESS_VENDOR_VALIDATE_EXCEL_LIST", "/vendor/validate-excel-list");
        vendorEndpoints.put("ACCESS_VENDOR_EXCEL_DOWNLOAD", "/vendor/excel-download");
        vendorEndpoints.put("ACCESS_VENDOR_POST_BULK_API", "/vendor/bulkData");
        vendorEndpoints.put("ACCESS_VENDOR_SAVE_EXCEL_DATA", "/vendor/save-excel-data");
        vendorEndpoints.put("ACCESS_VENDOR_EXCEL_FORMAT_DOWNLOAD", "/vendor/excel-format-download");
        // Vendor Type API
        vendorEndpoints.put("ACCESS_VENDOR_TYPE_ADD", "/vendor-type/add");
        vendorEndpoints.put("ACCESS_VENDOR_TYPE_UPDATE", "/vendor-type/update/*");
        vendorEndpoints.put("ACCESS_VENDOR_TYPE_DELETE", "/vendor-type/delete/*");
        vendorEndpoints.put("ACCESS_VENDOR_TYPE_GET_ALL", "/vendor-type/list");
        vendorEndpoints.put("ACCESS_VENDOR_TYPE_SAVE_EXCEL_DATA", "/vendor-type/save-excel-data");
        vendorEndpoints.put("ACCESS_VENDOR_TYPE_GET_SINGLE_BY_ID", "/vendor-type/get-ById/*");
    }

    public Map<String, String> getVendorEndpoints() {
        return vendorEndpoints;
    }
}
