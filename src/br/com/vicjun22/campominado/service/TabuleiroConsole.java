package br.com.vicjun22.campominado.service;

import br.com.vicjun22.campominado.exception.ExplosaoException;
import br.com.vicjun22.campominado.exception.SairException;
import br.com.vicjun22.campominado.model.Tabuleiro;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class TabuleiroConsole {

    private Tabuleiro tabuleiro;
    private Scanner scanner = new Scanner(System.in);

    public TabuleiroConsole(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;

        executarJogo();
    }

    private void executarJogo() {
        try {
            boolean continuar = true;

            while (continuar) {
                cicloDoJogo();

                System.out.println("Deseja começar uma nova partida? (S/n) ");
                String resposta = scanner.nextLine();

                if ("n".equalsIgnoreCase(resposta)) continuar = false;
                else tabuleiro.reiniciar();
            }

        } catch (SairException e) {
            System.out.println("Até logo!");
        } finally {
            scanner.close();
        }
    }

    private void cicloDoJogo() {
        try {
            while (!tabuleiro.objetivoAlcancado()) {
                System.out.println(tabuleiro.toString());
                String digitado = capturarValorDigitado("Digite (x, y): ");

                Iterator<Integer> xy = Arrays.stream(digitado.split(","))
                        .map(e -> Integer.parseInt(e.trim())).iterator();

                digitado = capturarValorDigitado("1 - Abrir ou 2 - (Des)Marcar: ");

                if ("1".equalsIgnoreCase(digitado)) tabuleiro.abrir(xy.next(), xy.next());
                else if ("2".equalsIgnoreCase(digitado)) tabuleiro.alterarMarcacao(xy.next(), xy.next());
            }

            System.out.println(tabuleiro.toString());
            System.out.println("Você ganhou!");
        } catch (ExplosaoException e) {
            System.out.println(tabuleiro.toString());
            System.out.println("Você perdeu!");
        }
    }

    private String capturarValorDigitado(String texto) {
        System.out.print(texto);
        String digitado = scanner.nextLine();

        if ("sair".equalsIgnoreCase(digitado)) {
            throw new SairException();
        }

        return digitado;
    }

}
