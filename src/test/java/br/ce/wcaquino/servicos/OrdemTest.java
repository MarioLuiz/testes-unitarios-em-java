package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;


@FixMethodOrder
public class OrdemTest {
	
	public static int contador = 0;
	
	@Test
	public void a_inicia() {
		contador = 1;
	}
	
	@Test
	public void b_verifica() {
		Assert.assertEquals(1, contador);
	}
}
