package com.ewus.ba.facilityService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Datenbankverbindung {

  private Connection connection;

  public Datenbankverbindung() {
    this.connect();
  }

  public boolean connect() {
    Properties props = Config.readDbProperties();

    String connectionCommand =
        "jdbc:sqlserver://"
            + props.getProperty("dbHost")
            + ";"
            + "database="
            + props.getProperty("dbName")
            + ";"
            + "user="
            + props.getProperty("dbUsername")
            + ";"
            + "password="
            + props.getProperty("dbPassword")
            + ";"
            + "encrypt=false;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";

    try {
      this.connection = DriverManager.getConnection(connectionCommand);
    } catch (SQLException e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return true;
  }

  public Connection getConnection() {
    return this.connection;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }
}
