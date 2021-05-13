package com.karate.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.minidev.json.JSONObject;

public class DBHandler {
	private static String connectionurl = "jdbc:sqlserver://localhost:14330;database=Pubs;user=sa;password=Passw0rd";
	
	public static JSONObject getSysDateFromDual(){
		JSONObject jsonobject = new JSONObject();
		try {
			String sqlquery="Select sysdate from dual";
			Connection connect = DriverManager.getConnection(connectionurl);
			ResultSet rs = connect.prepareStatement(sqlquery).executeQuery();
			rs.next();
			System.out.println(rs.getString("sysdate"));
			jsonobject.put("SYS_DATE", rs.getString("sysdate"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return jsonobject;
		
	}
	
}
