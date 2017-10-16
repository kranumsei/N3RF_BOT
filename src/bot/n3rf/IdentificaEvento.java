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
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import role.season.RoleGrinder;

public class IdentificaEvento extends ListenerAdapter {
	Calendar calendar = Calendar.getInstance();
	String dataAtual = "(" + calendar.getTime() + ") ";
	LogGenerator log = new LogGenerator();
	GuildController gc;
	Support supp = new Support();
	
	// RESPOSTA A MENSAGENS EM GERAL
	public void onMessageReceived(MessageReceivedEvent e){
		String message = e.getMessage().getContent();
		String name = e.getAuthor().getName();
		String response = "";
		Random rand = new Random();
		int numMembros;

		// ESSE IF BLOQUEIA COMANDO NO CHAT GENERAL
		if ((message.startsWith("!!!") || message.startsWith("-") || message.startsWith("'"))
				&& !(e.getTextChannel().getName().equals("commands"))) {
			deleteMessage(e);
			sendPrivateMessage(e.getAuthor(),
					"Comandos só podem ser feitos na aba 'commands', fale com o admin para conseguir acesso.");
			System.out.println(e.getAuthor().getName() + " tentou dar comandos em um chat que nao era o 'Commands'");
			log.logWriter(
					dataAtual + e.getAuthor().getName() + " tentou dar comandos em um chat que nao era o 'Commands'");

		}else if (name.equals("Jads1")) {
			int rng = rand.nextInt(100 + 1);
			if (rng == 9) {
				response = "CALA BOCA GORDÃO";
				e.getTextChannel().sendMessage(response).queue();
			}
//		} else if (message.startsWith("jads é oq?") || message.startsWith("Jads é oq?")) {
//			int rng = rand.nextInt(4 + 1);
//			if (rng == 1) {
//				response = response + "Jads é um gordo";
//			} else if (rng == 2) {
//				response = response + "Jads é gente gorda pra caralho";
//			} else if (rng == 3) {
//				response = response + "Jads é o Alaric";
//			} else {
//				response = response
//						+ "Para de tentar zoar o jads ai seu porra, viado é você "+e.getAuthor().getName();
//			}
//			e.getTextChannel().sendMessage(response).queue();
//		}
			else if(message.startsWith("'whois ")) {
			numMembros = e.getGuild().getMembers().size();
			for(int i = 0; i < numMembros; i++) {
				User usuarioAlvo = e.getGuild().getMembers().get(i).getUser();
				if(message.contains(usuarioAlvo.getName())) {
					String data = supp.reverteData(e.getGuild().getMembers().get(i).getJoinDate().toString().substring(0, 10));
					String cargoLista = supp.formatarCargos(e.getGuild().getMembers().get(i).getRoles().toString(), e.getGuild().getMembers().get(i).getRoles().size());
					log.logWriter("WhoIs executado para "+usuarioAlvo.getName());
					System.out.println("WhoIs executado para"+usuarioAlvo.getName());
					System.out.println(cargoLista);
					e.getTextChannel().sendMessage(usuarioAlvo.getName()+":\n Status: "+e.getGuild().getMembers().get(i).getOnlineStatus()+"\n Cargos: "+cargoLista+"\n Membro desde: "+data).queue();
				}
				
			}
			
			}
		}
		//else if (message.startsWith("Romero ta me perguntando sobre a festa no grupo la, pode responder por mim pfv? ngm me conhece melhor que vc.")){
//			e.getTextChannel().sendMessage("Hmm, assim como você eu também não gosto muito de festas como essa, principalmente as que tocam esse sertanejo fodido. Porem creio que você queira ir pelos seus amigos... Espere e veja o que acontece daqui pra la, entao poderás decidir.").queue();
//		}
	}

	public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		log.logWriter("\n"+dataAtual + e.getUser().getName() + " entrou no server " + e.getGuild().getName() + " em "
				+ e.getMember().getJoinDate()+"\n");
		
		e.getGuild().getController().addRolesToMember(e.getMember(), e.getGuild().getRolesByName("Common", true)).queue();
		
		System.out.println("\n"+e.getUser().getName() + " entrou no server " + e.getGuild().getName()+"\n");
		sendPrivateMessage(e.getUser(),
				"Bem-vindo ao " + e.getGuild().getName() + ". Duvidas? Fale com algum moderador.");
		sendPrivateMessage(e.getGuild().getOwner().getUser(),
				e.getMember().getUser().getName() + " entrou no server " + e.getGuild().getName());
	}

	public void onGuildMemberLeave(GuildMemberLeaveEvent e) {
		log.logWriter("\n"+dataAtual + "Usuario " + e.getMember().getUser().getName() + " saiu do server "
				+ e.getGuild().getName()+"\n");
		System.out
				.println("\nUsuario " + e.getMember().getUser().getName() + " saiu do server " + e.getGuild().getName()+"\n");
		sendPrivateMessage(e.getGuild().getOwner().getUser(), e.getMember().getUser().getName()
				+ " era parte do server desde " + e.getMember().getJoinDate().toString() + " mas saiu do server.");
	}

	// ATT LOG E AVISA NO CONSOLE QUANDO ALGUEM TEM CARGO REMOVIDO
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
		log.logWriter(dataAtual + e.getMember().getUser().getName() + " teve uma role removida no servidor "
				+ e.getGuild().getName() + ". Suas roles agora são " + supp.formatarCargos(e.getMember().getRoles().toString(), e.getMember().getRoles().size()));

		System.out.println(e.getMember().getUser().getName() + " teve uma role removida no sevidor "
				+ e.getGuild().getName() + ". Suas roles agora são " + supp.formatarCargos(e.getMember().getRoles().toString(), e.getMember().getRoles().size()));
	}

	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
		log.logWriter(dataAtual + e.getMember().getUser().getName() + " teve uma role adicionada no servidor "
				+ e.getGuild().getName() + ". Suas roles agora são " + supp.formatarCargos(e.getMember().getRoles().toString(), e.getMember().getRoles().size()));

		System.out.println(e.getMember().getUser().getName() + " teve uma role adicionada no servidor "
				+ e.getGuild().getName() + ". Suas roles agora são " + supp.formatarCargos(e.getMember().getRoles().toString(), e.getMember().getRoles().size()));
	}

	// ATT LOG E AVISA NO CONSOLE QUANDO ALGUEM SE CONECTA A UM CHAT DE VOZ NO
	// SERVER
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		if(!e.getMember().getUser().isBot()) {
			String name = e.getMember().getUser().getName();
			RoleGrinder registro = new RoleGrinder(e);			
			if(registro.exists()) {
				registro.alterarJoin();
			}else {
				registro.adicionarReg();
			}

			log.logWriter(dataAtual + name + " se juntou ao chat de voz "
					+ e.getChannelJoined().getName());
			System.out.println(dataAtual + e.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ e.getChannelJoined().getName() + " no servidor " + e.getGuild().getName());
		}
	}
	
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		if(!e.getMember().getUser().isBot()) {
			String name = e.getMember().getUser().getName();
			RoleGrinder registro = new RoleGrinder(e);		
			if(registro.exists()) {
				registro.alterarLeave();
			}else {
				registro.adicionarReg();
			}

			log.logWriter(dataAtual + name + " saiu do chat de voz "
					+ e.getChannelLeft().getName());
			System.out.println(dataAtual + e.getMember().getUser().getName() + " saiu do chat de voz "
					+ e.getChannelLeft().getName() + " no servidor " + e.getGuild().getName());
		}
	}
	

	// ATT LOG E AVISA NO CONSOLE QUANDO ALGUEM TROCA DE CHAT DE VOZ
	public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
		if(!e.getMember().getUser().isBot()) {
			log.logWriter(dataAtual + e.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ e.getChannelJoined().getName());
			System.out.println(dataAtual + e.getMember().getUser().getName() + " se juntou ao chat de voz "
					+ e.getChannelJoined().getName() + " no servidor " + e.getGuild().getName());
		}
	}

	// AVISA EM PRIVATE QUANDO ALGUEM É MUTADO
	// public void onGuildVoiceMute(GuildVoiceMuteEvent e) {
	// if(e.isMuted()) {
	// sendPrivateMessage(e.getMember().getUser(), "Você está mutado para o servidor
	// "+e.getGuild().getName());
	// }else {}
	// }

	public void onGuildVoiceDeafen(GuildVoiceDeafenEvent e) {
		if(!e.getMember().getUser().isBot()) {	
			if (e.getMember().getVoiceState().isSelfDeafened()) {
				e.getGuild().getController()
						.moveVoiceMember(e.getMember(), e.getGuild().getVoiceChannelById("254091864565612547")).queue();
			} else {
				
			}
		}
	}
	/*
	 * public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent e) {
	 * 
	 * }
	 */

	// ENVIA MSG PRIVADA
	public void sendPrivateMessage(User user, String content) {

		user.openPrivateChannel().queue((channel) -> {
			channel.sendMessage(content).queue();
		});
	}

	// MSGS PRO JADS, METODO CHAMADO NO "onMessageReceived"
	// public void questions(String message, MessageReceivedEvent e) {
	// String response = "";
	//
	// if (message.startsWith("jads é oq?") || message.startsWith("Jads é oq?")) {
	// Random rand = new Random();
	// int rng = rand.nextInt(4 + 1);
	// if (rng == 1) {
	// response = response + "Jads é um gordão";
	// } else if (rng == 2) {
	// response = response + "Jads é burro pra caralho";
	// } else if (rng == 3) {
	// response = response + "Jads não é o Alaric (que por sinal é gente boa pra
	// caralho)";
	// } else {
	// response = response
	// + "Jads é um arrombado, escroto, filho de uma cadela arrombada, que é pau no
	// cu, nega core e é arrombado";
	// }
	// e.getTextChannel().sendMessage(response).queue();
	// }
	// }

	public void deleteMessage(MessageReceivedEvent e) {
		e.getChannel().deleteMessageById(e.getMessageId()).queue();

	}
}
