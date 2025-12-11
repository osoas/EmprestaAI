package br.edu.ifce.emprestaai.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StartupDatabaseFix {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(StartupDatabaseFix.class);

    @Autowired
    public StartupDatabaseFix(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void run() {
        try {
            logger.info("StartupDatabaseFix: attempting to make Emprestimo.id_solicitacao nullable (dev mode)");
            // MySQL syntax: MODIFY COLUMN <name> <type> NULL
            // We try INT and allow null. If the column already allows null, this is a no-op or harmless.
            jdbcTemplate.execute("ALTER TABLE Emprestimo MODIFY COLUMN id_solicitacao INT NULL");
            logger.info("StartupDatabaseFix: ALTER TABLE executed (id_solicitacao set to NULLABLE)");
        } catch (Exception ex) {
            logger.warn("StartupDatabaseFix: could not alter table Emprestimo (permission/schema). This is fine in many environments. Error: {}", ex.getMessage());
        }
    }
}

