package bot.n3rf;

import java.util.Calendar;
import java.util.Random;

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
	//private String name = messageEvent.getAuthor().getName();
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
		messageEvent.getTextChannel().sendMessage(rg.ranking()).queue();
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
		log.logWriter("\n" + dataAtual + guildJoinEvent.getUser().getName() + " entrou no server "
				+ messageEvent.getGuild().getName() + " em " + messageEvent.getMember().getJoinDate() + "\n");

		messageEvent.getGuild().getController()
				.addRolesToMember(messageEvent.getMember(), messageEvent.getGuild().getRolesByName("Common", true))
				.queue();

		System.out.println("\n" + guildJoinEvent.getUser().getName() + " entrou no server "
				+ messageEvent.getGuild().getName() + "\n");
		sendPrivateMessage(guildJoinEvent.getUser(),
				"Bem-vindo ao " + messageEvent.getGuild().getName() + ". Duvidas? Fale com algum moderador.");
		sendPrivateMessage(messageEvent.getGuild().getOwner().getUser(), messageEvent.getMember().getUser().getName()
				+ " entrou no server " + messageEvent.getGuild().getName());
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
		log.logWriter(dataAtual + roleAddEvent.getMember().getUser().getName() + " teve uma role adicionada no servidor "
				+ roleAddEvent.getGuild().getName() + ". Suas roles agora são "
				+ supp.formatarCargos(roleAddEvent.getMember().getRoles().toString(), roleAddEvent.getMember().getRoles().size()));

		System.out.println(roleAddEvent.getMember().getUser().getName() + " teve uma role adicionada no servidor "
				+ roleAddEvent.getGuild().getName() + ". Suas roles agora são "
				+ supp.formatarCargos(roleAddEvent.getMember().getRoles().toString(), roleAddEvent.getMember().getRoles().size()));
	}
	
	public void entrouChatVoz() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		if (!voiceChatJoinEvent.getMember().getUser().isBot()) {
			String name = voiceChatJoinEvent.getMember().getUser().getName();
			RoleGrinder registro = new RoleGrinder(voiceChatJoinEvent);
			if (registro.exists()) {
				registro.alterarJoin();
			} else {
				registro.adicionarReg();
			}

			log.logWriter(dataAtual + name + " se juntou ao chat de voz " + voiceChatJoinEvent.getChannelJoined().getName());
			System.out.println(dataAtual + voiceChatJoinEvent.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ voiceChatJoinEvent.getChannelJoined().getName() + " no servidor " + voiceChatJoinEvent.getGuild().getName());
		}
	}

	public void saiuChatVoz() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		if (!voiceChatLeaveEvent.getMember().getUser().isBot()) {
			String name = voiceChatLeaveEvent.getMember().getUser().getName();
			RoleGrinder registro = new RoleGrinder(voiceChatLeaveEvent);
			if (registro.exists()) {
				registro.alterarLeave();
			} else {
				registro.adicionarReg();
			}

			log.logWriter(dataAtual + name + " saiu do chat de voz " + voiceChatLeaveEvent.getChannelLeft().getName());
			System.out.println(dataAtual + voiceChatLeaveEvent.getMember().getUser().getName() + " saiu do chat de voz "
					+ voiceChatLeaveEvent.getChannelLeft().getName() + " no servidor " + voiceChatLeaveEvent.getGuild().getName());
		}
	}
	
	public void mudouChatVoz() {
		calendar = Calendar.getInstance();
		String dataAtual = "(" + calendar.getTime() + ") ";
		if (!voiceChatMoveEvent.getMember().getUser().isBot()) {
			log.logWriter(dataAtual + voiceChatMoveEvent.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ voiceChatMoveEvent.getChannelJoined().getName());
			System.out.println(dataAtual + voiceChatMoveEvent.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ voiceChatMoveEvent.getChannelJoined().getName() + " no servidor " + voiceChatMoveEvent.getGuild().getName());
		}
	}

	public void mutadoEsurdecido() {
		if (!voiceDeafenEvent.getMember().getUser().isBot()) {
			if (voiceDeafenEvent.getMember().getVoiceState().isSelfDeafened()) {
				voiceDeafenEvent.getGuild().getController()
						.moveVoiceMember(voiceDeafenEvent.getMember(), voiceDeafenEvent.getGuild().getVoiceChannelById("254091864565612547")).queue();
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
}
