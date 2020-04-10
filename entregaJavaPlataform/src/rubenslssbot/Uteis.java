package rubenslssbot;

import java.text.Normalizer;

public class Uteis {

	public static String tratarResposta(String resposta) {
		resposta = resposta.toLowerCase();
	    resposta = Normalizer.normalize(resposta, Normalizer.Form.NFD);
	    resposta = resposta.replaceAll("[^\\p{ASCII}]", "");
	    return resposta;
	}
}
