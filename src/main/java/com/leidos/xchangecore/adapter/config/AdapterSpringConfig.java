package com.leidos.xchangecore.adapter.config;

import com.leidos.xchangecore.adapter.XchangeCoreAdapter;
import com.leidos.xchangecore.adapter.dao.CoreConfigurationDao;
import com.leidos.xchangecore.adapter.dao.DynamoDBDao;
import com.leidos.xchangecore.adapter.dao.MappedRecordDao;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.xmlbeans.XmlBeansMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import com.google.cloud.datastore.*;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import java.util.Properties;

@Configuration
public class AdapterSpringConfig {

    private static final String Key_hibernate_hbm2dll_auto = "hibernate.hbm2ddl.auto";

    private static Logger logger = LoggerFactory.getLogger(AdapterSpringConfig.class);

    private static String S_AMAZON_ENDPOINT = "amazon.endpoint";
    private static String S_AMAZON_REGION = "amazon.region";
    private static String S_AWS_ACCESS_KEY_ID = "aws.access.key.id";
    private static String S_AWS_SECRET_ACCESS_KEY = "aws.secret.access.key";
    private static String S_DB_TABLE_NAME = "db.table.name";

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${hibernate.persistence.package}")
    private String persistencePackage;
    @Value("${hibernate.hbm2dll.auto}")
    private String hbm2dll;

    @Value("${hibernate.showSql}")
    private String showSql;

    @Value("${hibernate.generateDdl}")
    private String generateDdl;

    @Value("${hibernate.databasePlatform}")
    private String databasePlatform;

    @Value("${amazon.endpoint}")
    private String amazon_endpoint;

    @Value("${amazon.region}")
    private String amazon_region;

    @Value("${aws.access.key.id}")
    private String aws_access_key_id;

    @Value("${aws.secret.access.key}")
    private String aws_secret_access_key;

    @Value("${db.table.name}")
    private String db_table_name;

    @Bean
    public DynamoDBDao dynamoDBDao() {

        DynamoDBDao dynamoDBDao = new DynamoDBDao();

        String DynamoDbUUID = "b4f7738a-4616-46df-bd74-ef082d07feb2";
        /*
         * logger.debug("DynamoDBDao: endpoint: " + amazon_endpoint + ", region: " +
         * amazon_region + ", key_id: " + aws_access_key_id + ", key: " +
         * aws_secret_access_key + ", tableName: " + db_table_name); if (amazon_endpoint
         * == null || amazon_endpoint.equals("${amazon.endpoint}")) { aws_access_key_id
         * = System.getenv(S_AWS_ACCESS_KEY_ID); aws_secret_access_key =
         * System.getenv(S_AWS_SECRET_ACCESS_KEY); amazon_endpoint =
         * System.getenv(S_AMAZON_ENDPOINT); amazon_region =
         * System.getenv(S_AMAZON_REGION); db_table_name =
         * System.getenv(S_DB_TABLE_NAME); }
         * 
         */

        try {
            Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
            Query<Entity> query = Query.newEntityQueryBuilder().setKind("Credentials")
                    .setFilter(StructuredQuery.PropertyFilter.eq("UUID", DynamoDbUUID)).build();

            QueryResults<Entity> results = datastore.run(query);
            Entity entity = results.next();
            aws_access_key_id = entity.getString("username");
            aws_secret_access_key = entity.getString("password");
            amazon_endpoint = entity.getString("Endpoint");
            amazon_region = entity.getString("Region");
            db_table_name = entity.getString("TableName");

            logger.info("**************Got aws_key: " + aws_access_key_id);

            dynamoDBDao.init(aws_access_key_id, aws_secret_access_key, amazon_endpoint, amazon_region, db_table_name);
        } catch (Exception e) {
            logger.error("Error: Create DynamoDBDao: " + e.getMessage());
        }
        return dynamoDBDao;
    }

    @Bean
    public CoreConfigurationDao coreConfigurationDao() {

        return new CoreConfigurationDao();
    }

    @Bean
    public DataSource dataSource() {

        return new SimpleDriverDataSource(new org.hsqldb.jdbc.JDBCDriver(), jdbcUrl, username, password);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        final LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setDataSource(dataSource());
        emfBean.setPersistenceProvider(new HibernatePersistenceProvider());
        emfBean.setPackagesToScan(persistencePackage);
        emfBean.setJpaProperties(jpaProperties());
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());

        emfBean.afterPropertiesSet();

        return emfBean.getObject();
    }

    @Bean
    public Properties jpaProperties() {

        final Properties properties = new Properties();
        properties.setProperty(Key_hibernate_hbm2dll_auto, hbm2dll);

        return properties;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {

        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(showSql.equalsIgnoreCase("true"));
        adapter.setDatabasePlatform(databasePlatform);
        adapter.setGenerateDdl(generateDdl.equalsIgnoreCase("true"));

        return adapter;
    }

    @Bean
    public MappedRecordDao mappedRecordDao() {

        return new MappedRecordDao();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        return new JpaTransactionManager(entityManagerFactory());
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() throws SOAPException {

        // need to pass MessageFactory.newInstance to create a SaajSoapMessageFactory
        final WebServiceTemplate ws = new WebServiceTemplate(new SaajSoapMessageFactory(MessageFactory.newInstance()));

        ws.setMarshaller(new XmlBeansMarshaller());
        ws.setUnmarshaller(new XmlBeansMarshaller());

        return ws;
    }

    @Bean
    public XchangeCoreAdapter xchangeCoreAdapter() {

        return new XchangeCoreAdapter();
    }
}
