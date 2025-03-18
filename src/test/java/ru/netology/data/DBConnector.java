package ru.netology.data;

import lombok.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    private static String dbUrl = System.getProperty("db.url");
    private static String dbUser = System.getProperty("app.user");
    private static String dbPass = System.getProperty("app.password");

    public static PaymentData getLastPaymentData(String tableName) throws SQLException {
        String paymentSQL = "SELECT id, status FROM " + tableName + " ORDER BY created DESC LIMIT 1";
        String id = null;
        String status = null;
        try (
                Connection conn = DriverManager.getConnection(
                        dbUrl, dbUser, dbPass
                );
                Statement paymentStmt = conn.createStatement()
        ) {
            try (var rs = paymentStmt.executeQuery(paymentSQL)) {
                if (rs.next()) {
                    id = rs.getString("id");
                    status = rs.getString("status");
                }
            }
        }
        return new PaymentData(id, status);
    }

    @Value
    public static class PaymentData {
        String id;
        String status;
    }
}
