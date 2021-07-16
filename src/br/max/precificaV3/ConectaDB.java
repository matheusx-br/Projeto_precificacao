package br.max.precificaV3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConectaDB {
    public Connection jdbc() throws SQLException {
        String server = "localhost";
        String port = "1521";
        String database = "sankhya_pdb1.subredeprivada.rdtudodebicho.oraclevcn.com";
        String user = "SANKHYA";
        String passwd = "o7%dRP.9M";
            String url = "jdbc:oracle:thin:@" + server + ":" + port + "/" + database;
            Connection con = DriverManager.getConnection(url, user, passwd);

            return con;
    }
}
