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
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testeLocacao() throws Exception {

		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario");
		Filme filme = new Filme("Uma linda mulher", 2, 10.00);

		// acao
		Locacao locacao = null;

		locacao = service.alugarFilme(usuario, filme);

		// verificacao
		assertTrue(DataUtils.isMesmaData(new Date(), locacao.getDataLocacao()));
		assertThat(DataUtils.isMesmaData(new Date(), locacao.getDataLocacao()), CoreMatchers.is(true));

		assertTrue(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1), locacao.getDataRetorno()));
		assertThat(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1), locacao.getDataRetorno()),
				CoreMatchers.is(true));

		assertEquals(10.00, locacao.getValor(), 0.01);
		assertThat(locacao.getValor(), CoreMatchers.is(10.00));
		assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(10.00)));
		assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.00)));

		// verificacao com errorCollector
		error.checkThat(locacao.getValor(), CoreMatchers.is(10.00));
		error.checkThat(DataUtils.isMesmaData(new Date(), locacao.getDataLocacao()), CoreMatchers.is(true));
		error.checkThat(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1), locacao.getDataRetorno()),
				CoreMatchers.is(true));
	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario");
		Filme filme = new Filme("Uma linda mulher", 0, 10.00);

		// acao
		service.alugarFilme(usuario, filme);
		System.out.println("Forma Elegante");
	}

	@Test
	public void testLocacao_filmeSemEstoque2() {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario");
		Filme filme = new Filme("Uma linda mulher", 0, 10.00);

		// acao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma exception");
		} catch (Exception e) {
			// e.printStackTrace();
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}

	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario");
		Filme filme = new Filme("Uma linda mulher", 0, 10.00);

		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filme);
	}

	@Test
	public void testLocacao_usuarioNull() throws FilmeSemEstoqueException {
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = null;
		Filme filme = new Filme("Uma linda mulher", 1, 10.00);

		// acao
		try {
			service.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma exception");
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario vazio"));
		}
		
		System.out.println("Forma Robusta");
	}
	
	@Test
	public void testLocacao_filmeNull() throws FilmeSemEstoqueException, LocadoraException{
		// cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Mario");
		Filme filme = null;

		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, filme);
		System.out.println("Forma Nova");
	}
}
