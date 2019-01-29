package com.leidos.xchangecore.adapter.model;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;

@Entity
public class MappedRecord implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(4096)")
    private String title = "N/A";

    @Column(columnDefinition = "VARCHAR(4096)")
    private String category = "N/A";

    @Column(columnDefinition = "VARCHAR(65536)")
    private String content = "N/A";

    @Column(columnDefinition = "VARCHAR(65536)")
    private String description = "N/A";

    @Column(columnDefinition = "VARCHAR(4096)")
    private String index = "N/A";

    private String creator;
    private String filter = "N/A";
    private String igID = null;

    @Column(columnDefinition = "VARCHAR(4096)")
    private String workProductID = null;
    private String latitude;
    private String longitude;
    private String distance;
    private String distanceFilterText;
    private String coreUri = null;

    private Date lastUpdated;

    public static String GetHash(byte[] bytes) {

        MessageDigest md5hash = null;
        try {
            md5hash = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
            return null;
        }
        md5hash.update(bytes);
        byte[] digest = md5hash.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public String getCategory() {

        return this.category;
    }

    public void setCategory(String category) {

        this.category = category;
    }

    public String getContent() {

        return this.content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public String getCoreUri() {

        return coreUri;
    }

    public void setCoreUri(String uri) {

        this.coreUri = uri;
    }

    public String getCreator() {

        return this.creator;
    }

    public void setCreator(String creator) {

        this.creator = creator;
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

    public Integer getId() {

        return this.id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getIgID() {

        return this.igID;
    }

    public void setIgID(String igID) {

        this.igID = igID;
    }

    public String getIndex() {

        return this.index;
    }

    public void setIndex(String index) {

        this.index = index;
    }

    public String getKey() {

        return this.index + this.coreUri;
    }

    public Date getLastUpdated() {

        return this.lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {

        this.lastUpdated = lastUpdated;
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

    public String getTitle() {

        return this.title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getWorkProductID() {

        return this.workProductID;
    }

    public void setWorkProductID(String workProductID) {

        this.workProductID = workProductID;
    }

    @Override
    public String toString() {

        final StringBuffer sb = new StringBuffer();
        if (this.id != null) {
            sb.append("\n\tID: " + this.id);
        }
        sb.append("\n\tTitle: " + this.title);
        sb.append("\n\tCategory: " + this.category);
        sb.append("\n\tLat/Lon: " + this.latitude + "/" + this.longitude);
        sb.append("\n\tIndex Key: " + this.index);
        sb.append("\n\tFilter: " + this.filter);
        sb.append("\n\tDescription: " + this.description);
        sb.append("\n\tContent:  " + this.content);
        if (this.distance != null) {
            sb.append("\n\tDistance: " + this.distance);
        }
        if (this.distanceFilterText != null) {
            sb.append("\n\tDistance.Filter.Text: " + this.distanceFilterText);
        }
        if (this.workProductID != null) {
            sb.append("\n\tProductID: " + this.workProductID);
        }
        if (this.igID != null) {
            sb.append("\n\tIGID: " + this.igID);
        }
        if (this.creator != null) {
            sb.append("\n\tCreator: " + this.creator);
        }
        if (this.coreUri != null) {
            sb.append("\n\tCoreUri: " + this.coreUri);
        }
        sb.append("\n");
        return sb.toString();
    }
}
