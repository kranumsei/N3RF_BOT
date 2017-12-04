package role.season;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

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

	public static void adicionaNovoRegistro(String id, String nome, long pontosTotais, long pontosAtuais) {
		String sql = "INSERT INTO CONTA (ID, NOME, PONTOSTOTAIS, PONTOSTEMP, MODIFIER) VALUES ('" + id + "', '" + nome + "', '" + pontosTotais + "', '"+pontosAtuais+"', '0')";
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
	
	public static void atualizarPontosTemp(String id) {
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
	
	public static void atualizarPontosTotais(String id) {
		long pontosTotais = calculaPontosTotais(id);
		long pontosAtuais = getPontosAtuais(id);
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
		int modificador = getModifier(id);
		long pontuacaoInicial = 0;
		long pontuacaoTotal = 0;
		long result;
		String sql = "SELECT C.PONTOSTOTAIS, C.PONTOSTEMP  FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			if(res.next()) {
				pontuacaoInicial = res.getLong("PONTOSTEMP");
				pontuacaoTotal = res.getLong("PONTOSTOTAIS");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(modificador == 2) {
			result = pontuacaoTotal + (((System.currentTimeMillis()/1000) - pontuacaoInicial)/30);
		}else if(modificador == 3) {
			result = pontuacaoTotal + (((System.currentTimeMillis()/1000) - pontuacaoInicial)/20);
		}else if(modificador == 4){
			result = pontuacaoTotal + (((System.currentTimeMillis()/1000) - pontuacaoInicial)/10);
		}else {
			result = pontuacaoTotal + (((System.currentTimeMillis()/1000) - pontuacaoInicial)/60);
		}
		return result;
	}
	
	public static void setModifier(String id, int modificador) {
		String sql = "UPDATE CONTA SET MODIFIER = '"+modificador+"' WHERE ID = '"+id+"'";
		try {
			PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			
		}
	}
	
	public static int getModifier(String id) {
		int modificador = -1;
		String sql = "SELECT C.MODIFIER  FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			if(res.next()) {
				modificador = res.getInt("MODIFIER");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return modificador;
	}
	
	public static int getPontosTotais(String id) {
		int pTotais = -1;
		String sql = "SELECT C.PONTOSTOTAIS FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			if(res.next()) {
				pTotais = res.getInt("MODIFIER");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pTotais;
	}
	
	public static long getPontosAtuais(String id) {
		long pontuacaoInicial = 0;
		String sql = "SELECT C.PONTOSTEMP  FROM CONTA C WHERE ID = " + id;
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			if(res.next()) {
				pontuacaoInicial = res.getLong("PONTOSTEMP");
			}
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
	
	public static void resetRanking() {
		String sql = "DELETE FROM CONTA";
		try {
			PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			
		}
	}
	
	public static void createTable() {
		String sql = "CREATE TABLE CONTA(ID VARCHAR(200) NOT NULL, NOME VARCHAR(200) NOT NULL, PONTOSTOTAIS LONG NOT NULL, PONTOSTEMP LONG NOT NULL, MODIFIER INT NOT NULL, PRIMARY KEY (ID))";
		try {
			PreparedStatement ps = registradorPontosDB().prepareStatement(sql);
			ps.execute();
			ps.close();

		} catch (SQLException e) {
			
		}
	}
	
	public static String ordenarRanking() {
		String sql = "SELECT * FROM CONTA ORDER BY PONTOSTOTAIS + 0 DESC";
		String resposta = "";
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				String nome = res.getString("NOME");
				String pontos = res.getString("PONTOSTOTAIS");
				resposta = resposta + nome+" ------ "+pontos+"\n";
			 }
			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resposta;
	}
	
	public static Stack<Long> rankingStack() {
		String sql = "SELECT ID FROM CONTA ORDER BY PONTOSTOTAIS + 0 ASC";
		Stack<Long> resposta = new Stack<>();
		try {
			Statement stmt = registradorPontosDB().createStatement();
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				long id = res.getLong("ID");
				resposta.push(id);
			 }
			res.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resposta;
	}
	
	// TRATA AS CONEXOES COM O DB
}
