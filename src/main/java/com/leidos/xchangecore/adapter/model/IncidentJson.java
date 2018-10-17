package com.leidos.xchangecore.adapter.model;

import com.google.gson.GsonBuilder;
import gov.niem.niem.niemCore.x20.IncidentType;
import org.json.JSONObject;
import org.json.XML;

public class IncidentJson extends JSONObject {

    public static int PRETTY_PRINT_INDENT_FACTOR = 4;

    public IncidentJson(String xmlText) {
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(xmlText);
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            System.out.println(jsonPrettyPrintString);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
