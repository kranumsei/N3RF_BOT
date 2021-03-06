package bot.n3rf;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Connection {

	public static void main(String[] args) {
		JDA discord = null;
		
		try {
			discord = new JDABuilder(AccountType.BOT).setToken(Constants.discordToken).buildBlocking();
		
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		IdentificaEvento resposta = new IdentificaEvento();
		// LookForWrongPlacedCommands watcher = new LookForWrongPlacedCommands();
		discord.addEventListener(resposta);
		// discord.addEventListener(watcher);

	}

}