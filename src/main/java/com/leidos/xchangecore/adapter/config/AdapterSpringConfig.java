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
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.xmlbeans.XmlBeansMarshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import java.util.Properties;

@Configuration
public class AdapterSpringConfig {

    private static final String Key_hibernate_hbm2dll_auto = "hibernate.hbm2ddl.auto";

    private static Logger logger = LoggerFactory.getLogger(AdapterSpringConfig.class);

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
        dynamoDBDao.init(System.getenv(aws_access_key_id),
                         System.getenv(aws_secret_access_key),
                         System.getenv(amazon_endpoint),
                         System.getenv(amazon_region),
                         System.getenv(db_table_name));
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
