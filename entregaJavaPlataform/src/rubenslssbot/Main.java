package rubenslssbot;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;


public class Main {

		public static void main(String[] args) {
		// Criação do objeto bot com as informações de acesso
		TelegramBot bot = TelegramBotAdapter.build("634932318:AAF5-1Lu0QbX4NoOHwSsPoJtT5PS7mLuQYY");
		// objeto responsável por receber as mensagens
		GetUpdatesResponse updatesResponse;
		// objeto responsável por gerenciar o envio de respostas
		SendResponse sendResponse;
		// objeto responsável por gerenciar o envio de ações do chat
		BaseResponse baseResponse;
		// controle de off-set, isto é, a partir deste ID será lido as mensagens
		// pendentes na fila
		int m = 0;
		
		
		ContaCorrente cc = new ContaCorrente("Rubens", 1234, "0001");
		ContaPoupanca cp = new ContaPoupanca("Rubens", 1234, "0001");
		cc.deposita(1000);
		
		boolean exibeOpcao = false;
		boolean perguntaSaldo = false;
		boolean perguntaDeposito = false;
		boolean perguntaSaque = false;
		boolean perguntaTranferencia = false;
		String operacao = "";
		int opcaoEscolhida = 0;
		
		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {
			// executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set ( limite inicial )
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));
			// lista de mensagens
			List<Update> updates = updatesResponse.updates();
			// análise de cada ação da mensagem
			for (Update update : updates) {
				// atualização do off-set
				m = update.updateId() + 1;
				System.out.println("Recebendo mensagem:" + update.message().text());
				// envio de " Escrevendo " antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				// verificação de ação de chat foi enviada com sucesso
				System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
				// envio da mensagem de resposta

				String mensagem = "Não entendi..., digite menu ou diga a operação que deseja";
				
				String resposta = (update.message().text() == null ? "": update.message().text()) ;
				
				resposta = Uteis.tratarResposta(resposta);

				List< String > saudacao = new ArrayList<>();
				saudacao.add("/start");
				saudacao.add("ola");
				saudacao.add("oi");
				saudacao.add("bem");
				saudacao.add("tarde");
				saudacao.add("noite");
				saudacao.add("dia");
				
				Collections.sort(saudacao);
				
				List< String > despedida = new ArrayList<>();
				despedida.add("tchau");
				despedida.add("adeus");
				despedida.add("fui");
				despedida.add("obrigado");
				despedida.add("logo");					

				Collections.sort(despedida);
				
				List< String > pergunta = new ArrayList<>();
				pergunta.add("quer");
				pergunta.add("gosta");
				pergunta.add("precis");
				pergunta.add("conta");	
				pergunta.add("dinhe");
				pergunta.add("grana");
				pergunta.add("op");
				pergunta.add("men");

				Collections.sort(pergunta);
				
				List< String > saldo = new ArrayList<>();
				saldo.add("sald");
				saldo.add("tot");
				saldo.add("tenho");
				saldo.add("cons");
				saldo.add("ver");		

				Collections.sort(saldo);
				
				List< String > deposito = new ArrayList<>();
				deposito.add("depo");
				deposito.add("coloc");
				deposito.add("inse");
		

				Collections.sort(deposito);
				
				List< String > saque = new ArrayList<>();
				saque.add("sac");
				saque.add("tir");
				saque.add("peg");
		
				Collections.sort(saque);
				
				List< String > transferencia = new ArrayList<>();
				transferencia.add("tran");
				transferencia.add("jog");
				transferencia.add("pass");

				Collections.sort(transferencia);
							
				Map<Integer,String> opcao = new HashMap<Integer,String>();				
				opcao.put(1,"consulta saldo");
				opcao.put(2,"Depósito");
				opcao.put(3,"Saque");
				opcao.put(4,"Transferencia");
				
				Map<Integer,String> tipoConta = new HashMap<Integer,String>();				
				tipoConta.put(1,"conta corrente");
				tipoConta.put(2,"conta poupança");

				boolean achou = false;

				int index;
				Integer valorInt = 0;
				double valorDouble = 0;
				boolean ehNumero;

				StringTokenizer palavras = new StringTokenizer(resposta, " ");
				
				while(palavras.hasMoreTokens()) {
					String palavra = palavras.nextToken();
					
					System.out.println("recebendo a palavra" + palavra);
					
					index = 0;
					index = Collections.binarySearch(saudacao, palavra);
					 
					if (index >= 0) {
						palavra = saudacao.get(index);
						Date data = new Date();
						if (data.getHours() < 12) {
							mensagem = "Bom dia Sr(a),\nbem vindo ao seu banco!!!\nvocê pode chamar o menu ou pedir o que deseja"; 
						} else if (data.getHours() > 18) {
							mensagem = "Bom Noite Sr(a),\nbem vindo ao seu banco!!!\nvocê pode chamar o menu ou pedir o que desejaf"; 
						} else {
							mensagem = "Boa tarde Sr(a),\nbem vindo ao seu banco!!!\nvocê pode chamar o menu ou pedir o que deseja";
						}
						break;
					}
					
					index = 0;
					index = Collections.binarySearch(despedida, palavra);
					 
					if (index >= 0) {
					    palavra = despedida.get(index);
						mensagem = "Até mais, se precisar de algo estamos a disposição";
						break;
					}
					
					if (perguntaSaldo || perguntaDeposito || perguntaSaque || perguntaTranferencia) {
						if (palavra.contains("sim") || palavra.contains("s")){
							if (perguntaSaldo) {
								opcaoEscolhida = 1;
							} else {
								if (perguntaDeposito) {
									opcaoEscolhida = 2;
								} else {
									if (perguntaSaque) {
										opcaoEscolhida = 3;
									} else {
										if (perguntaTranferencia) {
											opcaoEscolhida = 4;
										} else {
											opcaoEscolhida = 0;
										}
									}
								}
							}					
						} else {
							palavra = "opcao";
							
						}
						perguntaSaldo = false;
						perguntaDeposito = false;
						perguntaSaque = false;
						perguntaTranferencia = false;
					}
					

					
					if ((palavra.contains("corren") || palavra.contains("cc") || palavra.contains("c/c")) && opcaoEscolhida != 0) {
						if (opcaoEscolhida == 1) {
							mensagem = "seu saldo é de: " + NumberFormat.getCurrencyInstance().format(cc.getSaldo());
							opcaoEscolhida = 0;
							operacao = "";
							exibeOpcao = false;
							break;	
						}
						if (opcaoEscolhida == 2) {
							mensagem = "quanto você gostaria de depositar?";
							opcaoEscolhida = 0;
							operacao = "pagaValorDepositoCC";
							exibeOpcao = false;
							break;	
						}
						if (opcaoEscolhida == 3) {
							mensagem = "quanto você gostaria de sacar?";
							opcaoEscolhida = 0;
							operacao = "pegaValorSaqueCC";
							exibeOpcao = false;
							break;	
						}
						if (opcaoEscolhida == 4) {
							mensagem = "quanto você gostaria de tranferir da c/c para c/p?";
							opcaoEscolhida = 0;
							operacao = "pegaValorTranferenciaCC";
							exibeOpcao = false;
							break;	
						}
					}
					
					if ((palavra.contains("poupa") || palavra.contains("cp") || palavra.contains("c/p")) && opcaoEscolhida != 0) {
						if (opcaoEscolhida == 1) {
							mensagem = "seu saldo é de: " + NumberFormat.getCurrencyInstance().format(cp.getSaldo());
							opcaoEscolhida = 0;
							operacao = "";
							exibeOpcao = false;
							break;	
						}
						if (opcaoEscolhida == 2) {
							mensagem = "quanto você gostaria de depositar?";
							opcaoEscolhida = 0;
							operacao = "pagaValorDepositoCP"; 
							exibeOpcao = false;
							break;	
						}
						if (opcaoEscolhida == 3) {
							mensagem = "quanto você gostaria de sacar?";
							opcaoEscolhida = 0;
							operacao = "pegaValorSaqueCP";
							exibeOpcao = false;
							break;	
						}
						if (opcaoEscolhida == 4) {
							mensagem = "quanto você gostaria de tranferir da c/p para a c/c?";
							opcaoEscolhida = 0;
							operacao = "pegaValorTranferenciaCP";
							exibeOpcao = false;
							break;	
						}
					}
					
					//double valor = Double.parseDouble(palavra);
					

					
					try {
						valorInt = (Integer.parseInt(palavra));
						ehNumero = true;
					} catch (NumberFormatException e) {
						ehNumero = false;
					}

					if (ehNumero) {
						if (valorInt > 0 && valorInt < 5) {
							opcaoEscolhida = valorInt;
						}
					}
					
				

					
					if (operacao != "") {
						
						try {
							System.out.println("<recebendo valor " + palavra +">");	
							String valorString = palavra.replace("(?:[^\\d\\,])", "")
									.replace("r", "")
									.replace("$", "")
									.replace(".", "")
									.replace(",", ".");
							System.out.println("<recebendo valor " + valorString +">");	
							valorDouble = Double.parseDouble(valorString);
							System.out.println("<recebendo valor " + valorDouble +">");							
							ehNumero = true;
						} catch (NumberFormatException e) {
							ehNumero = false;
						}

						if (ehNumero) {
							if (valorDouble > 0) {
								switch (operacao) {
								case "pagaValorDepositoCC":
									try {
										cc.deposita(valorDouble);
										mensagem = "depositado o valor de : " + NumberFormat.getCurrencyInstance().format(valorDouble);
									} catch (Exception e) {
										mensagem = e.toString();
									}
									break;
								case "pegaValorSaqueCC":
									try {
										cc.saca(valorDouble);
										mensagem = "sacado o valor de : " + NumberFormat.getCurrencyInstance().format(valorDouble);
									} catch (SaldoInsuficienteException e) {
										mensagem = "seu saldo é insuficiente";
									}	
									
									break;								
								case "pegaValorTranferenciaCC":
									try {
										cc.transfere(valorDouble, cp);
										mensagem = "tranferido valor de : " + NumberFormat.getCurrencyInstance().format(valorDouble);
									} catch (Exception e) {
										mensagem = e.toString();
									}
									break;	
								case "pagaValorDepositoCP":
									try {
										cp.deposita(valorDouble);
										mensagem = "depositado o valor de : " + NumberFormat.getCurrencyInstance().format(valorDouble);
									} catch (Exception e) {
										mensagem = e.toString();
									}
									break;
								case "pegaValorSaqueCP":		
									try {
										cp.saca(valorDouble);
										mensagem = "sacado o valor de : " + NumberFormat.getCurrencyInstance().format(valorDouble);
									} catch (Exception e) {
										mensagem = e.toString();
									}										
									break;								
								case "pegaValorTranferenciaCP":
									try {
										cp.transfere(valorDouble, cc);
										mensagem = "tranferido o valor de : " + NumberFormat.getCurrencyInstance().format(valorDouble);
									} catch (Exception e) {
										mensagem = e.toString();
									}
									break;
								default:
									mensagem = "valor inválido";
									break;
								}		
								operacao = "";
							} else {
								mensagem = "valor inválido";
								//operacao = "";
							} 
						} else {
							mensagem = "valor inválido";
							//operacao = "";
						}
						
					}	           



					
					
					if (palavra.indexOf("sald") >= 0) {
						palavra = "sald";
					}
					if (palavra.indexOf("tot") >= 0) {
						palavra = "tot";
					}
					if (palavra.indexOf("cons") >= 0) {
						palavra = "cons";
					}
					if (palavra.indexOf("depo") >= 0) {
						palavra = "depo";
					}
					if (palavra.indexOf("coloc") >= 0) {
						palavra = "coloc";
					}
					if (palavra.indexOf("inse") >= 0) {
						palavra = "inse";
					}
					
					if (palavra.indexOf("sac") >= 0) {
						palavra = "sac";
					}
					if (palavra.indexOf("saq") >= 0) {
						palavra = "sac";
					}
					if (palavra.indexOf("tir") >= 0) {
						palavra = "tir";
					}
					if (palavra.indexOf("peg") >= 0) {
						palavra = "peg";
					}
					if (palavra.indexOf("tran") >= 0) {
						palavra = "tran";
					}
					if (palavra.indexOf("jog") >= 0) {
						palavra = "jog";
					}
					if (palavra.indexOf("pass") >= 0) {
						palavra = "pass";
					}
					
					
					index = 0;
					index = Collections.binarySearch(saldo, palavra);
					 
					if (index >= 0) {
					    palavra = saldo.get(index);
						mensagem = "você gostaria de consultar seu saldo? sim ou não.";
						perguntaSaldo = true;
						exibeOpcao = false;
						break;
					}

					index = 0;
					index = Collections.binarySearch(saque, palavra);
					
					if (index >= 0) {
					    palavra = saque.get(index);
						mensagem = "você gostaria de realizar um saque? sim ou não.";
						perguntaSaque = true;
						exibeOpcao = false;
						break;
					}
					
					index = 0;
					index = Collections.binarySearch(deposito, palavra);
					
					if (index >= 0) {
					    palavra = deposito.get(index);
						mensagem = "você gostaria de realizar um depósito? 'sim' ou 'não'.";
						perguntaDeposito = true;
						exibeOpcao = false;
						break;
					}
					
					index = 0;
					index = Collections.binarySearch(transferencia, palavra);
					
					if (index >= 0) {
					    palavra = transferencia.get(index);
						mensagem = "você gostaria de realizar uma transferência? sim ou não.";
						perguntaTranferencia = true;
						exibeOpcao = false;
						break;
					}
					
					if (opcaoEscolhida > 0 && opcaoEscolhida < 5 && exibeOpcao == false && !palavra.contains("conta")) {
						mensagem = "você gostaria de acessar conta corrente ou poupança";
						exibeOpcao = false;
						break;						
					}
					
					if (palavra.indexOf("quer") >= 0) {
						palavra = "quer";
					}
					if (palavra.indexOf("gosta") >= 0) {
						palavra = "gosta";
					}
					if (palavra.indexOf("precis") >= 0) {
						palavra = "precis";
					}
					if (palavra.indexOf("conta") >= 0) {
						palavra = "conta";
					}
					if (palavra.indexOf("dinhe") >= 0) {
						palavra = "dinhe";
					}
					if (palavra.indexOf("grana") >= 0) {
						palavra = "grana";
					}
					if (palavra.indexOf("op") >= 0) {
						palavra = "op";
					}
					if (palavra.indexOf("men") >= 0) {
						palavra = "men";
					}
					
					index = 0;
					index = Collections.binarySearch(pergunta, palavra);
					
					if (index >= 0) {
					    palavra = pergunta.get(index);
						mensagem = "Nós temos as seguintes opções:";
						exibeOpcao = true;
					}
				}
	
				
				sendResponse = bot.execute(new SendMessage(update.message().chat().id(), mensagem));
				// verificação de mensagem enviada com sucesso
				System.out.println("Mensagem Enviada?" + sendResponse.isOk()); //confirma
				
				
				//exibir menu
				
				if(exibeOpcao) {
					exibeOpcao = false;
					for (Integer key : opcao.keySet()) {
						String value = opcao.get(key);
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), (key + " - para " + value)));
					}
				}				
				
			}
		}}

}




//Map<String, String> example = new HashMap<String, String>();
//
//example.put("K1", new String("V1"));
//example.put("K2", new String("V2"));
//example.put("K3", new String("V3"));
//example.put("K4", new String("V4"));
//example.put("K5", new String("V5"));
//
///*
// * O método "keySet()" retorna um Set com todas as chaves do
// * nosso HashMap, e tendo o Set com todas as Chaves, 
//  * podemos facilmente pegar
// * os valores que desejamos
// * */
// 
//for (String key : example.keySet()) {
//        
//       //Capturamos o valor a partir da chave
//       String value = example.get(key);
//       System.out.println(key + " = " + value);
//}
//
//}
