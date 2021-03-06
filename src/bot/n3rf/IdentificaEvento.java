package bot.n3rf;

import java.util.Random;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class IdentificaEvento extends ListenerAdapter {
	
	GuildController gc;
	

	// RESPOSTA A MENSAGENS EM GERAL
	public void onMessageReceived(MessageReceivedEvent e) {
		Functions funcao = new Functions(e);
		String message = e.getMessage().getContent();
		String name = e.getAuthor().getName();
		String response = "";
		if ((message.startsWith("!!!") || message.startsWith("-")) && !e.getTextChannel().getName().equals("commands")) {
		//if(e.getMessage().getAuthor().isBot()) {
			funcao.limparComando();
		} else if (name.equals("Jads1")) {
			Random rand = new Random();
			int rng = rand.nextInt(100 + 1);
			if (rng == 9) {
				response = "Cala boca, Jads!";
				e.getTextChannel().sendMessage(response).queue();
			}
		} else if (message.equals("'flipCoin")) {
			funcao.flipCoin();
		}else if (message.startsWith("'ranking")) {
			funcao.displayRanking();
		} else if (message.startsWith("'whois")) {
			funcao.whoIs();
		}else if (message.equals("'atribuirCargos") && (e.getMember().getRoles().get(0).getName().equals(e.getGuild().getRoles().get(0).getName()) || e.getMember().getRoles().get(0).getName().equals(e.getGuild().getRoles().get(1).getName()))) {
			funcao.atribuirCargos();
		}else if(message.startsWith("'setNumEpics ") && (e.getMember().getRoles().get(0).getName().equals(e.getGuild().getRoles().get(0).getName()) || e.getMember().getRoles().get(0).getName().equals(e.getGuild().getRoles().get(1).getName()))) {
			funcao.setNumEpics();
		}else if(message.startsWith("'setNumRares ") && (e.getMember().getRoles().get(0).getName().equals(e.getGuild().getRoles().get(0).getName()) || e.getMember().getRoles().get(0).getName().equals(e.getGuild().getRoles().get(1).getName()))) {
			funcao.setNumRares();
		}
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		Functions funcao = new Functions(e);
		funcao.entrouNoServer();
	}

	public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
		Functions funcao = new Functions(e);
		funcao.saiuDoServer();
	}

	// ATT LOG E AVISA NO CONSOLE QUANDO ALGUEM TEM CARGO REMOVIDO
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
		if(e.getMember().getUser().isBot() == false) {
			Functions funcao = new Functions(e);
			try {
			funcao.cargoRemovido();
			}catch(StringIndexOutOfBoundsException exception) {
				
			}
		}
	}

	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
		if(e.getMember().getUser().isBot() == false) {
			Functions funcao = new Functions(e);
			funcao.cargoAdicionado();
		}
	}

	// ATT LOG E AVISA NO CONSOLE QUANDO ALGUEM SE CONECTA A UM CHAT DE VOZ NO
	// SERVER
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		Functions funcao = new Functions(e);
		funcao.entrouChatVoz();
	}

	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		Functions funcao = new Functions(e);
		funcao.saiuChatVoz();
	}

	// ATT LOG E AVISA NO CONSOLE QUANDO ALGUEM TROCA DE CHAT DE VOZ
	public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
		Functions funcao = new Functions(e);
		funcao.mudouChatVoz();
	}

	public void onGuildVoiceDeafen(GuildVoiceDeafenEvent e) {
		Functions funcao = new Functions(e);
		funcao.mutadoEsurdecido();
	}
	
}
