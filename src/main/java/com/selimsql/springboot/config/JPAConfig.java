package com.selimsql.springboot.config;

import java.util.Iterator;
import java.util.Properties;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.selimsql.springboot.util.Util;

@Configuration
@EnableTransactionManagement

@PropertySource(value = { "classpath:application.properties" })
public class JPAConfig {

  private Environment env;

  public JPAConfig() {
    final String msg = "construct " + this.getClass().getSimpleName();
    System.out.println(msg);
  }

  @Autowired
  private void setEnvironment(Environment env) {
    this.env = env;
  }

  @Bean
  public DataSource dataSource() {
    System.out.println("Bean: new DataSource");

	BasicDataSource dataSource = new BasicDataSource(); //Uses Poolable Connection

    final String jdbcPREFIX = "spring.datasource.";
    String driverClassName = env.getProperty(jdbcPREFIX + "driver-class-name");
    dataSource.setDriverClassName(driverClassName);

    String driverUrl = env.getProperty(jdbcPREFIX + "url");

    dataSource.setUrl(driverUrl);
    System.out.println("->driverClassName:" + driverClassName);
    System.out.println("->driverUrl:" + driverUrl);

    String username = env.getProperty(jdbcPREFIX + "username");
    dataSource.setUsername(username);

    String pass = env.getProperty(jdbcPREFIX + "password");
    dataSource.setPassword(pass);

    final int initialSizeDEFAULT = 1;
    int initialSize = Util.getInt(env.getProperty("jdbc.pool.initialSize"), initialSizeDEFAULT);
    if (initialSize > 0 && initialSize < 100) {
    	dataSource.setInitialSize(initialSize);
    	dataSource.setMinIdle(initialSize);

    	System.out.println("->initialSize:" + initialSize);
    }

    final int maxActiveDEFAULT = 5;
    int maxActive = Util.getInt(env.getProperty("jdbc.pool.maxActive"), maxActiveDEFAULT);
    if (maxActive >= initialSize && maxActive < 200) {
    	dataSource.setMaxIdle(maxActive); //Default: 100
    	dataSource.setMaxTotal(maxActive); //Default: 100

    	System.out.println("->maxActive:" + maxActive);
    }

    return dataSource;
  }//data_Source


  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws NamingException {
    System.out.println(">>>Bean:entityManagerFactory!!!");

    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    factoryBean.setDataSource(dataSource());

    factoryBean.setPackagesToScan(new String[] { "com.selimsql.springboot.model" });
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
    factoryBean.setJpaProperties(jpaProperties());
    return factoryBean;
  }


  @Bean
  public JpaVendorAdapter jpaVendorAdapter() {
    System.out.println(">>>Bean:jpaVendorAdapter!!!");

    HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
    adapter.setShowSql(true);
    adapter.setGenerateDdl(false);
    return adapter;
  }


  //Here you can specify any provider specific properties.
  private Properties jpaProperties() {
    Properties properties = new Properties();

    final String jpaPropertiesPREFIX = "spring.jpa.properties.";
    final int    jpaPropertiesPREFIXLen = jpaPropertiesPREFIX.length();

    AbstractEnvironment envAbs = (AbstractEnvironment)env;
    for(Iterator<org.springframework.core.env.PropertySource<?>> iter = envAbs.getPropertySources().iterator(); iter.hasNext(); ) {
    	org.springframework.core.env.PropertySource<?> propertySource = iter.next();
        if ((propertySource instanceof MapPropertySource) == false)
        	continue;

    	MapPropertySource mapPropertySource = (MapPropertySource)propertySource;
    	String[] propNames = mapPropertySource.getPropertyNames();
    	int countProps = (propNames==null ? 0 : propNames.length);
    	for(int i=0; i < countProps; i++) {
    		String propName = propNames[i];

    	    if (propName.startsWith(jpaPropertiesPREFIX)==false)
    	    	continue;

    	    Object propValue = mapPropertySource.getProperty(propName);
    	    if (propValue==null)
    	    	continue;

    	    propName = propName.substring(jpaPropertiesPREFIXLen);
            properties.put(propName, propValue);
    	}//for-i:count_Props
    }//for-iter:envAbs.getPropertySources

    System.out.println("jpaProperties: " + properties);

    return properties;
  }//jpa_Properties


  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
    System.out.println("Bean: new JpaTransactionManager:PlatformTransactionManager");

    PlatformTransactionManager platformTransactionManager = new JpaTransactionManager(entityManagerFactory);
    return platformTransactionManager;
  }
}
