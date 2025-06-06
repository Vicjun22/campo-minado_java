package br.com.vicjun22.campominado.model;

import br.com.vicjun22.campominado.exception.ExplosaoException;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    private final int LINHA;
    private final int COLUNA;

    private boolean aberto = false;
    private boolean minado = false;
    private boolean marcado = false;

    private final List<Campo> VIZINHOS = new ArrayList<>();

    public Campo(int linha, int coluna) {
        this.LINHA = linha;
        this.COLUNA = coluna;
    }

    boolean adicionarVizinho(Campo vizinho) {
        boolean linhaDiferente = LINHA != vizinho.LINHA;
        boolean colunaDiferente = COLUNA != vizinho.COLUNA;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(LINHA - vizinho.LINHA);
        int deltaColuna = Math.abs(COLUNA - vizinho.COLUNA);
        int deltaGeral = deltaColuna + deltaLinha;

        if (deltaGeral == 1 && !diagonal) {
            VIZINHOS.add(vizinho);
            return true;
        }
        else if (deltaGeral == 2 && diagonal) {
            VIZINHOS.add(vizinho);
            return true;
        } else {
            return false;
        }
    }

    void alternarMarcacao() {
        if (!aberto) marcado = !marcado;
    }

    boolean abrir() {
        if (!aberto && !marcado) {
            aberto = true;

            if (minado) throw new ExplosaoException();

            if (vizinhancaSegura()) VIZINHOS.forEach(Campo::abrir);

            return true;
        } else return false;
    }

    boolean vizinhancaSegura() {
        return VIZINHOS.stream().noneMatch(v -> v.minado);
    }

    void minar() {
        minado = true;
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    long minasNaVizinhanca() {
        return VIZINHOS.stream().filter(v -> v.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
    }

    public boolean isMinado() {
        return minado;
    }

    public boolean isMarcado() {
        return marcado;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public boolean isAberto() {
        return aberto;
    }

    public int getLinha() {
        return LINHA;
    }

    public int getColuna() {
        return COLUNA;
    }

    public String toString() {
        if (marcado) return "\u001B[33mx";
        else if (aberto && minado) return "*";
        else if (aberto && minasNaVizinhanca() > 0) {
            long minas = minasNaVizinhanca();
            return colorirNumero(minas);
        } else if (aberto) return "\u001B[32m■\u001B[0m";
        else return "?";
    }

    private String colorirNumero(long numero) {
        String cor = switch ((int) numero) {
            case 1 -> "\u001B[34m"; // Azul
            case 2 -> "\u001B[32m"; // Verde
            case 3 -> "\u001B[31m"; // Vermelho
            case 4 -> "\u001B[35m"; // Roxo
            case 5 -> "\u001B[33m"; // Amarelo
            case 6 -> "\u001B[36m"; // Ciano
            case 7 -> "\u001B[90m"; // Cinza claro
            case 8 -> "\u001B[91m"; // Vermelho claro
            default -> "\u001B[0m"; // Reset
        };
        return cor + numero + "\u001B[0m";
    }
}
