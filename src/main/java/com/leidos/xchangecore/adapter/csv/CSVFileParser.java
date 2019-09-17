package com.leidos.xchangecore.adapter.csv;

import au.com.bytecode.opencsv.CSVReader;
import com.leidos.xchangecore.adapter.dao.MappedRecordDao;
import com.leidos.xchangecore.adapter.model.Configuration;
import com.leidos.xchangecore.adapter.model.MappedRecord;
import com.leidos.xchangecore.adapter.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class CSVFileParser {

    private static final double Pi = 3.14159;

    private static final double Radius = 6378137.0;

    private static Logger logger = LoggerFactory.getLogger(CSVFileParser.class);
    private static MappedRecordDao mappedRecordDao;

    private static String PatternPrefix = "(?i:.*";
    private static String PatternPostfix = ".*)";
    private final Map<String, MappedRecord> newRecords = new HashMap<String, MappedRecord>();
    private final Map<String, MappedRecord> updateRecords = new HashMap<String, MappedRecord>();
    private final Map<String, MappedRecord> deleteRecords = new HashMap<String, MappedRecord>();
    private final Map<String, MappedRecord> notMatchedRecords = new HashMap<String, MappedRecord>();
    private String errorList = "";

    public CSVFileParser() {

        super();
    }

    /*
     * This method will read in the csv file and concatenate the base file if
     * existed Then pass it with the configuration map
     */
    public CSVFileParser(File file, InputStream baseInputStream, Configuration configuration) throws Throwable {

        super();

        MappingHeaderColumnNameTranslateMappingStrategy strategy = new MappingHeaderColumnNameTranslateMappingStrategy();
        strategy.setType(MappedRecord.class);
        strategy.setColumnMapping(configuration.toMap());

        // merge files if necessary
        File csvFile = null;
        if (baseInputStream != null) {
            csvFile = mergeFiles(baseInputStream, file);
        } else {
            csvFile = file;
        }

        // verify the configuration first
        validateConfiguration(configuration, strategy, new XCCSVReader(new FileReader(csvFile)));
        // logger.debug("Configuration:\n" + configMap);

        MappingCsvToBean bean = new MappingCsvToBean(configuration);

        List<MappedRecord> parsedRecords = bean.parse(strategy, new XCCSVReader(new FileReader(csvFile)));
        Date currentDate = new Date();

        Set<String> indexSet = new HashSet<String>();
        List<MappedRecord> records = new ArrayList<MappedRecord>();
        for (MappedRecord record : parsedRecords) {
            if (indexSet.contains(record.getIndex())) {
                errorList += new String(
                        "Duplicate Index: " + record.getIndex() + ", Context: " + record.getContent() + "\n");
                continue;
            }

            indexSet.add(record.getIndex());

            // if the category.prefix specified, prefix it
            if (configuration.getCategoryPrefix() != null && configuration.getCategoryPrefix().length() > 0) {
                if (record.getCategory().equalsIgnoreCase("N/A") == false) {
                    record.setCategory(configuration.getCategoryPrefix() + record.getCategory());
                }
            }

            // if the category.suffix specified, suffix it
            if (configuration.getCategorySuffix() != null && configuration.getCategorySuffix().length() > 0) {
                if (record.getCategory().equalsIgnoreCase("N/A") == false) {
                    record.setCategory(record.getCategory() + configuration.getCategorySuffix());
                }
            }

            // for the category, we will override with category.fixed if existed
            if (configuration.getCategoryFixed() != null && configuration.getCategoryFixed().length() > 0) {
                record.setCategory(configuration.getCategoryFixed());
            }

            // if the title.prefix specified, prefix it
            if (configuration.getTitlePrefix() != null && configuration.getTitlePrefix().length() > 0) {
                if (record.getTitle().equalsIgnoreCase("N/A") == false) {
                    record.setTitle(configuration.getTitlePrefix() + record.getTitle());
                }
            }

            // if the title.prefix.column defined and use it
            if (configuration.getTitlePrefixColumn() != null && configuration.getTitlePrefixColumn().length() > 0) {
                if (record.getTitle().equalsIgnoreCase("N/A") == false) {
                    String titlePrefixColumn = configuration.getTitlePrefixColumn();
                    String[] tokens = configuration.getTitlePrefixColumn().split(" ");
                    String titlePrefix = getValue(tokens[0], record);
                    titlePrefix = titlePrefixColumn.replaceAll(tokens[0], titlePrefix);
                    record.setTitle(titlePrefix + record.getTitle());
                }
            }

            // if the title.suffix specified, suffix it
            if (configuration.getTitleSuffix() != null && configuration.getTitleSuffix().length() > 0) {
                if (record.getTitle().equalsIgnoreCase("N/A") == false) {
                    record.setTitle(record.getTitle() + configuration.getTitleSuffix());
                }
            }

            // if the title.suffix.column specified, suffix it
            if (configuration.getTitleSuffixColumn() != null && configuration.getTitleSuffixColumn().length() > 0) {
                if (record.getTitle().equalsIgnoreCase("N/A") == false) {
                    String titleSuffixColumn = configuration.getTitleSuffixColumn();
                    String[] tokens = configuration.getTitleSuffixColumn().split(" ");
                    String titleSuffix = getValue(tokens[tokens.length - 1], record);
                    titleSuffix = titleSuffixColumn.replaceAll(tokens[tokens.length - 1], titleSuffix);
                    record.setTitle(record.getTitle() + titleSuffix);
                }
            }

            // if the title.suffix specified, suffix it
            if (configuration.getStatusSuffix() != null && configuration.getStatusSuffix().length() > 0) {
                if (record.getStatus().equalsIgnoreCase("N/A") == false) {
                    record.setStatus(record.getStatus() + configuration.getStatusSuffix());
                }
            }

            record.setCreator(configuration.getId());
            record.setLastUpdated(currentDate);
            record.setCoreUri(configuration.getUri());
            logger.debug("record: " + record.toString());
            records.add(record);
        }

        if (indexSet.isEmpty() == false) {
            indexSet.clear();
        }

        parseRecords(records, configuration);

        if (baseInputStream != null) {
            csvFile.delete();
        }
    }

    private String getValue(String columnName, MappedRecord record) {

        if (columnName.equalsIgnoreCase(Configuration.FN_Status)) {
            return record.getStatus();
        } else if (columnName.equalsIgnoreCase(Configuration.FN_Category)) {
            return record.getCategory();
        } else if (columnName.equalsIgnoreCase(Configuration.FN_Index)) {
            return record.getIndex();
        } else if (columnName.equalsIgnoreCase(Configuration.FN_Latitude)) {
            return record.getLatitude();
        } else if (columnName.equalsIgnoreCase(Configuration.FN_Longitude)) {
            return record.getLongitude();
        } else {
            return columnName;
        }
    }

    public static MappedRecordDao getMappedRecordDao() {

        return mappedRecordDao;
    }

    public static void setMappedRecordDao(MappedRecordDao mappedRecordDao) {

        CSVFileParser.mappedRecordDao = mappedRecordDao;
    }

    public String getErrorList() {

        return errorList;
    }

    private Double[][] calculateBoundingBox(Map<String, MappedRecord> filterRecordSet, double distance) {

        Collection<MappedRecord> records = filterRecordSet.values();
        double south = 0.0;
        double north = 0.0;
        double west = 0.0;
        double east = 0.0;
        for (MappedRecord r : records) {
            double lat = Double.parseDouble(r.getLatitude());
            north = lat > 0 ? lat > north ? lat : north : lat < north ? lat : north;
            south = lat > 0 ? lat < south ? lat : south : lat > south ? lat : south;
            double lon = Double.parseDouble(r.getLongitude());
            west = lon > 0 ? lon < west ? lon : west : lon > west ? west : lon;
            east = lon > 0 ? lon > east ? lon : east : lon < east ? east : lon;
            if (south == 0) {
                south = north;
            }
            if (north == 0) {
                north = south;
            }
            if (east == 0) {
                east = west;
            }
            if (west == 0) {
                west = east;
            }
        }
        /*
         * Earth’s radius, sphere R=6378137 offsets in meters dn = 100 de = 100
         * Coordinate offsets in radians dLat = dn/R dLon =de/(R*Cos(Pi*lat/180))
         * OffsetPosition, decimal degrees latO = lat + dLat * 180/Pi lonO = lon + dLon
         * * 180/Pi
         */
        double d = distance * 1000.0;
        double deltaLat = d / Radius * 180 / Pi;
        north += deltaLat * (north > 0 ? 1 : -1);
        south -= deltaLat * (south > 0 ? 1 : -1);
        double northDelta = d / (Radius * Math.cos(Pi * north / 180.0)) * 180.0 / Pi;
        double northWestLon = west - northDelta;
        double northEastLon = east + northDelta;
        double southDelta = d / (Radius * Math.cos(Pi * south / 180.0)) * 180.0 / Pi;
        double southWestLon = west - southDelta;
        double southEastLon = east + southDelta;
        Double[][] boundingBox = new Double[5][2];
        boundingBox[0][0] = northWestLon;
        boundingBox[0][1] = north;
        boundingBox[1][0] = northEastLon;
        boundingBox[1][1] = north;
        boundingBox[2][0] = southEastLon;
        boundingBox[2][1] = south;
        boundingBox[3][0] = southWestLon;
        boundingBox[3][1] = south;
        boundingBox[4][0] = northWestLon;
        boundingBox[4][1] = north;

        return boundingBox;
    }

    private String getCombinedLine(String[] indexHeaders, String[] baseHeaders, int index) {

        // write the header first
        StringBuffer sb = new StringBuffer();
        for (String indexHeader : indexHeaders) {
            sb.append(indexHeader);
            sb.append(",");
        }
        for (int i = 0; i < baseHeaders.length; i++) {
            if (i == index) {
                continue;
            }
            sb.append(baseHeaders[i]);
            sb.append(",");
        }
        String header = sb.toString();
        header = header.substring(0, header.lastIndexOf(","));
        return header + "\n";
    }

    public MappedRecord[] getDeleteRecords() {

        if (deleteRecords.isEmpty()) {
            return null;
        } else {
            return deleteRecords.values().toArray(new MappedRecord[deleteRecords.values().size()]);
        }
    }

    public MappedRecord[] getNewRecords() {

        if (newRecords.isEmpty()) {
            return null;
        } else {
            return newRecords.values().toArray(new MappedRecord[newRecords.values().size()]);
        }
    }

    public MappedRecord[] getUpdateRecords() {

        if (updateRecords.isEmpty()) {
            return null;
        } else {
            return updateRecords.values().toArray(new MappedRecord[updateRecords.values().size()]);
        }
    }

    private File mergeFiles(InputStream is, File f) {

        CSVReader baseReader = new CSVReader(new InputStreamReader(is));
        CSVReader indexedReader = null;
        try {
            String[] headers = baseReader.readNext();
            String[] baseHeaders = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                baseHeaders[i] = headers[i].trim().toLowerCase();
            }
            indexedReader = new CSVReader(new FileReader(f));
            headers = indexedReader.readNext();
            String[] indexHeaders = new String[headers.length];
            for (int i = 0; i < headers.length; i++) {
                indexHeaders[i] = headers[i].trim().toLowerCase();
            }

            int[] columnNumbers = whichColumn(baseHeaders, indexHeaders);
            HashMap<String, String[]> indexMap = new HashMap<String, String[]>();
            // read in the target file
            String[] columns = null;
            while ((columns = indexedReader.readNext()) != null) {
                indexMap.put(columns[columnNumbers[0]], columns);
            }
            indexedReader.close();
            logger.debug("index file contains " + indexMap.size() + " records");

            File temp = File.createTempFile(f.getName(), ".tmp");
            BufferedWriter writer = new BufferedWriter(new FileWriter(temp));

            writer.write(getCombinedLine(indexHeaders, baseHeaders, columnNumbers[1]));

            // merge with base csv file
            while ((columns = baseReader.readNext()) != null) {
                String key = columns[columnNumbers[1]];
                if (indexMap.containsKey(key)) {
                    // write the merged line
                    // logger.debug("index file contain [" + key + "]");
                    writer.write(getCombinedLine(indexMap.get(key), columns, columnNumbers[1]));
                }
            }
            baseReader.close();
            writer.flush();
            writer.close();
            return temp;
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (baseReader != null) {
                    baseReader.close();
                }
                if (indexedReader != null) {
                    indexedReader.close();
                }
            } catch (Throwable e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }

    private void parseRecords(List<MappedRecord> records, Configuration config) {

        // reset the update and delete set
        newRecords.clear();
        updateRecords.clear();
        deleteRecords.clear();

        logger.debug("total records: " + records.size());

        boolean negativeExpression = config.getFilterText().startsWith("!");
        String filterText = negativeExpression ? config.getFilterText().substring(1) : config.getFilterText();
        String pattern = PatternPrefix + filterText + PatternPostfix;
        logger.debug("Filter Pattern: " + pattern);

        // find the matched filter text records
        for (MappedRecord r : records) {
            boolean isMatched = r.getFilter().matches(pattern);
            if (isMatched && negativeExpression == false || isMatched == false && negativeExpression == true) {
                logger.debug("new record: key: " + r.getIndex());
                newRecords.put(r.getIndex(), r);
            } else {
                logger.debug("not-matched record: key: " + r.getIndex());
                notMatchedRecords.put(r.getIndex(), r);
            }
        }
        logger.debug("matched records# " + newRecords.size() + " not-matched record# " + notMatchedRecords.size());
        Set<MappedRecord> distanceSet = new HashSet<MappedRecord>();
        if (config.getDistance().length() > 0 && newRecords.size() > 1) {
            Double[][] boundingBox = calculateBoundingBox(newRecords, Double.parseDouble(config.getDistance()));
            Collection<MappedRecord> newRecordSet = newRecords.values();
            for (MappedRecord r : newRecordSet) {
                if (r.getFilter().equalsIgnoreCase(config.getDistanceFilterText())) {
                    if (Util.insideBoundingBox(boundingBox, r.getLatitude(), r.getLongitude())) {
                        distanceSet.add(r);
                    }
                }
            }
            logger.debug("within the BoundingBox: " + distanceSet.size() + " records found");
            for (MappedRecord r : distanceSet) {
                logger.debug("new record: key: " + r.getIndex());
                newRecords.put(r.getIndex(), r);
            }
        }
        distanceSet.clear();

        // get the existed records for this creator and the core, for example:
        // target and spotonresponse'core
        List<MappedRecord> existedRecordList = getMappedRecordDao().findByCreator(config.getId(), config.getUri());
        logger.debug("[" + config.getId() + "]" + " @ [" + config.getUri() + "]");
        if (existedRecordList.size() > 0) {
            // if the existed record contains the IGID, then we assume it's been
            // saved in XchangeCore already
            Map<String, MappedRecord> inCoreSet = new HashMap<String, MappedRecord>();
            for (MappedRecord r : existedRecordList) {
                inCoreSet.put(r.getIndex(), r);
            }
            logger.debug("records in Core: " + inCoreSet.size());

            // if the in-core record is part of the new incident, we will
            // perform an update of it
            // if the in-core recrod is not part of the new incident, we will
            // delete it from XchangeCore
            Set<String> inCoreKeySet = inCoreSet.keySet();
            for (String key : inCoreKeySet) {
                logger.debug("in-core: key: " + key);
                if (newRecords.containsKey(key)) {
                    MappedRecord record = newRecords.remove(key);
                    logger.debug("in-core:\n" + inCoreSet.get(key).getContent());
                    logger.debug("new record:\n" + record.getContent());
                    if (inCoreSet.get(key).getContent().equalsIgnoreCase(record.getContent()) == false) {
                        record.setWorkProductID(inCoreSet.get(key).getWorkProductID());
                        logger.debug("for update: WPID: " + record.getWorkProductID());
                        // updateRecords.put(key, record);
                        // for NoSQL instead of put the recored into UpdateRecords
                        // we will put old one into deleteRecords and new one into newRecords
                        newRecords.put(key, record);
                        deleteRecords.put(key, inCoreSet.get(key));
                    }
                } else {
                    // logger.debug("IGID: " + key + " existed ... Auto.Close: "
                    // + (config.isAutoClose() ? "true" : "false"));
                    if (config.isAutoClose() == true) {
                        deleteRecords.put(key, inCoreSet.get(key));
                    } else {
                        MappedRecord record = notMatchedRecords.remove(key);
                        if (record != null) {
                            logger.debug("Auto.Close=FALSE key: " + key + " in-core but not in the filter record");
                            deleteRecords.put(key, inCoreSet.get(key));
                        } else {
                            logger.debug("Auto.Close=FALSE key: " + key + " not in current csv upload. No Action "
                                    + "Taken");
                        }
                    }
                }
            }
            // clean up the inCoreSet
            inCoreSet.clear();
        }
        logger.debug("records need to be created: " + newRecords.size());
        logger.debug("records need to be updated: " + updateRecords.size());
        logger.debug("records need to be deleted: " + deleteRecords.size());
    }

    private void validateConfiguration(Configuration csvConfiguration,
            MappingHeaderColumnNameTranslateMappingStrategy strategy, CSVReader csvReader) throws Throwable {

        strategy.captureHeader(csvReader);

        for (String columnName : Configuration.DefinedColumnNames) {
            String column = csvConfiguration.getFieldValue(columnName);
            // if the attribute is Description
            if (columnName.equals(Configuration.FN_Description)) {
                // if the full.description is set then collect all the fields to build the
                // description
                if (csvConfiguration.isFullDescription()) {
                    StringBuffer sb = new StringBuffer();
                    for (String cName : strategy.getHeaders()) {
                        sb.append(cName);
                        sb.append(".");
                    }
                    String columnNames = sb.toString();
                    columnNames = columnNames.substring(0, columnNames.length() - 1);
                    csvConfiguration.setDescription(columnNames);
                    continue;
                }
            }

            if (column == null) {
                logger.warn("Undefined Attribute: " + columnName + " N/A might be used");
                continue;
            }
            // logger.debug(columnName + ": " + column);
            String[] columns = column.split("\\.", -1);
            for (String c : columns) {
                boolean found = false;
                for (String h : strategy.getHeaders()) {
                    if (c.equalsIgnoreCase(h.trim())) {
                        found = true;
                        break;
                    }
                }
                // if the column is not specified in configuration file, it's
                // valid
                if (!found) {
                    throw new Exception("Attribute: " + column + ", header doesn't contain [" + c + "]");
                }
            }
        }
    }

    private int[] whichColumn(String[] baseHeaders, String[] indexHeaders) {

        for (int i = 0; i < indexHeaders.length; i++) {
            for (int j = 0; j < baseHeaders.length; j++) {
                if (indexHeaders[i].equalsIgnoreCase(baseHeaders[j])) {
                    return new int[] { i, j };
                }
            }
        }
        return null;
    }
}
