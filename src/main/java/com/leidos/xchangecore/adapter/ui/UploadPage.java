package com.leidos.xchangecore.adapter.ui;

import com.leidos.xchangecore.adapter.XchangeCoreAdapter;
import com.leidos.xchangecore.adapter.csv.CSVFileParser;
import com.leidos.xchangecore.adapter.csv.ConfigFilePaser;
import com.leidos.xchangecore.adapter.model.Configuration;
import com.leidos.xchangecore.adapter.model.MappedRecord;
import com.leidos.xchangecore.adapter.webclient.WebServiceClient;
import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SuppressWarnings("serial")
public class UploadPage extends WebPage {

    private static final Logger logger = LoggerFactory.getLogger(UploadPage.class);
    private static final String ConfigParameterName = "config";
    private static final String ConfigFilePostfix = ".config";
    private static final String BaseFilePostfix = ".csv";
    private static String redirectUrl = "http://www.google.com";
    private int numOfCreation = 0;
    private int numOfUpdate = 0;
    private int numOfDeletion = 0;
    // private final InputStream configInputStream = null;
    // private final InputStream baseInputStream = null;
    private String baseFilename;
    private String configFilename;
    private String message = "";
    private List<Configuration> configurationList = null;

    /**
     * Constructor.
     *
     * @param parameters Page parameters
     */
    public UploadPage(PageParameters parameters) {

        // turn the version off
        setVersioned(false);

        numOfCreation = 0;
        numOfUpdate = 0;
        numOfDeletion = 0;

        getUploadFolder().removeFiles();
        // this.cleanUpFiles();

        // Add simple upload form, which is hooked up to its feedback panel by
        // virtue of that panel being nested in the form.
        FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
        simpleUploadForm.add(new UploadProgressBar("progress", simpleUploadForm, simpleUploadForm.fileUploadField));
        add(simpleUploadForm);

        final PropertyModel<String> messageModel = new PropertyModel<String>(this, "message");
        add(new Label("statusMessage", messageModel) {

            @Override
            public boolean isVisible() {

                return !messageModel.getObject().equals(message);
            }
        });

        FeedbackPanel uploadFeedback = new FeedbackPanel("feedback");
        uploadFeedback.setOutputMarkupId(true);
        add(uploadFeedback);

        /*
        final ModalWindow statusPage = createStatusPage(numOfCreation, numOfUpdate, numOfDeletion);
        this.add(statusPage);
        this.add(new AjaxLink<Void>("showStatus") {

        @Override
        public void onClick(AjaxRequestTarget target) {

        statusPage.show(target);
        }
        });
         */

        add(new AjaxLink<Void>("redirect") {

            @Override
            public void onClick(AjaxRequestTarget target) {

                logger.debug("redirect to " + redirectUrl);
                throw new RestartResponseAtInterceptPageException(new RedirectPage(redirectUrl));
            }
        });

        if (parameters.get(ConfigParameterName)==null) {
            error("No configuration defined: Usage: xcadapter?config=somename");
            return;
        }
        configFilename = parameters.get(ConfigParameterName) + ConfigFilePostfix;
        baseFilename = parameters.get(ConfigParameterName) + BaseFilePostfix;
        info("UploadPage: Configuration File: " + configFilename);
        logger.debug("UploadPage: configFilename: " + configFilename);

        if (getFileStream(configFilename)==null) {
            error("Configuration File: " + configFilename + " Not existed");
            return;
        }
        try {
            configurationList = new ConfigFilePaser(configFilename,
                                                    getFileStream(configFilename)).listOfConfiguration();
        }
        catch (Exception e) {
            error(e.getMessage());
        }
    }

    private InputStream getFileStream(String filename) {

        List<IResourceFinder> finders = getApplication().getResourceSettings().getResourceFinders();
        for (IResourceFinder finder : finders) {
            IResourceStream resource = finder.find(UrlResourceStream.class, "/config/" + filename);
            if (resource!=null && resource instanceof UrlResourceStream) {
                try {
                    return ((UrlResourceStream) resource).getInputStream();
                }
                catch (ResourceStreamNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Reference to listview for easy access.
     */
    // private final FileListView fileListView = null;
    public String getMessage() {

        return message;
    }

    // private ConfigFilePaser configFileParser = null;

    public void setMessage(String message) {

        this.message = message;
    }

    /*
    private ModalWindow createStatusPage(final int created, final int updated, final int deleted) {

        final ModalWindow modal = new ModalWindow("statusPage");

        modal.setCookieName("status window");

        modal.setResizable(false);
        modal.setInitialWidth(30);
        modal.setInitialHeight(15);
        modal.setWidthUnit("em");
        modal.setHeightUnit("em");

        modal.setCssClassName(ModalWindow.CSS_CLASS_GRAY);

        modal.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {

                return new StatusPage(modal, created, updated, deleted);
            }
        });

        modal.setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {

            @Override
            public boolean onCloseButtonClicked(AjaxRequestTarget target) {

                target.appendJavaScript("alert('You can\\'t close this modal window using close button."
                    + " Use the link inside the window instead.');");

                return false;
            }
        });

        return modal;

    }
     */

    private Folder getUploadFolder() {

        return ((XchangeCoreAdapter) Application.get()).getUploadFolder();
    }

    /**
     * Form for uploads.
     */
    private class FileUploadForm extends Form<Void> {

        FileUploadField fileUploadField;

        /**
         * Construct.
         *
         * @param name Component name
         */
        public FileUploadForm(String name) {

            super(name);

            // set this form to multipart mode (always needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            add(fileUploadField = new FileUploadField("fileInput"));

            // Set maximum size to 100K for demo purposes
            // setMaxSize(Bytes.kilobytes(100));
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        protected void onSubmit() {

            List<FileUpload> uploads = fileUploadField.getFileUploads();

            String errorMessage = null;

            if (uploads!=null) {

                try {
                    getUploadFolder().ensureExists();
                }
                catch (IOException ioe) {
                    errorMessage = "Upload Folder not existed and creation failed: " + ioe.getMessage();
                    logger.error(errorMessage);
                    UploadPage.this.error(errorMessage);
                    return;
                }

                boolean isRemoved = getUploadFolder().removeFiles();
                logger.debug(
                    "Removed all the files under xchangecore-uploads: " + (isRemoved ? " success" : " failure"));

                for (FileUpload upload : uploads) {
                    // Create a new file
                    File newFile = new File(getUploadFolder(), upload.getClientFileName());

                    info("Upload file: [" + upload.getClientFileName() + "] ...");
                    setMessage("Upload file: [" + upload.getClientFileName() + "] ...");

                    try {
                        newFile.createNewFile();
                        upload.writeTo(newFile);
                    }
                    catch (Exception e) {
                        throw new IllegalStateException("Unable to write file", e);
                    }

                    try {
                        for (Configuration configuration : configurationList) {
                            CSVFileParser csvFileParser = new CSVFileParser(newFile, getFileStream(baseFilename),
                                                                            configuration);

                            redirectUrl = configuration.getRedirectUrl();
                            WebServiceClient wsClient = new WebServiceClient(configuration.getUri(),
                                                                             configuration.getUsername(),
                                                                             configuration.getPassword());

                            numOfCreation = numOfUpdate = numOfDeletion = 0;

                            // get the new Incidents
                            MappedRecord[] records = csvFileParser.getNewRecords();

                            setMessage("......");
                            if (records!=null) {
                                numOfCreation = records.length;
                                info("Created: " + numOfCreation + " records");
                                for (MappedRecord r : records) {
                                    if (wsClient.createIncident(r)) {
                                        CSVFileParser.getMappedRecordDao().makePersistent(r);
                                    }
                                }
                            }

                            // update the incidents
                            MappedRecord[] updateRecordSet = csvFileParser.getUpdateRecords();
                            if (updateRecordSet!=null) {
                                numOfUpdate = updateRecordSet.length;
                                info("Updated: " + numOfUpdate + " records");
                                for (MappedRecord r : updateRecordSet) {
                                    if (wsClient.updateIncident(r)) {
                                        CSVFileParser.getMappedRecordDao().makePersistent(r);
                                    }
                                }
                            }

                            // delete the incidents
                            MappedRecord[] deleteRecordSet = csvFileParser.getDeleteRecords();
                            if (deleteRecordSet!=null) {
                                numOfDeletion = deleteRecordSet.length;
                                info("Deleted: " + numOfDeletion + " records");
                                for (MappedRecord r : deleteRecordSet) {
                                    if (wsClient.deleteIncident(r)) {
                                        CSVFileParser.getMappedRecordDao().makeTransient(r);
                                    }
                                }
                            }

                            logger.debug(
                                "number of creation/update/deletion: " + numOfCreation + "/" + numOfUpdate + "/" + numOfDeletion);

                            if (csvFileParser.getErrorList().length() > 0) {
                                errorMessage = csvFileParser.getErrorList();
                            }
                        }
                        info("Upload is done ...");
                        Files.remove(newFile);
                        if (errorMessage.length() > 0) {
                            throw new Exception(errorMessage);
                        }
                    }
                    catch (Throwable e) {
                        logger.error("Exception: " + e.getMessage());
                        UploadPage.this.error("Exception: " + e.getMessage());
                    }
                }
            }
        }
    }
}
