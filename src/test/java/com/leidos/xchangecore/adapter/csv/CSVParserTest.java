package com.leidos.xchangecore.adapter.csv;

import com.leidos.xchangecore.adapter.model.IncidentJson;
import com.leidos.xchangecore.adapter.model.MappedRecord;
import com.leidos.xchangecore.adapter.model.MappedRecordJson;
import com.leidos.xchangecore.adapter.util.Util;
import gov.niem.niem.niemCore.x20.IncidentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:contexts/applicationContext.xml" })
public class CSVParserTest {

    private void processingCSV(final String configFilename, final String cvsFilename, final String baseFilename) {

        System.err.println("\n>>>>>>>>>>\n\tConfiguration:\t" + configFilename + "\n\tCSV File:\t\t" + cvsFilename
                + "\n<<<<<<<<<<");
        try {
            // create the csv data file
            final File csvFile = new File(cvsFilename);

            // create the configuration file input stream
            final FileInputStream fis = new FileInputStream(new File(configFilename));

            // create the base file inputstream if the baseFilename is not null
            final FileInputStream baseIS = (baseFilename == null) ? null : new FileInputStream(new File(baseFilename));

            // create the configuration parser
            final ConfigFilePaser configFileParser = new ConfigFilePaser(configFilename, fis);

            // create the csv file parser
            final CSVFileParser csvFileParser = new CSVFileParser(csvFile, baseIS, configFileParser.getConfigMap());

            // retrieve the records from the parsed file
            MappedRecord[] records = csvFileParser.getNewRecords();
            if (records != null) {
                for (final MappedRecord record : records) {
                    System.out.println("NEW: " + record);
                    System.out.println("MappedRecordJson: " + new MappedRecordJson(record));
                    final IncidentType incident = Util.getIncidentDocument(record);
                    System.out.println("NEW Incident: " + incident);
                    System.out.println("IncidentJson: " + new IncidentJson(incident.xmlText()));
                }
            }
            records = csvFileParser.getUpdateRecords();
            if (records != null) {
                for (final MappedRecord record : records) {
                    System.out.println("UPDATE: " + record);
                    System.out.println("UPDATE Json: " + new MappedRecordJson(record));
                    final IncidentType incident = Util.getIncidentDocument(record);
                    System.out.println("UPDATE Incident: " + incident);
                }
            }
            records = csvFileParser.getDeleteRecords();
            if (records != null) {
                for (final MappedRecord record : records) {
                    System.out.println("DELTE: " + record);
                    System.out.println("DELTE Json: " + new MappedRecordJson(record));
                    final IncidentType incident = Util.getIncidentDocument(record);
                    System.out.println("DELETE Incident: " + incident);
                }
            }
            if (csvFileParser.getErrorList().length() > 0) {
                String[] eMessages = csvFileParser.getErrorList().split("\n", -1);
                for (String e : eMessages) {
                    if (e.length() > 0) {
                        System.err.println(e);
                    }
                }
            }
            System.err.println(
                    "=======================================================================================================================");
            return;
        } catch (Throwable e) {
            System.err.println(">>>>>>>>>>\nError: " + e.getMessage() + "\n<<<<<<<<<<");
            System.err.println(
                    "=======================================================================================================================");
            return;
        }
    }

    @Test
    public void testCSVUpload() throws Throwable {

        // test the empty entries
        processingCSV("src/test/resources/config/testDuplicateAttribute.config",
                "src/test/resources/data/test-empty.csv", null);

        // test duplicate index using the same column
        processingCSV("src/test/resources/config/testDuplicateIndex.config", "src/test/resources/data/test.csv", null);

        // test duplicate attributes using the same column
        processingCSV("src/test/resources/config/testDuplicateAttribute.config", "src/test/resources/data/test.csv",
                null);

        // test the missing required attributes
        processingCSV("src/test/resources/config/testMissingAttribute.config", "src/test/resources/data/test.csv",
                null);

        // Shaken
        processingCSV("src/main/webapp/config/shaken.config", "src/test/resources/data/shaken.csv", null);

        // CVS
        processingCSV("src/main/webapp/config/cvs.config", "src/test/resources/data/cvs.csv", null);

        // Macys
        processingCSV("src/main/webapp/config/macys.config", "src/test/resources/data/macys.csv", null);

        // Sears
        processingCSV("src/main/webapp/config/sears.config", "src/test/resources/data/sears.csv", null);

        // Dick's
        processingCSV("src/main/webapp/config/dicks.config", "src/test/resources/data/dicks.csv", null);

        // PGNE
        processingCSV("src/main/webapp/config/pgne.config", "src/test/resources/data/pgne.csv", null);

        // Walgreens
        // processingCSV("src/main/webapp/config/walgreen.config",
        // "src/test/resources/data/WalgreenStatus-org.csv",
        // "src/main/webapp/config/walgreen.csv");

        // processingCSV("src/main/webapp/config/walgreen.config",
        // "src/test/resources/data/a.Walgreen.csv",
        // "src/main/webapp/config/walgreen.csv");

        // Walmart
        processingCSV("src/main/webapp/config/walmart.config", "src/test/resources/data/walmart.csv", null);

        // CostCo
        processingCSV("src/main/webapp/config/costco.config", "src/test/resources/data/costco.csv", null);

        // Target
        processingCSV("src/main/webapp/config/target.config", "src/test/resources/data/target.csv", null);

        // Boyd
        processingCSV("src/main/webapp/config/boyd.config", "src/test/resources/data/boyd.csv", null);

        // Irwin
        processingCSV("src/main/webapp/config/irwin.config", "src/test/resources/data/irwin.csv", null);

        // Lowes
        processingCSV("src/main/webapp/config/lowes.config", "src/test/resources/data/lowes.csv", null);

    }

    @Test
    public void testString() {

        String key = "abc:def:";
        key = key.substring(0, key.lastIndexOf(":"));
        System.out.println("Key: " + key);

        key = "abc:def:kadfa dkerekjkkde  dde";
        final boolean found = key.matches("(?i:.*ada.*)");
        System.out.println("contains de: " + (found ? " found" : " not found"));

        key = " abc def ";
        System.out.println("[" + key + "] -> [" + key.trim() + "]");
    }
}
