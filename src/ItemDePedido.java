import java.text.NumberFormat;

public class ItemDePedido {

    // Atributos encapsulados
    private Produto produto;
    private int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = precoVenda;
    }

    public void mesclarItensDePedido(ItemDePedido outro) {
        if(!this.equals(outro))
            throw new IllegalArgumentException("O outro item tem um produto divergente.");
        this.quantidade += outro.quantidade;
        this.precoVenda = this.precoVenda < outro.precoVenda ?
                            this.precoVenda :
                            outro.precoVenda;
    }

    public double calcularSubtotal() {
        return precoVenda * quantidade;
    }

    // --- Sobrescrita do método equals ---

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override
    public boolean equals(Object obj) {

        return produto.equals(obj);
    }

    public String linhaDeRecibo() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();

        return String.format("%s, %s, %s", produto.toString(), moeda.format(precoVenda), moeda.format(calcularSubtotal()));
    }
}
