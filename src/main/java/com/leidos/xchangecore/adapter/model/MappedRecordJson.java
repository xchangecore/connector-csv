package com.leidos.xchangecore.adapter.model;

import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.AbstractMap;
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
    private static final String S_SourceName = "xcadapter";
    private static final String S_Source = "Source";
    private static final String S_SourceHost = "SourceHost";
    private static final String S_SourceURL = "SourceURL";
    private static final String S_SourceContact = "SourceContact";
    private static final String S_SourceEmail = "SourceEmail";
    private static final String[] removeEntries = {
        "coreUri",
        "longitude",
        "latitude",
        "workProductID",
    };

    public MappedRecordJson(MappedRecord record) {

        super(new GsonBuilder().setPrettyPrinting().create().toJson(record));
        setWhere(record.getLatitude(), record.getLongitude());
        this.put(S_Title, record.getTitle());
        this.put(S_MD5HASH, MappedRecord.GetHash(record.getIndex().getBytes()));
        this.put(S_Source, S_SourceName);
        this.put(S_SourceHost, getHostname());
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

    public String getPrimaryKey() {

        return (String) this.get(S_MD5HASH);
    }

    public Map.Entry getMapEntry() {

        return new AbstractMap.SimpleImmutableEntry(getTitle(), getPrimaryKey());
    }
}

