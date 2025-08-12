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
        basePackages =  "suzzingv.suzzingv.rockstar",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION, classes = DBMarkers.PgRepository.class
        ),
        entityManagerFactoryRef = "pgEmf",
        transactionManagerRef = "pgTx"
)
public class PgDataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.pg")
    public DataSource pgDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean pgEmf(
            @Qualifier("pgDataSource") DataSource ds,
            Environment env
    ) {
        var em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);
        
        // @PgEntity 어노테이션이 붙은 클래스들을 찾아서 등록
        try {
            List<String> pgEntityPackages = findPgEntityPackages("suzzingv.suzzingv.rockstar");
            em.setPackagesToScan(pgEntityPackages.toArray(new String[0]));
        } catch (Exception e) {
            throw new RuntimeException("Failed to scan @PgEntity classes", e);
        }
        
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        var props = new HashMap<String, Object>();
        props.put("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        props.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa-pg.hibernate.ddl-auto","validate"));
        props.put("hibernate.dialect", env.getProperty("spring.jpa-pg.properties.hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect"));
        em.setJpaPropertyMap(props);
        return em;
    }

    private List<String> findPgEntityPackages(String basePackage) throws Exception {
        List<String> entityPackages = new ArrayList<>();
        entityPackages.add(basePackage); // 일단 전체 패키지를 스캔하도록 설정
        return entityPackages;
    }

    @Bean
    public PlatformTransactionManager pgTx(
            @Qualifier("pgEmf") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
