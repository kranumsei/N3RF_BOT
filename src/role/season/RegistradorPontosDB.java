package role.season;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import bot.n3rf.Constants;


public class RegistradorPontosDB {

	public static Connection registradorPontosDB() {
		try {

			Class.forName("com.mysql.jdbc.Driver");//oracle.jdbc.driver.OracleDriver
			Connection conexao = DriverManager.getConnection("jdbc:mysql://"+Constants.dbIp+"/n3rfdb?useSSL=false", "root", Constants.pass);//jdbc:oracle:thin:@localhost:1521:xe
			return conexao;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void adicionaRegistro(String id, String nome, long pontosTotais, long pontosAtuais) {
		String sql = "INSERT INTO CONTA (ID, NOME, PONTOSTOTAIS, PONTOSTEMP) VALUES ('" + id + "', '" + nome + "', '" + pontosTotais + "', '"+pontosAtuais+"')";
		try {
			PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
			ps.execute();
			System.out.println("Adicionar executado.");
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void alteraRegistroJoin(String id) {
		String sql = "UPDATE CONTA SET PONTOSTEMP = '"+(System.currentTimeMillis()/1000)+"' WHERE ID = '"+id+"'";
		try {
			PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void alteraRegistroLeave(String id) {
		long pontosTotais = calculaPontosTotais(id);
		long pontosAtuais = calculaPontosAtuais(id);
		if(pontosAtuais != 0) {
			String sql = "UPDATE CONTA SET PONTOSTOTAIS = '"+pontosTotais+"', PONTOSTEMP = '0' WHERE ID = '"+id+"'";
			try {
				PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
				ps.execute();
				ps.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			return;
		}
	}

	public static long calculaPontosTotais(String id) {
		long pontuacaoInicial = 0;
		long pontuacaoTotal = 0;
		String sql = "SELECT C.PONTOSTOTAIS, C.PONTOSTEMP  FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			res.next();
			pontuacaoInicial = res.getLong("PONTOSTEMP");
			pontuacaoTotal = res.getLong("PONTOSTOTAIS");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long result = pontuacaoTotal + (System.currentTimeMillis()/1000 - pontuacaoInicial);
		return result;
	}
	
	public static long calculaPontosAtuais(String id) {
		long pontuacaoInicial = 0;
		String sql = "SELECT C.PONTOSTEMP  FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			res.next();
			pontuacaoInicial = res.getLong("PONTOSTEMP");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pontuacaoInicial;
	}

	public static boolean exists(String id) {
		String sql = "SELECT C.ID FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			if(res.next()) {
				return true;	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

//	public static boolean tableExists() {
//		String sql = "SELECT ID FROM CONTA LIMIT 1";
//		try {
//			Statement stmt = registradorPontosDB().createStatement();
//			ResultSet res = stmt.executeQuery(sql);
//			if(res.next()) {
//				return true;	
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
	
	public static void createTable() {
		String sql = "CREATE TABLE CONTA(ID VARCHAR(200) NOT NULL, NOME VARCHAR(200) NOT NULL, PONTOSTOTAIS LONG NOT NULL, PONTOSTEMP LONG NOT NULL, PRIMARY KEY (ID))";
		try {
			PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			
		}
	}
	
	// TRATA AS CONEXOES COM O DB
}
