package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exception.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void config() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		// cenario
		int a = 5;
		int b = 3;
		
		// acao
		int resultado = calc.somar(a , b);
		
		//Verificacao
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubitrairDoisValores() {
		// cenario
		int a = 7;
		int b = 3;
		
		// acao
		int resultado = calc.subitrair(a , b);
		
		//Verificacao
		Assert.assertEquals(4, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		// cenario
		int a = 6;
		int b = 3;
		
		// acao
		int resultado = calc.dividir(a , b);
		
		//Verificacao
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarUmaExecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		// cenario
		int a = 10;
		int b = 0;
		
		// acao
		int resultado = calc.dividir(a , b);
		
		//Verificacao
		Assert.assertEquals(2, resultado);
	}
}
