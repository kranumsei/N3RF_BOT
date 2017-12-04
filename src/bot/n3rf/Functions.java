package bot.n3rf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//import java.util.List;
import java.util.Random;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
//import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import role.season.RoleGrinder;

public class Functions {
	private MessageReceivedEvent messageEvent;
	private GuildMemberJoinEvent guildJoinEvent;
	private GuildMemberLeaveEvent guildLeaveEvent;
	private GuildMemberRoleRemoveEvent roleRemoveEvent;
	private GuildVoiceJoinEvent voiceChatJoinEvent;
	private GuildVoiceLeaveEvent voiceChatLeaveEvent;
	private GuildVoiceMoveEvent voiceChatMoveEvent;
	private GuildMemberRoleAddEvent roleAddEvent;
	private GuildVoiceDeafenEvent voiceDeafenEvent;
	private Support supp = new Support();
	// private String name = messageEvent.getAuthor().getName();
	private Calendar calendar;
	private LogGenerator log = new LogGenerator();

	// calendar = Calendar.getInstance();
	public Functions(MessageReceivedEvent e) {
		this.messageEvent = e;
	}

	public Functions(GuildVoiceDeafenEvent e) {
		this.voiceDeafenEvent = e;
	}

	public Functions(GuildVoiceJoinEvent e) {
		this.voiceChatJoinEvent = e;
	}

	public Functions(GuildVoiceLeaveEvent e) {
		this.voiceChatLeaveEvent = e;
	}

	public Functions(GuildVoiceMoveEvent e) {
		this.voiceChatMoveEvent = e;
	}

	public Functions(GuildMemberRoleAddEvent e) {
		this.roleAddEvent = e;
	}

	public Functions(GuildMemberLeaveEvent e) {
		this.guildLeaveEvent = e;
	}

	public Functions(GuildMemberJoinEvent e) {
		this.guildJoinEvent = e;
	}

	public Functions(GuildMemberRoleRemoveEvent e) {
		this.roleRemoveEvent = e;
	}

	public void atribuirCargos() {
		Guild server = messageEvent.getGuild();
		RoleGrinder rg = new RoleGrinder();
		Role epic = messageEvent.getGuild().getRoles().get(VariaveisConfiguraveis.posicaoEpic);
		Role rare = messageEvent.getGuild().getRoles().get(VariaveisConfiguraveis.posicaoRare);
		Role common = messageEvent.getGuild().getRoles().get(VariaveisConfiguraveis.posicaoCommon);
		List<Member> demotedEpics = server.getMembersWithRoles(epic);
		List<Member> demotedRares = server.getMembersWithRoles(rare);
		ArrayList<ArrayList<Long>> ids = rg.definirCargos();
		if (ids.get(1).size() < VariaveisConfiguraveis.numRares
				|| ids.get(0).size() < VariaveisConfiguraveis.numEpics) {
			messageEvent.getChannel().sendMessage("Não possui gente o suficiente para eu atribuir cargos.").queue();
			return;
		}
		for (int y = 0; y < demotedEpics.size(); y++) {
			if (!demotedEpics.isEmpty()) {
				messageEvent.getGuild().getController().modifyMemberRoles(demotedEpics.get(y), common).queue();
			} else {
				break;
			}
		}
		for (int z = 0; z < demotedRares.size(); z++) {
			if (!demotedRares.isEmpty()) {
				messageEvent.getGuild().getController().modifyMemberRoles(demotedRares.get(z), common).queue();
			} else {
				break;
			}
		}
		for (int i = 0; i < ids.size(); i++) {
			for (int x = 0; x < ids.get(i).size(); x++) {
				Member membroAtual = server.getMemberById(ids.get(i).get(x));
				if (i == 0) {
					messageEvent.getGuild().getController().modifyMemberRoles(membroAtual, epic).queue();
				}
				if (i == 1) {
					messageEvent.getGuild().getController().modifyMemberRoles(membroAtual, rare).queue();
				}
			}
		}
		rg.resetRanking();
		messageEvent.getTextChannel().sendMessage("Cargos atribuidos.").queue();

	}

	public void limparComando() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		deleteMessage();
		sendPrivateMessage(messageEvent.getAuthor(),
				"Comandos só podem ser feitos na aba 'commands', fale com o admin para conseguir acesso.");
		System.out.println(
				messageEvent.getAuthor().getName() + " tentou dar comandos em um chat que nao era o 'Commands'");
		log.logWriter(dataAtual + messageEvent.getAuthor().getName()
				+ " tentou dar comandos em um chat que nao era o 'Commands'");
	}

	public void flipCoin() {
		Random rand = new Random();
		int rng = rand.nextInt(2 + 1);
		if (rng == 1) {
			messageEvent.getTextChannel().sendMessage("Heads").queue();
		} else {
			messageEvent.getTextChannel().sendMessage("Tails").queue();
		}
	}

	public void displayRanking() {
		RoleGrinder rg = new RoleGrinder();
		messageEvent.getTextChannel().sendMessage("Ranking:\n" + rg.ranking()).queue();
	}

	public void whoIs() {
		String message = messageEvent.getMessage().getContent();
		int numMembros = messageEvent.getGuild().getMembers().size();
		for (int i = 0; i < numMembros; i++) {
			User usuarioAlvo = messageEvent.getGuild().getMembers().get(i).getUser();
			if (message.contains(usuarioAlvo.getName())) {
				String data = supp.reverteData(
						messageEvent.getGuild().getMembers().get(i).getJoinDate().toString().substring(0, 10));
				String cargoLista = supp.formatarCargos(
						messageEvent.getGuild().getMembers().get(i).getRoles().toString(),
						messageEvent.getGuild().getMembers().get(i).getRoles().size());
				log.logWriter("WhoIs executado para " + usuarioAlvo.getName());
				System.out.println("WhoIs executado para" + usuarioAlvo.getName());
				System.out.println(cargoLista);
				messageEvent.getTextChannel()
						.sendMessage(usuarioAlvo.getName() + ":\n Status: "
								+ messageEvent.getGuild().getMembers().get(i).getOnlineStatus() + "\n Cargos: "
								+ cargoLista + "\n Membro desde: " + data)
						.queue();
			}

		}
	}

	public void entrouNoServer() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		RoleGrinder rg = new RoleGrinder(guildJoinEvent);
		rg.adicionarReg();
		log.logWriter("\n" + dataAtual + guildJoinEvent.getUser().getName() + " entrou no server "
				+ guildJoinEvent.getGuild().getName() + " em " + guildJoinEvent.getMember().getJoinDate() + "\n");

		guildJoinEvent.getGuild().getController()
				.addRolesToMember(guildJoinEvent.getMember(), guildJoinEvent.getGuild().getRolesByName("Common", true))
				.queue();

		System.out.println("\n" + guildJoinEvent.getUser().getName() + " entrou no server "
				+ guildJoinEvent.getGuild().getName() + "\n");
		sendPrivateMessage(guildJoinEvent.getUser(),
				"Bem-vindo ao " + guildJoinEvent.getGuild().getName() + ". Duvidas? Fale com algum moderador.");
		sendPrivateMessage(guildJoinEvent.getGuild().getOwner().getUser(),
				guildJoinEvent.getMember().getUser().getName() + " entrou no server "
						+ guildJoinEvent.getGuild().getName());
	}

	public void saiuDoServer() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		log.logWriter("\n" + dataAtual + "Usuario " + guildLeaveEvent.getMember().getUser().getName()
				+ " saiu do server " + guildLeaveEvent.getGuild().getName() + "\n");
		System.out.println("\nUsuario " + guildLeaveEvent.getMember().getUser().getName() + " saiu do server "
				+ guildLeaveEvent.getGuild().getName() + "\n");
		sendPrivateMessage(guildLeaveEvent.getGuild().getOwner().getUser(),
				guildLeaveEvent.getMember().getUser().getName() + " era parte do server desde "
						+ guildLeaveEvent.getMember().getJoinDate().toString() + " mas saiu do server.");
	}

	public void cargoRemovido() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		log.logWriter(dataAtual + roleRemoveEvent.getMember().getUser().getName()
				+ " teve uma role removida no servidor " + roleRemoveEvent.getGuild().getName()
				+ ". Suas roles agora são " + supp.formatarCargos(roleRemoveEvent.getMember().getRoles().toString(),
						roleRemoveEvent.getMember().getRoles().size()));

		System.out.println(roleRemoveEvent.getMember().getUser().getName() + " teve uma role removida no sevidor "
				+ roleRemoveEvent.getGuild().getName() + ". Suas roles agora são "
				+ supp.formatarCargos(roleRemoveEvent.getMember().getRoles().toString(),
						roleRemoveEvent.getMember().getRoles().size()));
	}

	public void cargoAdicionado() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		log.logWriter(dataAtual + roleAddEvent.getMember().getUser().getName()
				+ " teve uma role adicionada no servidor " + roleAddEvent.getGuild().getName()
				+ ". Suas roles agora são " + supp.formatarCargos(roleAddEvent.getMember().getRoles().toString(),
						roleAddEvent.getMember().getRoles().size()));

		System.out.println(roleAddEvent.getMember().getUser().getName() + " teve uma role adicionada no servidor "
				+ roleAddEvent.getGuild().getName() + ". Suas roles agora são " + supp.formatarCargos(
						roleAddEvent.getMember().getRoles().toString(), roleAddEvent.getMember().getRoles().size()));

	}

	public void entrouChatVoz() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		if (!voiceChatJoinEvent.getMember().getUser().isBot()) {
			String name = voiceChatJoinEvent.getMember().getUser().getName();
			RoleGrinder registro = new RoleGrinder(voiceChatJoinEvent);
			if (!voiceChatJoinEvent.getMember().getUser().isBot()) {
				registro.salvarPontos();
				registro.alterarModificadoresNovaRoom();
				registro.iniciarContagem();
			}
			log.logWriter(
					dataAtual + name + " se juntou ao chat de voz " + voiceChatJoinEvent.getChannelJoined().getName());
			System.out.println(dataAtual + voiceChatJoinEvent.getMember().getUser().getName()
					+ " se juntou ao chat de voz " + voiceChatJoinEvent.getChannelJoined().getName() + " no servidor "
					+ voiceChatJoinEvent.getGuild().getName());
		}
	}

	public void saiuChatVoz() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		if (!voiceChatLeaveEvent.getMember().getUser().isBot()) {
			String name = voiceChatLeaveEvent.getMember().getUser().getName();
			RoleGrinder registro = new RoleGrinder(voiceChatLeaveEvent);
			registro.salvarPontos();
			registro.alterarModificadoresNovaRoom();

			log.logWriter(dataAtual + name + " saiu do chat de voz " + voiceChatLeaveEvent.getChannelLeft().getName());
			System.out.println(dataAtual + voiceChatLeaveEvent.getMember().getUser().getName() + " saiu do chat de voz "
					+ voiceChatLeaveEvent.getChannelLeft().getName() + " no servidor "
					+ voiceChatLeaveEvent.getGuild().getName());
		}
	}

	public void mudouChatVoz() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		if (!voiceChatMoveEvent.getMember().getUser().isBot()) {
			RoleGrinder registro = new RoleGrinder(voiceChatMoveEvent);
			// if (registro.exists()) {
			registro.salvarPontos();
			registro.alterarModificadoresAntigaRoom();
			registro.alterarModificadoresNovaRoom();
			registro.iniciarContagem();
			// } else {
			// registro.adicionarReg();
			// }
			log.logWriter(dataAtual + voiceChatMoveEvent.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ voiceChatMoveEvent.getChannelJoined().getName());
			System.out.println(dataAtual + voiceChatMoveEvent.getMember().getUser().getName()
					+ " se juntou ao chat de voz " + voiceChatMoveEvent.getChannelJoined().getName() + " no servidor "
					+ voiceChatMoveEvent.getGuild().getName());
		}
	}

	public void mutadoEsurdecido() {
		if (!voiceDeafenEvent.getMember().getUser().isBot()) {
			if (voiceDeafenEvent.getMember().getVoiceState().isSelfDeafened()) {
				// voiceDeafenEvent.getGuild().getController().moveVoiceMember(voiceDeafenEvent.getMember(),
				// voiceDeafenEvent.getGuild().getVoiceChannelById("254091864565612547")).queue();
				voiceDeafenEvent.getGuild().getController()
						.moveVoiceMember(voiceDeafenEvent.getMember(), voiceDeafenEvent.getGuild().getAfkChannel())
						.queue();
			} else {

			}
		}
	}

	private void sendPrivateMessage(User user, String content) {
		user.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(content).queue();
		});
	}

	private void deleteMessage() {
		messageEvent.getChannel().deleteMessageById(messageEvent.getMessageId()).queue();

	}

	public void setNumEpics() {
		int numEpics = Integer.parseInt(messageEvent.getMessage().getContent().split(" ")[1]);
		if (numEpics >= 1 && numEpics <= 5) {
			VariaveisConfiguraveis.numEpics = numEpics;
			messageEvent.getChannel()
					.sendMessage("Setado com sucesso, o numro total de epicos ao atribuir cargos será de "
							+ VariaveisConfiguraveis.numEpics)
					.queue();
		} else {
			messageEvent.getChannel().sendMessage("Valor invalido, para epicos escolha um valos de 1 a 5").queue();
		}
	}

	public void setNumRares() {
		int numRares = Integer.parseInt(messageEvent.getMessage().getContent().split(" ")[1]);
		if (numRares >= 5 && numRares <= 10) {
			VariaveisConfiguraveis.numRares = numRares;
			messageEvent.getChannel()
					.sendMessage("Setado com sucesso, o numro total de raros ao atribuir cargos será de "
							+ VariaveisConfiguraveis.numRares)
					.queue();
		} else {
			messageEvent.getChannel().sendMessage("Valor invalido, para raros escolha um valor de 5 a 10").queue();
		}
	}
}
