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
    public static final String FN_Category = "category";
    public static final String FN_CategoryPrefix = "category.prefix";
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
    public static final String FN_URLHost = "url.host";
    public static final String FN_Username = "url.username";
    public static final String FN_Password = "url.password";
    public static final String FN_RedirectUrl = "url.redirectUrl";
    public static final String urlPostfix = "/core/ws/services";

    public static final String[] DefinedColumnNames = new String[]{
        FN_Title,
        FN_Category,
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
    private String titlePrefix;
    private String category;
    private String filter;
    private String filterText;
    private String distance = "";
    private String distanceFilterText = "";
    private String latitude;
    private String longitude;
    private String categoryPrefix;
    private String categoryFixed;
    private String description; //  = "title.category";
    private String index; //  = "title.category.latitude.longitude";
    private boolean autoClose = true;
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

    public String getDescription() {

        return this.description;
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
        } else if (keyAndValue[0].equalsIgnoreCase(FN_CategoryFixed)) {
            this.setCategoryFixed(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_Distance)) {
            this.setDistance(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_DistanceFilterText)) {
            this.setDistanceFilterText(keyAndValue[1]);
        } else if (keyAndValue[0].equalsIgnoreCase(FN_AutoClose)) {
            this.setAutoClose(keyAndValue[1]);
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
}
