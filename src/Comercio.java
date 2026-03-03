import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {
	/** Para inclusão de novos produtos no vetor */
	static final int MAX_NOVOS_PRODUTOS = 10;

	/**
	 * Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto
	 */
	static String nomeArquivoDados;

	/** Scanner para leitura do teclado */
	static Scanner teclado;

	/**
	 * Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a
	 * cada execução
	 */
	static Produto[] produtosCadastrados;

	/** Quantidade produtos cadastrados atualmente no vetor */
	static int quantosProdutos;

	/** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
	static void pausa() {
		System.out.println("Digite enter para continuar...");
		teclado.nextLine();
	}

	/** Cabeçalho principal da CLI do sistema */
	static void cabecalho() {
		System.out.println("AEDII COMÉRCIO DE COISINHAS");
		System.out.println("===========================");
	}

	/**
	 * Imprime o menu principal, lê a opção do usuário e a retorna (int).
	 * Perceba que poderia haver uma melhor modularização com a criação de uma
	 * classe Menu.
	 * 
	 * @return Um inteiro com a opção do usuário.
	 */
	static int menu() {
		cabecalho();
		System.out.println("1 - Listar todos os produtos");
		System.out.println("2 - Procurar e listar um produto");
		System.out.println("3 - Cadastrar novo produto");
		System.out.println("0 - Sair");
		System.out.print("Digite sua opção: ");
		return Integer.parseInt(teclado.nextLine());
	}

	/**
	 * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no
	 * formato
	 * N (quantiade de produtos) <br/>
	 * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
	 * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em
	 * caso de problemas com o arquivo.
	 * 
	 * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
	 * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de
	 *         leitura.
	 */
	static Produto[] lerProdutos(String nomeArquivoDados) {

		File arquivo = new File(nomeArquivoDados);

		try {

			Scanner leitorArquivo = new Scanner(arquivo);
			int quantidadeProdutos = Integer.parseInt(leitorArquivo.nextLine());

			produtosCadastrados = new Produto[quantidadeProdutos + MAX_NOVOS_PRODUTOS];

			quantosProdutos = 0;

			for(int i = 0; i < quantidadeProdutos; i++) {
				produtosCadastrados[i] = Produto.criarDoTexto(leitorArquivo.nextLine());
				quantosProdutos++;
			}

			leitorArquivo.close();

			return produtosCadastrados;

		} catch(FileNotFoundException e) {
			throw new IllegalArgumentException("Arquivo não encontrado para o nome: " + nomeArquivoDados + ". ");
		}
	}

	/** Lista todos os produtos cadastrados, numerados, um por linha */
	static void listarTodosOsProdutos() {
		for(int i = 0; i < quantosProdutos; i++) {
			System.out.println((i + 1) + ". " + produtosCadastrados[i]);
		}
	}

	/**
	 * Localiza um produto no vetor de cadastrados, a partir do nome (descrição), e
	 * imprime seus dados.
	 * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime
	 * mensagem padrão
	 */
	static void localizarProdutos() {
		System.out.print("Insira o nome do produto: ");
		String descricao = teclado.nextLine();

		Produto produtoExemplo = new ProdutoNaoPerecivel(descricao, 0.01);
		Produto produtoEncontrado = null;

		boolean localizado = false;
		for(int i = 0; i < quantosProdutos && !localizado; i++) {
			if(produtosCadastrados[i].equals(produtoExemplo)) {
				produtoEncontrado = produtosCadastrados[i];
				localizado = true;
			}
		}
		
		if(localizado)
			System.out.println("Dados do produto encontrado:\n" + produtoEncontrado.toString());
		else 
			System.out.println("Produto não encontrado.");
	}

	/**
	 * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto,
	 * lê os dados correspondentes,
	 * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método
	 * pode ser feito com um nível muito
	 * melhor de modularização. As diversas fases da lógica poderiam ser
	 * encapsuladas em outros métodos.
	 * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão
	 * Factory Method para criação dos
	 * objetos.
	 */
	static void cadastrarProduto() {
		System.out.println("==== CADASTRAR NOVO PRODUTO ====");
		int tipo = lerInteiro("* Tipo do produto (1: não perecível; 2: perecível): ");
		String descricao = lerString("* Descrição/nome do produto: ");
		double precoCusto = lerDouble("* Insira o preço de custo: ");
		double margemLucro = lerDouble("* Insira a margem de lucro: ");

		switch(tipo) {
			case 1:
				produtosCadastrados[quantosProdutos] = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
				break;
			case 2:
				LocalDate dataValidade = lerData("* Insira a data de validade (dd/mm/aaaa): ");

				produtosCadastrados[quantosProdutos] = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade);
				break;
		}

		quantosProdutos++;
	}

	private static String lerString(String mensagem) {
		System.out.print(mensagem);
		return teclado.nextLine();
	}

	private static int lerInteiro(String mensagem) {
		System.out.print(mensagem);
		return Integer.parseInt(teclado.nextLine());
	}

	private static double lerDouble(String mensagem) {
		System.out.print(mensagem);
		return Double.parseDouble(teclado.nextLine());
	}

	private static LocalDate lerData(String mensagem) {
		System.out.print(mensagem);
		return LocalDate.parse(teclado.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	/**
	 * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve
	 * todo o conteúdo do arquivo.
	 * 
	 * @param nomeArquivo Nome do arquivo a ser gravado.
	 */
	public static void salvarProdutos(String nomeArquivo) {
		try {
			File arquivo = new File(nomeArquivo);
			FileWriter escritor = new FileWriter(arquivo);

			StringBuilder conteudoArquivo = new StringBuilder(quantosProdutos + "\n");

			for(int i = 0; i < quantosProdutos; i++) {
				conteudoArquivo.append(produtosCadastrados[i].gerarDadosTexto() + "\n");
			}

			escritor.write(conteudoArquivo.toString());

			escritor.close();
		} catch (IOException e) {
			System.out.println("Arquivo inexistente!");
		}
	}

	public static void main(String[] args) throws Exception {
		teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
		nomeArquivoDados = "dadosProdutos.csv";
		produtosCadastrados = lerProdutos(nomeArquivoDados);
		int opcao = -1;
		do {
			opcao = menu();
			switch (opcao) {
				case 1 -> listarTodosOsProdutos();
				case 2 -> localizarProdutos();
				case 3 -> cadastrarProduto();
			}
			pausa();
		} while (opcao != 0);
		salvarProdutos(nomeArquivoDados);
		teclado.close();
	}
}