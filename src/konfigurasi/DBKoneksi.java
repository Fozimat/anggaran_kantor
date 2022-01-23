/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konfigurasi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lenovo
 */
public class DBKoneksi {
    public Connection koneksi;
    
    public Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Berhasil Koneksi ke JDBC Driver MySQL");
        } catch (ClassNotFoundException ex) {
            System.out.println("Tidak berhasil Koneksi ke JDBC Driver MySQL");
        }
        //koneksi ke database
        try {
            String url = "jdbc:mysql://localhost:3306/anggaran_sei_enam";
            koneksi = DriverManager.getConnection(url,"root","");
            System.out.println("Berhasil Koneksi ke Database");
        } catch(SQLException e) {
            System.out.println("Tidak Berhasil Koneksi ke Database");
        }
        return koneksi;
    }
    public static void main(String [] args) {
        Connection conn = new DBKoneksi().connect();
    }    
}
