package rubenslssbot;

public class ContaPoupanca extends Conta {

	
	public ContaPoupanca() {
		super();
	}

	public ContaPoupanca(String titular, Integer numero, String agencia) {
		super(titular, numero, agencia);
	}

	public String getTipo() {
		return "Conta Poupança";
	}

}

