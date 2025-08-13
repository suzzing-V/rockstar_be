package suzzingv.suzzingv.rockstar.global.db;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Configuration
@EnableJpaRepositories(
        basePackages = "suzzingv.suzzingv.rockstar",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION, classes = DBMarkers.MainRepository.class
        ),
        entityManagerFactoryRef = "mainEmf",
        transactionManagerRef = "mainTx"
)
public class MainDataSourceConfig {

    @Bean("dataSource")
    @Primary
    @Profile("dev")
    @ConfigurationProperties("spring.datasource.h2")
    public DataSource h2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("dataSource")
    @Primary
    @Profile("prod")
    @ConfigurationProperties("spring.datasource")
    public DataSource mariaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEmf(
            @Qualifier("dataSource") DataSource ds,
            Environment env
    ) {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);
        
        // @MainEntity 어노테이션이 붙은 클래스들을 스캔
        List<String> mainEntityPackages = findMainEntityPackages("suzzingv.suzzingv.rockstar");
        em.setPackagesToScan(mainEntityPackages.toArray(new String[0]));
        
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        var props = new HashMap<String, Object>();
        
        // 공통 naming strategy 설정
        props.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        
        // 환경별 설정 분기
        if (env.acceptsProfiles("dev")) {
            // H2 설정
            props.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa-h2.hibernate.ddl-auto", "create"));
            props.put("hibernate.dialect", env.getProperty("spring.jpa-h2.properties.hibernate.dialect", "org.hibernate.dialect.H2Dialect"));
            props.put("hibernate.show_sql", env.getProperty("spring.jpa-h2.properties.hibernate.show-sql", "true"));
            
            // import files 설정
            String importFiles = env.getProperty("spring.jpa-h2.properties.hibernate.hbm2ddl.import_files");
            if (importFiles != null) {
                props.put("hibernate.hbm2ddl.import_files", importFiles);
            }
        } else if (env.acceptsProfiles("prod")) {
            // MariaDB 설정
            props.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto", "update"));
            props.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.MariaDBDialect"));
            props.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql", "true"));
            props.put("hibernate.jdbc.time_zone", env.getProperty("spring.jpa.properties.hibernate.jdbc.time_zone", "Asia/Seoul"));
        }
        
        em.setJpaPropertyMap(props);
        return em;
    }

    private List<String> findMainEntityPackages(String basePackage) {
        List<String> entityPackages = new ArrayList<>();
        entityPackages.add(basePackage);
        return entityPackages;
    }

    @Bean
    @Primary
    public PlatformTransactionManager mainTx(
            @Qualifier("mainEmf") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}