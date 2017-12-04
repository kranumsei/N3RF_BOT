package bot.n3rf;



public class Support {
	public String reverteData(String s) {
		StringBuilder data = new StringBuilder();
		String year, month, day;
		year = s.substring(0, 4);
		month = s.substring(5, 7);
		day = s.substring(8, 10);		
		data.append(day+"/"+month+"/"+year);
		return data.toString();
	}

	
	public String formatarCargos(String s, int numeroCargos) {
		StringBuilder retorno = new StringBuilder();
		String[] temp = s.split(",", numeroCargos);
		
		for(int i = 0; i < temp.length; i++) {
			temp[i] = temp[i].substring(3);	
			String[] temp2  = temp[i].split("\\(");
			temp[i] = temp2[0];
		}
		
		for(int i = 0; i < numeroCargos; i++) {
			retorno.append(temp[i]+" -|- ");
		}
		
		return retorno.toString();
	}
}
