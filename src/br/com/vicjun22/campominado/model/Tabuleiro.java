package br.com.vicjun22.campominado.model;

import br.com.vicjun22.campominado.exception.ExplosaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {

    private final int LINHAS;
    private final int COLUNAS;
    private final int MINAS;

    private final List<Campo> CAMPOS = new ArrayList<>();

    public Tabuleiro(int linhas, int colunas, int minas) {
        this.LINHAS = linhas;
        this.COLUNAS = colunas;
        this.MINAS = minas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void abrir(int linha, int coluna) {
        try {
            CAMPOS.parallelStream()
                    .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                    .findFirst()
                    .ifPresent(Campo::abrir);
        } catch (ExplosaoException e) {
            CAMPOS.forEach(c -> c.setAberto(true));
            throw e;
        }
    }

    public void alterarMarcacao(int linha, int coluna) {
        CAMPOS.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(Campo::alternarMarcacao);
    }

    private void gerarCampos() {
        for (int linha = 0; linha < LINHAS; linha++) {
            for (int coluna = 0; coluna < COLUNAS; coluna++) {
                CAMPOS.add(new Campo(linha, coluna));
            }
        }
    }

    private void associarVizinhos() {
        for (Campo c1: CAMPOS) {
            for (Campo c2: CAMPOS) {
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0L;
        Predicate<Campo> minado = Campo::isMinado;

        do {
            int aleatorio = (int) (Math.random() * CAMPOS.size());
            CAMPOS.get(aleatorio).minar();
            minasArmadas = CAMPOS.stream().filter(minado).count();
        } while (minasArmadas < MINAS);
    }

    public boolean objetivoAlcancado() {
        return CAMPOS.stream().allMatch(Campo::objetivoAlcancado);
    }

    public void reiniciar() {
        CAMPOS.forEach(Campo::reiniciar);
        sortearMinas();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("  ");
        for (int c = 0; c < COLUNAS; c++) {
            sb.append(" ");
            sb.append(c);
            sb.append(" ");
        }
        sb.append("\n\n");

        int indice = 0;

        for (int linha = 0; linha < LINHAS; linha++) {
            sb.append(linha);
            sb.append(" ");
            for (int coluna = 0; coluna < COLUNAS; coluna++) {
                sb.append(" ");
                sb.append(CAMPOS.get(indice));
                sb.append(" ");
                indice++;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
