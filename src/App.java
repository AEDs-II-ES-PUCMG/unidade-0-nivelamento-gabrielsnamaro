import java.time.LocalDate;

public class App {

	public static void main(String[] args) {
		Produto produto = new ProdutoPerecivel("galao", 100, 0.25, LocalDate.of(2026, 3, 3));

		System.out.println(produto.gerarDadosTexto());
	}
}
