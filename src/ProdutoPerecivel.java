import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO  = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, 
                            double precoCusto, 
                            double margemLucro, 
                            LocalDate validade) {
        super(desc, precoCusto, margemLucro);

        if(validade.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("A data de validade já está vencida.");

        this.dataDeValidade = validade;
    }

    public double valorVenda() {
        if(dataDeValidade.isAfter(LocalDate.now()))
            throw new IllegalStateException("Esse produto passou da validade e não pode estar à venda.");

        double valor = precoCusto * (1 + margemLucro);

        return 
            dataDeValidade.isAfter(LocalDate.now().minusDays(PRAZO_DESCONTO)) ?
            valor * (1 - DESCONTO) : 
            valor;
    }


}
