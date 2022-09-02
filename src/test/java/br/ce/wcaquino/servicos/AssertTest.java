package br.ce.wcaquino.servicos;

import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

import org.junit.Assert;

public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		Assert.assertEquals("Erro de comparacao", 1.51, 1.51, 0.01111111);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		int i = 7;
		Integer i7 = 7;
		Assert.assertEquals(Integer.valueOf(i) , i7);
		Assert.assertEquals(i , i7.intValue());
		
		Assert.assertEquals("bola" , "bola");
		Assert.assertNotEquals("Bola" , "bola");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		Usuario u1 = new Usuario("User 1");
		Usuario u2 = new Usuario("User 1");
		Usuario u3 = u2;
		Usuario u4 = null;
		
		Assert.assertEquals(u1, u2);
		Assert.assertSame(u3, u2);
		Assert.assertNotSame(u1, u2);
		Assert.assertNull(u4);
		Assert.assertNotNull(u1);
	}
}
