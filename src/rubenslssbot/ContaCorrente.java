package rubenslssbot;

import rubenslssbot.SaldoInsuficienteException;

public class ContaCorrente extends Conta {

	public ContaCorrente() {
		super();
	}
	
	public ContaCorrente(String titular, Integer numero, String agencia) {
		super(titular, numero, agencia);
	}

	public String getTipo() {
		return "Conta Corrente";
	}

	@Override
	public void saca(double valor) {
		if (valor < 0) {
			throw new IllegalArgumentException("Você tentou sacar um valor negativo");
		}
		if (this.saldo < valor) {
			throw new SaldoInsuficienteException(valor);
		}
		this.saldo -= (valor + 0.10);
	}



}

