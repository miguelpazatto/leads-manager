package com.miguelpazatto.leadsmanager.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    private final DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void runFlywayManually() {
        System.out.println("=================================================");
        System.out.println(" ASSUMINDO O CONTROLE: INICIANDO FLYWAY MANUALMENTE ");
        System.out.println("=================================================");

        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();

            flyway.migrate();
            
            System.out.println("=================================================");
            System.out.println(" FLYWAY FINALIZADO COM SUCESSO! TABELAS CRIADAS! ");
            System.out.println("=================================================");
            
        } catch (Exception e) {
            System.out.println("=================================================");
            System.out.println(" ERRO FATAL NO FLYWAY: O MISTÉRIO REVELADO ");
            System.out.println(" Motivo: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=================================================");
        }
    }
}
