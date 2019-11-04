package io.michaelsimon.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
class DatabaseConfig

@Bean
fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
    val em = LocalContainerEntityManagerFactoryBean()
    em.dataSource = dataSource()
    em.setPackagesToScan(*arrayOf("com.baeldung.persistence.model"))

    val vendorAdapter = HibernateJpaVendorAdapter()
    em.jpaVendorAdapter = vendorAdapter
    em.setJpaProperties(additionalProperties())

    return em
}

@Bean
fun dataSource(): DataSource {
    val dataSource = DriverManagerDataSource()

    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver")
    dataSource.username = "mysqluser"
    dataSource.password = "mysqlpass"
    dataSource.url = "jdbc:mysql://localhost:3306/myDb?createDatabaseIfNotExist=true"

    return dataSource
}

@Bean
fun transactionManager(emf: EntityManagerFactory): PlatformTransactionManager {
    val transactionManager = JpaTransactionManager()
    transactionManager.entityManagerFactory = emf

    return transactionManager
}

@Bean
fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor {
    return PersistenceExceptionTranslationPostProcessor()
}

fun additionalProperties(): Properties {
    val properties = Properties()
    properties.setProperty("hibernate.hbm2ddl.auto", "create-drop")
    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect")

    return properties
}

