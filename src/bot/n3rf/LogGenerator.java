package bot.n3rf;

import java.io.BufferedWriter;

//import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;

public class LogGenerator {
	private final String nomeArquivo = Constants.logDirectory;
	//private File log = new File(nomeArquivo);
	private FileWriter fw;
	private BufferedWriter bw;
	public LogGenerator () {
		try {
			fw = new FileWriter(nomeArquivo, true);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logWriter(String linhaLog) {
			try {
				bw = new BufferedWriter(fw);
				bw.write(linhaLog);
				bw.newLine();
				bw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	
	public void closeLog() {
		try {
			this.bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
