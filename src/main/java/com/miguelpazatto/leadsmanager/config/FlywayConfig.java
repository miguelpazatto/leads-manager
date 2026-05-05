package com.miguelpazatto.leadsmanager.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class FlywayConfig {

    private final DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void runFlywayManually() {
        System.out.println("=================================================");
        System.out.println(" INICIANDO OPERAÇÃO DE FAXINA E MIGRAÇÃO ");
        System.out.println("=================================================");

        try {
            System.out.println(" -> Limpando o banco de dados antigo...");
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("DROP SCHEMA public CASCADE; CREATE SCHEMA public;");
                System.out.println(" -> Banco de dados resetado com sucesso!");
            }

            System.out.println(" -> Iniciando o Flyway...");
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .load();

            flyway.migrate();
            
            System.out.println("=================================================");
            System.out.println(" SUCESSO ABSOLUTO! TABELAS CRIADAS E DADOS INSERIDOS! ");
            System.out.println("=================================================");
            
        } catch (Exception e) {
            System.out.println("=================================================");
            System.out.println(" ERRO NA FAXINA OU NO FLYWAY ");
            System.out.println(" Motivo: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=================================================");
        }
    }
}