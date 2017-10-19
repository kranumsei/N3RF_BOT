package role.season;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

public class RoleGrinder {
	private String id;
	private String name;
	private VoiceChannel afkRoom;
	private boolean isAfk;
	private List<Member> listaUsuarios;
	private List<Member> listaUsuarios2;
	// private String name;
	// AQUI VEM TODAS AS OPERAÇOES E MECANICAS DE PONTE
	public RoleGrinder(GuildVoiceJoinEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.afkRoom = e.getGuild().getAfkChannel();
		this.listaUsuarios = e.getChannelJoined().getMembers();
		if (e.getChannelJoined().equals(this.afkRoom)) {
			this.isAfk = true;
		} else {
			this.isAfk = false;
		}
		// this.name = e.getMember().getUser().getName();
		RegistradorPontosDB.createTable();
	}

	public RoleGrinder(GuildVoiceMoveEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.afkRoom = e.getGuild().getAfkChannel();
		this.listaUsuarios = e.getChannelJoined().getMembers();
		this.listaUsuarios2 = e.getChannelLeft().getMembers();
		if (e.getChannelJoined().equals(this.afkRoom)) {
			this.isAfk = true;
		} else {
			this.isAfk = false;
		}
		RegistradorPontosDB.createTable();
	}

	public RoleGrinder() {

	}

	public RoleGrinder(GuildVoiceLeaveEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.listaUsuarios = e.getChannelLeft().getMembers();
		RegistradorPontosDB.createTable();

	}

	public boolean exists(String id) {
		return RegistradorPontosDB.exists(id);
	}

	public void adicionarReg() {
			RegistradorPontosDB.adicionaNovoRegistro(this.id, this.name, 0, System.currentTimeMillis() / 1000);
	}

	public void salvarPontos() {
		RegistradorPontosDB.atualizarPontosTotais(this.id);
		RegistradorPontosDB.atualizarPontosTemp(this.id);
		for (int i = 0; i < listaUsuarios.size(); i++) {
			if(exists(listaUsuarios.get(i).getUser().getId())) {
				RegistradorPontosDB.atualizarPontosTotais(listaUsuarios.get(i).getUser().getId());	
			}else {
				RegistradorPontosDB.adicionaNovoRegistro(listaUsuarios.get(i).getUser().getId(), listaUsuarios.get(i).getUser().getName(), 0, System.currentTimeMillis() / 1000);
			}
		}
		
	}

	public void alterarModificadoresAntigaRoom() {
		if(isAfk() == false) {
			if (listaUsuarios2.size() == 1) {
				RegistradorPontosDB.setModifier(listaUsuarios2.get(0).getUser().getId(), 1);
			} else if (listaUsuarios2.size() >= 2) {
				boolean jogandoMesmoJogo = false;
				ArrayList<String> listaJogadores = new ArrayList<>();
				for (int x = 0; x < listaUsuarios2.size(); x++) {
					Game jogoDoUser1 = listaUsuarios2.get(x).getGame();
					String idUser1 = listaUsuarios2.get(x).getUser().getId();
					for (int y = x + 1; y < listaUsuarios2.size(); y++) {
						Game jogoDoUser2 = listaUsuarios2.get(y).getGame();
						String idUser2 = listaUsuarios2.get(y).getUser().getId();
						if (jogoDoUser1 == jogoDoUser2 && jogoDoUser1 != null) {
							jogandoMesmoJogo = true;
							if (!listaJogadores.contains(idUser1)) {
								listaJogadores.add(idUser1);
							}
							if (!listaJogadores.contains(idUser2)) {
								listaJogadores.add(idUser2);
							}
						}
					}
				}
				if (listaUsuarios2.size() == 2) {
					for (int i = 0; i < listaUsuarios2.size(); i++) {
						RegistradorPontosDB.setModifier(listaUsuarios2.get(i).getUser().getId(), 2);
					}
				} else if (listaUsuarios2.size() > 2) {
					for (int i = 0; i < listaUsuarios2.size(); i++) {
						RegistradorPontosDB.setModifier(listaUsuarios2.get(i).getUser().getId(), 3);
					}
				}
				if (jogandoMesmoJogo == true) {
					for (int i = 0; i < listaJogadores.size(); i++) {
						RegistradorPontosDB.setModifier(listaJogadores.get(i), 4);
					}
				}
			}
		}
	}
	
	public void alterarModificadoresNovaRoom() {
		if(isAfk() == false) {
			if (listaUsuarios.size() == 1) {
				RegistradorPontosDB.setModifier(listaUsuarios.get(0).getUser().getId(), 1);
			} else if (listaUsuarios.size() >= 2) {
				boolean jogandoMesmoJogo = false;
				ArrayList<String> listaJogadores = new ArrayList<>();
				for (int x = 0; x < listaUsuarios.size(); x++) {
					Game jogoDoUser1 = listaUsuarios.get(x).getGame();
					String idUser1 = listaUsuarios.get(x).getUser().getId();
					for (int y = x + 1; y < listaUsuarios.size(); y++) {
						Game jogoDoUser2 = listaUsuarios.get(y).getGame();
						String idUser2 = listaUsuarios.get(y).getUser().getId();
						if (jogoDoUser1 == jogoDoUser2 && jogoDoUser1 != null) {
							jogandoMesmoJogo = true;
							System.out.println(jogoDoUser1);
							if (!listaJogadores.contains(idUser1)) {
								listaJogadores.add(idUser1);
							}
							if (!listaJogadores.contains(idUser2)) {
								listaJogadores.add(idUser2);
							}
						}
					}
				}
				if (listaUsuarios.size() == 2) {
					for (int i = 0; i < listaUsuarios.size(); i++) {
						RegistradorPontosDB.setModifier(listaUsuarios.get(i).getUser().getId(), 2);
					}
				} else if (listaUsuarios.size() > 2) {
					for (int i = 0; i < listaUsuarios.size(); i++) {
						RegistradorPontosDB.setModifier(listaUsuarios.get(i).getUser().getId(), 3);
					}
				}
				if (jogandoMesmoJogo == true) {
					for (int i = 0; i < listaJogadores.size(); i++) {
						RegistradorPontosDB.setModifier(listaJogadores.get(i), 4);
					}
				}
			}
		}
	}

	public void iniciarContagem() {
		if (isAfk() == false) {
			for (int i = 0; i < listaUsuarios.size(); i++) {
				if (!isAfk()) {
					RegistradorPontosDB.atualizarPontosTemp(listaUsuarios.get(i).getUser().getId());
				} else {
					RegistradorPontosDB.atualizarPontosTotais(this.id);
				}
			}
		}
	}

	public String ranking() {
		return RegistradorPontosDB.ordenarRanking();
	}

	public boolean isAfk() {
		if (this.isAfk == true) {
			System.out.println("AFK Channel joined.");
			return true;
		} else {
			return false;
		}
	}

}
