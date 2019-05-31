package com.leidos.xchangecore.adapter.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Configuration implements Serializable {

    public static final String N_Configuration_Start = "configuration:start";
    public static final String N_Configuration_End = "configuration:end";
    public static final String FN_Latitude = "latitude";
    public static final String FN_Longitude = "longitude";
    public static final String FN_Title = "title";
    public static final String FN_TitlePrefix = "title.prefix";
    public static final String FN_TitlePrefixColumn = "title.prefix.column";
    public static final String FN_TitleSuffix = "title.suffix";
    public static final String FN_TitleSuffixColumn = "title.suffix.column";
    public static final String FN_Category = "category";
    public static final String FN_CategoryPrefix = "category.prefix";
    public static final String FN_CategorySuffix = "category.suffix";
    public static final String FN_CategoryFixed = "category.fixed";
    public static final String FN_FilterName = "filter";
    public static final String FN_FilterText = "filter.text";
    public static final String FN_Distance = "distance";
    public static final String FN_DistanceFilterText = "distance.filter.text";
    public static final String FN_Index = "index";
    public static final String FN_Description = "description";
    public static final String FN_MappingColumns = "mapping.columns";
    public static final String FN_FullDescription = "full.description";
    public static final String FN_AutoClose = "auto.close";
    public static final String FN_EnableXCore = "enable.xcore";
    public static final String FN_URLHost = "url.host";
    public static final String FN_Username = "url.username";
    public static final String FN_Password = "url.password";
    public static final String FN_RedirectUrl = "url.redirectUrl";
    public static final String urlPostfix = "/core/ws/services";
    public static final String FN_Status = "status";
    public static final String FN_StatusPrefix = "";
    public static final String FN_StatusSuffix = "";

    public static final String[] DefinedColumnNames = new String[]{
            FN_Title,
            FN_Category,
            FN_Status,
            FN_Latitude,
            FN_Longitude,
            FN_FilterName,
            FN_Index,
            FN_Description,
    };
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    final HashMap<String, String> map = new HashMap<String, String>();
    final HashMap<String, String> duplicateMap = new HashMap<String, String>();
    private String id;
    private String title;
    private String titlePrefix = null;
    private String titlePrefixColumn = null;
    private String titleSuffix = null;
    private String titleSuffixColumn = null;
    private String category;
    private String status;
    private String statusPrefix = null;
    private String statusSuffix = null;
    private String filter;
    private String filterText;
    private String distance = "";
    private String distanceFilterText = "";
    private String latitude;
    private String longitude;
    private String categoryPrefix = null;
    private String categorySuffix = null;
    private String categoryFixed = null;
    private String description; //  = "title.category";
    private String index; //  = "title.category.latitude.longitude";
    private boolean autoClose = true;
    private boolean enableXCore = false;
    private boolean fullDescription = false;
    private String uri = "http://localhost";
    private String username = "xchangecore";
    private String password = "xchangecore";
    private String redirectUrl = "http://www.google.com";

    private static HashMap<String, String> mappingColumns = new HashMap<String, String>();

    public HashMap<String, String> getMappingColumns() {
        return mappingColumns;
    }

    public static String getMappingColumn(String column) {
        if (mappingColumns.size() == 0) {
            return column;
        }

        String mappedColumn = mappingColumns.get(column);
        return mappedColumn == null ? column : mappedColumn;
    }

    public void setMappingColumns(HashMap<String, String> mappingColumns) {
        this.mappingColumns = mappingColumns;
    }

    public String getCategory() {

        return this.category;
    }

    public void setCategory(String category) {

        this.category = category;
    }

    public String getCategoryFixed() {

        return this.categoryFixed;
    }

    public void setCategoryFixed(String categoryFixed) {

        this.categoryFixed = categoryFixed;
    }

    public String getCategoryPrefix() {

        return this.categoryPrefix;
    }

    public void setCategoryPrefix(String categoryPrefix) {

        this.categoryPrefix = categoryPrefix;
    }

    public String getCategorySuffix() {
        return categorySuffix;
    }

    public void setCategorySuffix(String categorySuffix) {
        this.categorySuffix = categorySuffix;
    }

    public String getDescription() {

        return this.description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusPrefix() {
        return statusPrefix;
    }

    public void setStatusPrefix(String statusPrefix) {
        this.statusPrefix = statusPrefix;
    }

    public String getStatusSuffix() {
        return statusSuffix;
    }

    public void setStatusSuffix(String statusSuffix) {
        this.statusSuffix = statusSuffix;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getDistance() {

        return this.distance;
    }

    public void setDistance(String distance) {

        this.distance = distance;
    }

    public String getDistanceFilterText() {

        return this.distanceFilterText;
    }

    public void setDistanceFilterText(String distanceFilterText) {

        this.distanceFilterText = distanceFilterText;
    }

    public String getFilter() {

        return this.filter;
    }

    public void setFilter(String filter) {

        this.filter = filter;
    }

    public String getFilterText() {

        return this.filterText;
    }

    public void setFilterText(String filterText) {

        this.filterText = filterText;
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getIndex() {

        return this.index;
    }

    public void setIndex(String index) {

        this.index = index;
    }

    public String getLatitude() {

        return this.latitude;
    }

    public void setLatitude(String latitude) {

        this.latitude = latitude;
    }

    public String getLongitude() {

        return this.longitude;
    }

    public void setLongitude(String longitude) {

        this.longitude = longitude;
    }

    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getRedirectUrl() {

        return this.redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {

        this.redirectUrl = redirectUrl;
    }

    public String getTitle() {

        return this.title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getTitlePrefix() {

        return this.titlePrefix;
    }

    public void setTitlePrefix(String titlePrefix) {

        this.titlePrefix = titlePrefix;
    }

    public String getTitleSuffix() {
        return titleSuffix;
    }

    public void setTitleSuffix(String titleSuffix) {
        this.titleSuffix = titleSuffix;
    }

    public String getUri() {

        return this.uri;
    }

    public void setUri(String uri) {

        this.uri = uri;
    }

    public String getUsername() {

        return this.username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public boolean isFullDescription() {

        return this.fullDescription;
    }

    public void setFullDescription(String fullDescription) {

        if (fullDescription.toLowerCase().trim().equals("true")) {
            this.fullDescription = true;
        }
    }

    public String getFieldValue(String columnName) {

        try {
            return (String) this.getClass().getDeclaredField(columnName).get(this);
        }
        catch (final Throwable e) {
            if (e instanceof NoSuchFieldException) {
                return "";
            } else {
                logger.error("getFieldValue: " + columnName + ": " + e.getMessage());
                return null;
            }
        }
    }

    public String getValue(String key) {

        if (key.equals(FN_Category)) {
            return this.getCategory();
        } else if (key.equalsIgnoreCase(FN_Description)) {
            return this.getDescription();
        } else if (key.equalsIgnoreCase(FN_FilterName)) {
            return this.getFilter();
        } else if (key.equalsIgnoreCase(FN_Latitude)) {
            return this.getLatitude();
        } else if (key.equalsIgnoreCase(FN_Longitude)) {
            return this.getLongitude();
        } else if (key.equalsIgnoreCase(FN_Title)) {
            return this.getTitle();
        } else if (key.equalsIgnoreCase(FN_Category)) {
            return this.getCategory();
        } else if (key.equalsIgnoreCase(FN_Status)) {
            return this.getStatus();
        } else if (key.equalsIgnoreCase(FN_Distance)) {
            return this.getDistance();
        } else if (key.equalsIgnoreCase(FN_DistanceFilterText)) {
            return this.getDistanceFilterText();
        } else if (key.equalsIgnoreCase(FN_Index)) {
            return this.getIndex();
        } else {
            return null;
        }
    }

    public boolean isEnableXCore() {
        return enableXCore;
    }

    public void setEnableXCore(String enableXCore) {

        if (enableXCore.equalsIgnoreCase("true") || enableXCore.equals("1")) {
            this.enableXCore = true;
        } else {
            this.enableXCore = false;
        }
    }

    public boolean isAutoClose() {

        return autoClose;
    }

    private void setAutoClose(String ac) {

        this.autoClose = ac.equalsIgnoreCase("true") == true || ac.equals("1") == true ? true : false;
    }

    public boolean isValid() {

        return this.latitude != null && this.longitude != null && this.filter != null && this.filterText != null ? true : false;
    }

    public void setKeyValue(final String[] keyAndValue) {

        // logger.debug("key/value: [" + keyAndValue[0] + "/" + keyAndValue[1] + "]");

        if (keyAndValue[0].equalsIgnoreCase(FN_Category)) {
            this.setCategory(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Title)) {
            this.setTitle(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_TitlePrefix)) {
            this.setTitlePrefix(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_TitlePrefixColumn)) {
            this.setTitlePrefixColumn(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_TitleSuffix)) {
            this.setTitleSuffix(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_TitleSuffixColumn)) {
            this.setTitleSuffixColumn(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Status)) {
            this.setStatus(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_StatusPrefix)) {
            this.setStatusPrefix(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_StatusSuffix)) {
            this.setStatusSuffix(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Latitude)) {
            this.setLatitude(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Longitude)) {
            this.setLongitude(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_FilterName)) {
            this.setFilter(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_FilterText)) {
            this.setFilterText(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Index)) {
            this.setIndex(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Description)) {
            this.setDescription(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_URLHost)) {
            this.setUri(keyAndValue[1] + urlPostfix);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Username)) {
            this.setUsername(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Password)) {
            this.setPassword(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_RedirectUrl)) {
            this.setRedirectUrl(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_CategoryPrefix)) {
            this.setCategoryPrefix(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_CategorySuffix)) {
            this.setCategorySuffix(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_CategoryFixed)) {
            this.setCategoryFixed(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Distance)) {
            this.setDistance(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_DistanceFilterText)) {
            this.setDistanceFilterText(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_AutoClose)) {
            this.setAutoClose(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_EnableXCore)) {
            this.setEnableXCore(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_MappingColumns)) {
            String[] pairs = keyAndValue[1].split("\\.", -1);
            for (String pair : pairs) {
                String[] tokens = pair.split(":", -1);
                if (tokens.length != 2) {
                    logger.error("[" + pair + "] is not valid key and value");
                    continue;
                }
                mappingColumns.put(tokens[0], tokens[1]);
            }
        } else if (keyAndValue[0].equalsIgnoreCase(FN_FullDescription)) {
            this.setFullDescription(keyAndValue[1]);
        } else {
            logger.warn("Invalid Key/Value: [" + keyAndValue[0] + "/" + keyAndValue[1] + "]");
        }
    }

    public Map<String, String> toMap() {

        if (this.getTitle() != null && this.getTitle().indexOf(".") == -1) {
            map.put(this.getTitle(), FN_Title);
        }
        if (this.getStatus() != null && this.getStatus().indexOf(".") == -1) {
            map.put(this.getStatus(), FN_Status);
        }
        String column = null;
        if (this.getCategory() != null && this.getCategory().indexOf(".") == -1) {

            column = map.get(this.getCategory());
            if (column != null) {
                duplicateMap.put(FN_Category, column);
            } else {
                map.put(this.getCategory(), FN_Category);
            }
        }
        if (this.getFilter() != null && this.getFilter().indexOf(".") == -1) {
            column = map.get(this.getFilter());
            if (column != null) {
                duplicateMap.put(FN_FilterName, column);
            } else {
                map.put(this.getFilter(), FN_FilterName);
            }
        }
        if (this.getIndex() != null && this.getIndex().indexOf(".") == -1) {
            column = map.get(this.getIndex());
            if (column != null) {
                duplicateMap.put(FN_Index, column);
            } else {
                map.put(this.getIndex(), FN_Index);
            }
        }
        if (this.getDescription() != null && this.getDescription().indexOf(".") == -1) {
            column = map.get(this.getDescription());
            if (column != null) {
                duplicateMap.put(FN_Description, column);
            } else {
                map.put(this.getDescription(), FN_Description);
            }
        }
        map.put(this.getLatitude(), FN_Latitude);
        map.put(this.getLongitude(), FN_Longitude);

        return map;
    }

    public String getDuplicateAttributeValue(String attributeName) {

        return duplicateMap.get(attributeName);
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();

        sb.append("Configuration:\n\t");
        sb.append(FN_Category);
        sb.append(":\t");
        sb.append(getCategory());
        sb.append("\n");

        sb.append(FN_Status);
        sb.append(":\t");
        sb.append(getStatus());
        sb.append("\n");

        sb.append("\t");
        sb.append(FN_Title);
        sb.append(":\t");
        sb.append(getTitle());
        sb.append("\n");

        sb.append("\t");
        sb.append(FN_FilterName);
        sb.append(":\t");
        sb.append(getFilter());
        sb.append("\n");

        sb.append("\t");
        sb.append(FN_Index);
        sb.append(":\t");
        sb.append(getIndex());
        sb.append("\n");

        sb.append("\t");
        sb.append(FN_Description);
        sb.append(":\t");
        sb.append(getDescription());
        sb.append("\n");

        return sb.toString();
    }

    // Based on the required attributes, return the missing attributes
    public String getMissingAttributes() {

        StringBuffer sb = new StringBuffer();
        if (this.getFilterText() == null) {
            sb.append("filter.text, ");
        }
        if (this.getFilter() == null) {
            sb.append("filter, ");
        }
        if (this.getIndex() == null) {
            sb.append("index, ");
        }
        if (this.getLatitude() == null) {
            sb.append("latitude, ");
        }
        if (this.getLongitude() == null) {
            sb.append("longitude, ");
        }
        String errorMessage = sb.toString();
        errorMessage = errorMessage.substring(0, errorMessage.lastIndexOf(", "));

        return "Missing Attribute: [ " + errorMessage + " ]";
    }

    public String getTitlePrefixColumn() {
        return titlePrefixColumn;
    }

    public void setTitlePrefixColumn(String titlePrefixColumn) {
        this.titlePrefixColumn = titlePrefixColumn;
    }

    public String getTitleSuffixColumn() {
        return titleSuffixColumn;
    }

    public void setTitleSuffixColumn(String titleSuffixColumn) {
        this.titleSuffixColumn = titleSuffixColumn;
    }
}
