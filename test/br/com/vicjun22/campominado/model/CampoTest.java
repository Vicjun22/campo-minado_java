package br.com.vicjun22.campominado.model;

import br.com.vicjun22.campominado.exception.ExplosaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CampoTest {

    private Campo campo;

    @BeforeEach
    void iniciarCampo() {
        campo = new Campo(3, 3);
    }

    @Test
    void vizinhoComDistancia1EsquerdaTest() {
        Campo vizinho = new Campo(3, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia1DireitaTest() {
        Campo vizinho = new Campo(3, 4);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia1EmCimaTest() {
        Campo vizinho = new Campo(2, 3);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia1EmBaixoTest() {
        Campo vizinho = new Campo(4, 3);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhComDistancia2DiagonalSuperiorEsquerdaTest() {
        Campo vizinho = new Campo(2, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia2DiagonalSuperiorDireitaTest() {
        Campo vizinho = new Campo(2, 4);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia2DiagonalInferiorEsquerdaTest() {
        Campo vizinho = new Campo(4, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia2DiagonalInferiorDireitaTest() {
        Campo vizinho = new Campo(4, 4);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertTrue(resultado);
    }

    @Test
    void vizinhoComDistancia3Test() {
        Campo vizinho = new Campo(5, 2);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertFalse(resultado);
    }

    @Test
    void naoVizinhoTest() {
        Campo vizinho = new Campo(7, 7);
        boolean resultado = campo.adicionarVizinho(vizinho);
        assertFalse(resultado);
    }

    @Test
    void valorPadraoAtributoMarcadoTest() {
        assertFalse(campo.isMarcado());
    }

    @Test
    void alternarMarcacaoTest() {
        campo.alternarMarcacao();
        assertTrue(campo.isMarcado());
    }

    @Test
    void alternarMarcacaoQuandoOcorrerDuasChamadasTest() {
        campo.alternarMarcacao();
        campo.alternarMarcacao();
        assertFalse(campo.isMarcado());
    }

    @Test
    void abrirCampoNaoMinadoENaoMarcadoTest() {
        assertTrue(campo.abrir());
    }

    @Test
    void abrirCampoNaoMinadoEMarcadoTest() {
        campo.alternarMarcacao();
        assertFalse(campo.abrir());
    }

    @Test
    void abrirCampoMinadoENaoMarcadoTest() {
        campo.minar();
        assertThrows(ExplosaoException.class, () -> campo.abrir());
    }

    @Test
    void abrirCampoMinadoEMarcadoTest() {
        campo.alternarMarcacao();
        campo.minar();
        assertFalse(campo.abrir());
    }

    @Test
    void abrirComVizinhos1Test() {
        Campo campo11 = new Campo(1, 1);
        Campo campo22 = new Campo(2, 2);

        campo22.adicionarVizinho(campo11);
        campo.adicionarVizinho(campo22);

        campo.abrir();

        assertTrue(campo22.isAberto() && campo11.isAberto());
    }

    @Test
    void abrirComVizinhos2Test() {
        Campo campo11 = new Campo(1, 1);
        Campo campo12 = new Campo(1, 2);
        Campo campo22 = new Campo(2, 2);

        campo12.minar();
        campo22.adicionarVizinho(campo11);
        campo22.adicionarVizinho(campo12);
        campo.adicionarVizinho(campo22);

        campo.abrir();

        assertTrue(campo22.isAberto() && !campo11.isAberto());
    }

    @Test
    void objetivoAlcancadoAbertoNaoMinadoTest() {
        assertTrue(campo.abrir());
        assertTrue(campo.objetivoAlcancado());
    }

    @Test
    void objetivoAlcancadoMinadoEMarcadoTest() {
        campo.minar();
        campo.alternarMarcacao();
        assertTrue(campo.objetivoAlcancado());
    }

    @Test
    void objetivoAlcancadoMinadoNaoMarcadoTest() {
        campo.minar();
        assertFalse(campo.objetivoAlcancado());
    }

    @Test
    void objetivoAlcancadoNaoMinadoENaoAbertoTest() {
        assertFalse(campo.objetivoAlcancado());
    }

    @Test
    void minasNaVizinhancaTest() {
        Campo vizinho1 = new Campo(3, 2);
        Campo vizinho2 = new Campo(2, 3);
        vizinho1.minar();
        campo.adicionarVizinho(vizinho1);
        campo.adicionarVizinho(vizinho2);

        assertEquals(1, campo.minasNaVizinhanca());
    }

    @Test
    void reiniciarCampoTest() {
        campo.minar();
        campo.alternarMarcacao();

        campo.reiniciar();

        assertFalse(campo.isAberto());
        assertFalse(campo.isMinado());
        assertFalse(campo.isMarcado());
    }

    @Test
    void toStringQuandoCampoMarcadoTest() {
        campo.alternarMarcacao();
        assertEquals("\u001B[33mx", campo.toString());
    }

    @Test
    void toStringQuandoCampoMinadoEAbertoTest() {
        campo.minar();
        campo.setAberto(true);
        assertEquals("*", campo.toString());
    }

    @Test
    void toStringQuandoCampoAbertoComMinasNaVizinhancaTest() {
        Campo vizinho = new Campo(3, 2);
        vizinho.minar();
        campo.adicionarVizinho(vizinho);
        campo.setAberto(true);

        String str = campo.toString();
        assertTrue(str.matches("\u001B\\[[0-9;]*m[1-8]\u001B\\[0m"));
    }

    @Test
    void toStringQuandoCampoAbertoSemMinasTest() {
        campo.setAberto(true);
        assertEquals("\u001B[32m■\u001B[0m", campo.toString());
    }

    @Test
    void toStringQuandoCampoFechadoNaoMarcadoTest() {
        assertEquals("?", campo.toString());
    }

}