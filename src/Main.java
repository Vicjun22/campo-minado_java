import br.com.vicjun22.campominado.model.Tabuleiro;
import br.com.vicjun22.campominado.service.TabuleiroConsole;

public class Main {
    public static void main(String[] args) {

        Tabuleiro tabuleiro = new Tabuleiro(6, 6, 6);
        new TabuleiroConsole(tabuleiro);
    }
}