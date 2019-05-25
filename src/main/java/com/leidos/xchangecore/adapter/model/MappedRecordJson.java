package com.leidos.xchangecore.adapter.model;

import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;

public class MappedRecordJson extends JSONObject {

    /*
     * extra attributes for dynamoDB
        Source -> {The name of your program}
        SourceHost -> {Where your program is running}
        SourceURL -> {URL to view/manage source (if applicable)}
        SourceContact -> {Name of person or group to contact about source data if needed (if applicable)}
        SourceEmail -> {Email of SourceContact (if applicable)}
     */
    private static final String S_Title = "title";
    private static final String S_MD5HASH = "md5hash";
    private static final String S_Status = "status";

    private static final String S_SourceHost = "Adapter Host";
    private static final String S_SourceLocation = "Adapter Location";
    private static final String S_SourceName = "Adapter Name";
    private static final String S_SourceType = "Adapter Type";
    private static final String S_SourceURL = "Data Source URL";
    private static final String S_SourceContact = "Adapter Contact";
    private static final String S_SourceEmail = "Adapter Contact Email";


    private static final String AdapterName = "xcadapter";
    private static final String AdapterType = "Java";
    private static final String DataSourceURL = "CSV Upload";


    private static final String[] removeEntries = {
            "content",
            "coreUri",
            "otherColumns",
            "description"
    };

    public MappedRecordJson(MappedRecord record) {

        super(new GsonBuilder().setPrettyPrinting().create().toJson(record));

        setWhere(record.getLatitude(), record.getLongitude());
        this.put(S_Title, record.getTitle());
        this.put(S_Status, record.getStatus());
        this.put(S_MD5HASH, MappedRecord.GetHash(record.getIndex().getBytes()));
        this.put(S_SourceHost, getHostname());
        this.put(S_SourceLocation, AdapterName);
        this.put(S_SourceURL, DataSourceURL);

        this.put(S_SourceName, AdapterName);
        this.put(S_SourceType, AdapterType);


        // Add otherFields
        Iterator<String> keys = record.getOtherColumns().keys();
        while (keys.hasNext()) {
            String key = keys.next();
            this.put(key, record.getOtherColumns().get(key));
        }
        // this.put(S_MD5HASH, getHash(record.getContent().getBytes()));
        clearUp();
    }

    private String getHostname() {
        String hostname = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            hostname = address.getHostName();
        } catch (Exception e) {
            // log error
        }
        return hostname;
    }
    private void clearUp() {
        for (String key : removeEntries) {
            this.remove(key);
        }
    }

    private void setWhere(String lat, String lon) {

        JSONObject where = new JSONObject();
        JSONObject point = new JSONObject();
        point.put("pos", lat + " " + lon);
        where.put("Point", point);
        this.put("where", where);
    }

    public String getTitle() {
        return (String) this.get(S_Title);
    }

    public String getStatus() {
        return (String) this.get(S_Status);
    }

    public String getPrimaryKey() {

        return (String) this.get(S_MD5HASH);
    }

    public Map.Entry getMapEntry() {

        return new AbstractMap.SimpleImmutableEntry(getTitle(), getPrimaryKey());
    }
}

