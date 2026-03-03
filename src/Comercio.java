import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.time.LocalDate;
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
		/*
		 * Implementar a sub-rotina de exibir o novo menu para cadastro de novo produto,
		 * ler os dados necessários
		 * conforme o tipo desejado, criar o objeto correspondente, salvando-o no vetor
		 * de produtosCadastrados e
		 * incrementando a variável de controle da quantidade de produtos.
		 */
	}

	/**
	 * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve
	 * todo o conteúdo do arquivo.
	 * 
	 * @param nomeArquivo Nome do arquivo a ser gravado.
	 */
	public static void salvarProdutos(String nomeArquivo) {
		/*
		 * Você deve implementar aqui a lógica que abrirá um arquivo para escrita com o
		 * nome informado no
		 * parâmetro, percorrerá um por um todos os produtos existentes no vetor de
		 * produtosCadastrados, gerando
		 * uma linha de texto com os dados de cada objeto Produto, escrevendo-a no
		 * arquivo.
		 */
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