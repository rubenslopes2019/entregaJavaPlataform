package rubenslssbot;

public abstract class Conta implements Comparable < Conta > {

	protected double saldo;
	public String titular;
	public Integer numero;
	public String agencia;

	public Conta() {
	}
	
	public Conta(String titular, Integer numero, String agencia) {
		this.titular = titular;
		this.numero = numero;
		this.agencia = agencia;
	}

	public abstract String getTipo();
	// {return "Conta";}

	public void saca(double valor) {
		this.saldo -= valor;

	}

	public void transfere(double valor, Conta conta) {
		this.saca(valor);
		conta.deposita(valor);
	}

	public void deposita(double valor) {
		if (valor < 0) {
			throw new IllegalArgumentException("Você tentou depositar um valor negativo");
		} else {
			this.saldo += valor;
		}
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "[titular=" + titular + ", numero=" + numero + ", agencia=" + agencia + "]";
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	
	public int compareTo (Conta outraConta) {
		return this .titular.compareTo(outraConta.titular);
		}
	
	

}
