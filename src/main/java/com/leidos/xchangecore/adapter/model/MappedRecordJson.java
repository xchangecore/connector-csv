package com.leidos.xchangecore.adapter.model;

import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.AbstractMap;
import java.util.Map;

public class MappedRecordJson extends JSONObject {

    private static final String S_Title = "title";
    private static final String S_MD5HASH = "md5hash";
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
        this.put(S_MD5HASH, getHash(record.getIndex().getBytes()));
        // this.put(S_MD5HASH, getHash(record.getContent().getBytes()));
        clearUp();
    }

    private void clearUp() {
        for (String key : removeEntries) {
            this.remove(key);
        }
    }

    private String getHash(byte[] byes) {

        MessageDigest md5hash = null;
        try {
            md5hash = MessageDigest.getInstance("MD5");
        }
        catch (Exception e) {
            return null;
        }
        md5hash.update(byes);
        byte[] digest = md5hash.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
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

