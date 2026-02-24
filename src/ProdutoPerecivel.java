import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {

    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO  = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, 
                            double precoCusto, 
                            double margemLucro, 
                            LocalDate validade) {
        super(desc, precoCusto, margemLucro);

        if(validade.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("A data de validade já está vencida.");

        this.dataDeValidade = validade;
    }

    @Override
    public double valorVenda() {
        if(dataDeValidade.isBefore(LocalDate.now()))
            throw new IllegalStateException("Esse produto passou da validade e não pode estar à venda.");

        double valor = precoCusto * (1 + margemLucro);

        return 
            LocalDate.now().isAfter(dataDeValidade.minusDays(PRAZO_DESCONTO)) ?
            valor * (1 - DESCONTO) : 
            valor;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/mm/yyyy");

        StringBuilder builder = new StringBuilder(super.toString());
        builder.append("\nVálido até " + formatoData);

        return builder.toString();
    }


}
