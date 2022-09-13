package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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

	private LocacaoService service;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() {
		service = new LocacaoService();
	}

	@After
	public void tearDown() {
	}
	
	@BeforeClass
	public static void setupClass() {
		System.out.println("@BeforeClass");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("@AfterClass");
	}

	@Test
	public void testeLocacao() throws Exception {
		
		//cenario
		Usuario usuario = new Usuario("Mario");
		List<Filme> filmes = Arrays.asList(new Filme("Uma linda mulher", 2, 10.00));
		
		// acao
		Locacao locacao = null;

		locacao = service.alugarFilme(usuario, filmes);

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
		Usuario usuario = new Usuario("Mario");
		List<Filme> filmes = Arrays.asList(new Filme("Uma linda mulher", 0, 10.00));

		// acao
		service.alugarFilme(usuario, filmes);
		System.out.println("Forma Elegante");
	}

	@Test
	public void testLocacao_filmeSemEstoque2() {
		// cenario
		Usuario usuario = new Usuario("Mario");
		List<Filme> filmes = Arrays.asList(new Filme("Uma linda mulher", 0, 10.00));
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lan�ado uma exception");
		} catch (Exception e) {
			// e.printStackTrace();
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
		}
	}

	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		// cenario
		Usuario usuario = new Usuario("Mario");
		List<Filme> filmes = Arrays.asList(new Filme("Uma linda mulher", 0, 10.00));

		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void testLocacao_usuarioNull() throws FilmeSemEstoqueException {
		// cenario
		Usuario usuario = null;
		List<Filme> filmes = Arrays.asList(new Filme("Uma linda mulher", 2, 10.00));

		// acao
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lan�ado uma exception");
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario vazio"));
		}

		System.out.println("Forma Robusta");
	}

	@Test
	public void testLocacao_filmeNull() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Mario");
		ArrayList<Filme> filmes = null;

		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, filmes);
		System.out.println("Forma Nova");
	}
	
	@Test
	public void testeLocacaoComDescontoProgressivo() throws Exception {
		
		//cenario
		Usuario usuario = new Usuario("Mario");
		List<Filme> filmes = new ArrayList<Filme>();
				filmes.addAll(Arrays.asList(new Filme("Uma linda mulher", 2, 10.00), new Filme("Top Gun", 1, 15.00),
				new Filme("Pato Donald", 3, 15.00), new Filme("As Branquelas", 2, 12.00), new Filme("Re Zero", 5, 13.00)
				,new Filme("Chacal", 2, 13.00)));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		for (int i = 0; i < filmes.size(); i++) {
			if(i == 2) {
				assertThat(filmes.get(i).getDesconto.porcentagem, CoreMatchers.is(25.00));
			}
			if(i == 3) {
				assertThat(filmes.get(i).getDesconto.porcentagem, CoreMatchers.is(50.00));
			}
			if(i == 4) {
				assertThat(filmes.get(i).getDesconto.porcentagem, CoreMatchers.is(75.00));
			}
			if(i == 5) {
				assertThat(filmes.get(i).getDesconto.porcentagem, CoreMatchers.is(1.00));
			}
		}
	}
	
	
}
