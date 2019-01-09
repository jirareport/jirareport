package br.com.leonardoferreira.jirareport.base;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Component
public class CleanDatabase {

    @Autowired
    private DataSource dataSource;

    @SneakyThrows
    public void clean() {
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {
            truncateTables(connection, stmt);
        }
    }

    @SneakyThrows
    private void truncateTables(final Connection connection, final Statement stmt) {
        try (ResultSet rs = connection.getMetaData().getTables(null, null, null, new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (!"flyway_schema_history".equals(tableName)) {
                    stmt.executeUpdate("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE");
                }
            }
        }
    }

}
