package role.season;

import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;

public class RoleGrinder {
	private String id;
	private String name;
	//private String name;
	//AQUI VEM TODAS AS OPERAÇOES E MECANICAS DE PONTE
	public RoleGrinder(GuildVoiceJoinEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		//this.name = e.getMember().getUser().getName();
		RegistradorPontosDB.createTable();	
	}

	public RoleGrinder() {
		
	}
	
	public RoleGrinder(GuildVoiceLeaveEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		RegistradorPontosDB.createTable();
		
	}
	
	public boolean exists() {
		return RegistradorPontosDB.exists(this.id);	
	}
	
	public void adicionarReg() {
		RegistradorPontosDB.adicionaRegistro(this.id, this.name, 0, System.currentTimeMillis()/1000);
	}
	
	public void alterarJoin() {
		RegistradorPontosDB.alteraRegistroJoin(this.id);
	}
	
	public void alterarLeave() {
		RegistradorPontosDB.alteraRegistroLeave(this.id);
	}
	
	public String ranking() {
		return RegistradorPontosDB.ordenarRanking();
	}
	
	
	
}
