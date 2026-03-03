import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Arrays;

public abstract class Produto {
	
	private static final double MARGEM_PADRAO = 0.2;
	protected String descricao;
	protected double precoCusto;
	protected double margemLucro;
	
	/**
     * Inicializador privado. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	private void init(String desc, double precoCusto, double margemLucro) {
		
		if ((desc.length() >= 3) && (precoCusto > 0.0) && (margemLucro > 0.0)) {
			descricao = desc;
			this.precoCusto = precoCusto;
			this.margemLucro = margemLucro;
		} else {
			throw new IllegalArgumentException("Valores inválidos para os dados do produto.");
		}
	}
	
	/**
     * Construtor completo. Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00, 0.0  
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     * @param margemLucro Margem de lucro (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto, double margemLucro) {
		init(desc, precoCusto, margemLucro);
	}
	
	/**
     * Construtor sem margem de lucro - fica considerado o valor padrão de margem de lucro.
     * Os valores default, em caso de erro, são:
     * "Produto sem descrição", R$ 0.00 
     * @param desc Descrição do produto (mínimo de 3 caracteres)
     * @param precoCusto Preço do produto (mínimo 0.01)
     */
	public Produto(String desc, double precoCusto) {
		init(desc, precoCusto, MARGEM_PADRAO);
	}
	
	/**
     * Retorna o valor de venda do produto, considerando seu preço de custo e margem de lucro.
     * @return Valor de venda do produto (double, positivo)
     */
	public double valorVenda() {
		return (precoCusto * (1.0 + margemLucro));
	}

	/**
	 * Igualdade de produtos: caso possuam o mesmo nome/descrição.
	 * @param obj Outro produto a ser comparado
	 * @return booleano true/false conforme o parâmetro possua a descrição igual ou não a este produto.
	 */	
	@Override
	public boolean equals(Object obj) {
		Produto outro = (Produto)obj;
		return this.descricao.toLowerCase().equals(outro.descricao.toLowerCase());
	}

	/**
	 * Gera uma linha de texto a partir dos dados do produto
	 * @return Uma string no formato "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	 */
	public abstract String gerarDadosTexto();
	
	/**
	 * Cria um produto a partir de uma linha de dados em formato texto. A linha de dados deve estar de acordo com a formatação
	 * "tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade]"
	 * ou o funcionamento não será garantido. Os tipos são 1 para produto não perecível e 2 para perecível.
	 * @param linha Linha com os dados do produto a ser criado.
	 * @return Um produto com os dados recebidos
	 */
	public static Produto criarDoTexto(String linha){
		Produto novoProduto;
		String[] dadosProduto = linha.split(";");
		int tipoProduto = Integer.parseInt(dadosProduto[0]);

		switch(tipoProduto) {
			case 1:
				novoProduto = criarNaoPerecivel(dadosProduto);
				break;
			case 2:
				novoProduto = criarPerecivel(dadosProduto);
				break;
			default:
				throw new IllegalArgumentException("Tipo do produto é inválido. Deve ser exclusivamente 1 (não perecível) ou 2 (perecível).");
		}

		return novoProduto;
	}

	private static ProdutoNaoPerecivel criarNaoPerecivel(String[] dados) {
		return new ProdutoNaoPerecivel(
			dados[1],
			Double.parseDouble(dados[2]),
			Double.parseDouble(dados[3])
		);
	}

	private static ProdutoPerecivel criarPerecivel(String[] dados) {
		String[] dataDividida = dados[4].split("/");

		LocalDate validade = LocalDate.of(
			Integer.parseInt(dataDividida[2]), 
			Integer.parseInt(dataDividida[1]), 
			Integer.parseInt(dataDividida[0])
		);

		return new ProdutoPerecivel(
			dados[1],
			Double.parseDouble(dados[2]),
			Double.parseDouble(dados[3]),
			validade
		);
	}

	/**
     * Descrição, em string, do produto, contendo sua descrição e o valor de venda.
     *  @return String com o formato:
     * [NOME]: R$ [VALOR DE VENDA]
     */
    @Override
	public String toString() {
    	
    	NumberFormat moeda = NumberFormat.getCurrencyInstance();
    	
		return String.format("NOME: " + descricao + ": " + moeda.format(valorVenda()));
	}
}