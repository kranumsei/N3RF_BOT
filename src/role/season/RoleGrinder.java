package role.season;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bot.n3rf.VariaveisConfiguraveis;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

public class RoleGrinder {
	private String id;
	private String name;
	private VoiceChannel afkRoom;
	private Role admins;
	private boolean isAfk;
	private Member membroAtual;
	private List<Member> listaUsuarios;
	private List<Member> listaUsuarios2;

	// private String name;
	// AQUI VEM TODAS AS OPERAÇOES E MECANICAS DE PONTE
	public RoleGrinder(GuildVoiceJoinEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.afkRoom = e.getGuild().getAfkChannel();
		this.listaUsuarios = e.getChannelJoined().getMembers();
		this.admins = e.getGuild().getRoles().get(1);
		this.membroAtual = e.getMember();
		if (e.getChannelJoined().equals(this.afkRoom)) {
			this.isAfk = true;
		} else {
			this.isAfk = false;
		}
		// this.name = e.getMember().getUser().getName();
		// MembroDAO.createTable();
	}

	public RoleGrinder(GuildMemberJoinEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.afkRoom = e.getGuild().getAfkChannel();
		this.admins = e.getGuild().getRoles().get(1);
		this.membroAtual = e.getMember();
		// MembroDAO.createTable();
	}

	public RoleGrinder(GuildVoiceMoveEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.afkRoom = e.getGuild().getAfkChannel();
		this.listaUsuarios = e.getChannelJoined().getMembers();
		this.listaUsuarios2 = e.getChannelLeft().getMembers();
		this.admins = e.getGuild().getRoles().get(1);
		this.membroAtual = e.getMember();
		if (e.getChannelJoined().equals(this.afkRoom)) {
			this.isAfk = true;
		} else {
			this.isAfk = false;
		}
		// MembroDAO.createTable();
	}

	public RoleGrinder() {

	}

	public RoleGrinder(GuildVoiceLeaveEvent e) {
		this.id = e.getMember().getUser().getId();
		this.name = e.getMember().getUser().getName();
		this.listaUsuarios = e.getChannelLeft().getMembers();
		this.admins = e.getGuild().getRoles().get(1);
		this.membroAtual = e.getMember();
		// MembroDAO.createTable();

	}

	public boolean exists(String id) {
		return MembroDAO.exists(id);
	}

	public void adicionarReg() {
		MembroDAO.adicionaNovoRegistro(this.id, this.name);
	}

	public void salvarPontos() {
		// RegistradorPontosDB.atualizarPontosTemp(this.id);
		if (!membroAtual.isOwner() && !membroAtual.getRoles().get(0).getName().equals("Legendary")) {
			for (int i = 0; i < listaUsuarios.size(); i++) {
				if (exists(listaUsuarios.get(i).getUser().getId()) && !listaUsuarios.get(i).getUser().isBot()
						&& !listaUsuarios.get(i).isOwner()
						&& !listaUsuarios.get(i).getRoles().get(0).getName().equals(admins.getName())) {
					MembroDAO.atualizarPontosTotais(listaUsuarios.get(i).getUser().getId());
					MembroDAO.atualizarPontosTemp(listaUsuarios.get(i).getUser().getId(), Long.parseLong("0"));

				}else if (!exists(listaUsuarios.get(i).getUser().getId()) && !listaUsuarios.get(i).getUser().isBot()
						&& !listaUsuarios.get(i).isOwner()
						&& !listaUsuarios.get(i).getRoles().get(0).getName().equals(admins.getName())) {
					MembroDAO.adicionaNovoRegistro(listaUsuarios.get(i).getUser().getId(), listaUsuarios.get(i).getUser().getName());
				}

			}
			MembroDAO.atualizarPontosTotais(this.id);
		}
	}

	public void alterarModificadoresAntigaRoom() {
		if (isAfk() == false) {
			int botQuantity = 0;
			for(int i = 0; i < listaUsuarios2.size(); i++) {
				if(listaUsuarios2.get(i).getUser().isBot()) {
					botQuantity++;
				}
			}
			if (listaUsuarios2.size() - botQuantity == 1) {
				MembroDAO.setModifier(listaUsuarios2.get(0).getUser().getId(), 1);
			} else if (listaUsuarios2.size() - botQuantity >= 2) {
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
				if (jogandoMesmoJogo == true) {
					for (int i = 0; i < listaJogadores.size(); i++) {
						MembroDAO.setModifier(listaJogadores.get(i), 4);
					}
				}else if (listaUsuarios2.size() - botQuantity == 2) {
					for (int i = 0; i < listaUsuarios2.size(); i++) {
						MembroDAO.setModifier(listaUsuarios2.get(i).getUser().getId(), 2);
					}
				} else if (listaUsuarios2.size() - botQuantity > 2) {
					for (int i = 0; i < listaUsuarios2.size(); i++) {
						MembroDAO.setModifier(listaUsuarios2.get(i).getUser().getId(), 3);
					}
				}
				
			}
		}
	}

	public void alterarModificadoresNovaRoom() {
		if (isAfk() == false) {
			int botQuantity = 0;
			for(int i = 0; i < listaUsuarios.size(); i++) {
				if(listaUsuarios.get(i).getUser().isBot()) {
					botQuantity++;
				}
			}
			if (listaUsuarios.size() - botQuantity == 1) {
				MembroDAO.setModifier(listaUsuarios.get(0).getUser().getId(), 1);
			} else if (listaUsuarios.size() - botQuantity >= 2) {
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
				if (jogandoMesmoJogo == true) {
					for (int i = 0; i < listaJogadores.size(); i++) {
						MembroDAO.setModifier(listaJogadores.get(i), 4);
					}
				}else if (listaUsuarios.size() - botQuantity == 2) {
					for (int i = 0; i < listaUsuarios.size(); i++) {
						MembroDAO.setModifier(listaUsuarios.get(i).getUser().getId(), 2);
					}
				} else if (listaUsuarios.size() - botQuantity > 2) {
					for (int i = 0; i < listaUsuarios.size(); i++) {
						MembroDAO.setModifier(listaUsuarios.get(i).getUser().getId(), 3);
					}
				}
				
			}
		}
	}

	public void iniciarContagem() {
		if (isAfk() == false) {
			for (int i = 0; i < listaUsuarios.size(); i++) {
				if (!isAfk()) {
					MembroDAO.atualizarPontosTemp(listaUsuarios.get(i).getUser().getId(),
							System.currentTimeMillis() / 1000);
				} else {
					MembroDAO.atualizarPontosTotais(this.id);
				}
			}
		}
	}

	public ArrayList<ArrayList<Long>> definirCargos() {
		Stack<Long> pilha = MembroDAO.rankingStack();

		ArrayList<ArrayList<Long>> ids = new ArrayList<>();
		if (ids.size() == 0) {
			ArrayList<Long> epics = new ArrayList<>();
			ids.add(epics);
			while (ids.get(0).size() < VariaveisConfiguraveis.numEpics && !pilha.isEmpty()) {
				ids.get(0).add(pilha.pop());
			}
		}
		if (ids.size() == 1 && !pilha.isEmpty()) {
			ArrayList<Long> rares = new ArrayList<>();
			ids.add(rares);
			while (ids.get(1).size() < VariaveisConfiguraveis.numRares && !pilha.isEmpty()) {
				ids.get(1).add(pilha.pop());
			}
		}

		return ids;
	}

	public void resetRanking() {
		MembroDAO.resetRanking();
	}

	public String ranking() {
		return MembroDAO.ordenarRanking();
	}

	public boolean isAfk() {
		if (this.isAfk == true) {
			return true;
		} else {
			return false;
		}
	}

}
