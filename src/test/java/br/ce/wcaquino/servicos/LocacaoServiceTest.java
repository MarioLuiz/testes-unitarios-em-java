package br.ce.wcaquino.servicos;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoServiceTest {
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Test
	public void testeLocacao() {
		
		// cenario
		LocacaoService service = new  LocacaoService();
		Usuario usuario =  new Usuario("Mario");
		Filme filme = new Filme("Uma linda mulher", 0, 10.00);
		
		// acao
		Locacao locacao = null;
		try {
			locacao = service.alugarFilme(usuario,filme);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			// verificacao
			assertTrue(DataUtils.isMesmaData(new Date(),locacao.getDataLocacao()));
			assertThat(DataUtils.isMesmaData(new Date(),locacao.getDataLocacao()), CoreMatchers.is(true));
			
			assertTrue(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1),locacao.getDataRetorno()));
			assertThat(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1),locacao.getDataRetorno()), CoreMatchers.is(true));
			
			assertEquals(10.00 , locacao.getValor(), 0.01);
			assertThat(locacao.getValor(), CoreMatchers.is(10.00));
			assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(10.00)));
			assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.00)));
			
			// verificacao com errorCollector
			error.checkThat(locacao.getValor(), CoreMatchers.is(10.00));
			error.checkThat(DataUtils.isMesmaData(new Date(),locacao.getDataLocacao()), CoreMatchers.is(true));
			error.checkThat(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1),locacao.getDataRetorno()), CoreMatchers.is(true));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Não deveria lançar exception");
		}
		
		
		
	}
}
