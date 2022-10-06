package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	
	@InjectMocks
	private LocacaoService service;
	
	@Mock
	private SPCService spc;
	
	@Mock
	private LocacaoDAO dao;
	
	@Mock
	private EmailService emailService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.openMocks(this);
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
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
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

		// verificacao com Matchers proprios
		error.checkThat(locacao.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacao.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDeDias(1));

	}

	@Test(expected = FilmeSemEstoqueException.class)
	public void deveLancarExeptionAoAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

		// acao
		service.alugarFilme(usuario, filmes);
		System.out.println("Forma Elegante");
	}

	@Test
	public void deveLancarExeptionAoAlugarFilmeSemEstoque2() {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
		// acao
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lançado uma exception");
		} catch (Exception e) {
			// e.printStackTrace();
			Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
		}
	}

	@Test
	public void deveLancarExeptionAoAlugarFilmeSemEstoque3() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());

		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque");

		// acao
		service.alugarFilme(usuario, filmes);
	}

	@Test
	public void deveLancarExeptionAoAlugarFilmeUsuarioNulo() throws FilmeSemEstoqueException {
		// cenario
		Usuario usuario = null;
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		try {
			service.alugarFilme(usuario, filmes);
			Assert.fail("Deveria ter lançado uma exception");
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuario vazio"));
		}

		System.out.println("Forma Robusta");
	}

	@Test
	public void deveLancarExeptionAoAlugarFilmeNulo() throws FilmeSemEstoqueException, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();
		ArrayList<Filme> filmes = null;

		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		// acao
		service.alugarFilme(usuario, filmes);
		System.out.println("Forma Nova");
	}
	
	@Test
	public void deveFornecerDesconto25PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList<Filme>();
				filmes.addAll(Arrays.asList(new Filme("Uma linda mulher", 2, 10.00), new Filme("Top Gun", 1, 15.00),
				new Filme("Pato Donald", 3, 12.00)));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		// 10 + 15 + 9
		assertThat(locacao.getValor(), CoreMatchers.is(34.00));
	}
	
	@Test
	public void deveFornecerDesconto50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList<Filme>();
				filmes.addAll(Arrays.asList(new Filme("Uma linda mulher", 2, 10.00), new Filme("Top Gun", 1, 14.00),
				new Filme("Pato Donald", 3, 12.00), new Filme("As Branquelas", 2, 12.00)));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		// 10 + 14 + 9 + 6
		assertThat(locacao.getValor(), CoreMatchers.is(39.00));
	}
	
	@Test
	public void deveFornecerDesconto75PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList<Filme>();
				filmes.addAll(Arrays.asList(new Filme("Uma linda mulher", 2, 10.00), new Filme("Top Gun", 1, 14.00),
				new Filme("Pato Donald", 3, 12.00), new Filme("As Branquelas", 2, 12.00), new Filme("O Senhor dos Aneis", 2, 16.00)));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		// 10 + 14 + 9 + 6 + 4
		assertThat(locacao.getValor(), CoreMatchers.is(43.00));
	}
	
	@Test
	public void deveFornecerDesconto100PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList<Filme>();
				filmes.addAll(Arrays.asList(new Filme("Uma linda mulher", 2, 10.00), new Filme("Top Gun", 1, 14.00),
				new Filme("Pato Donald", 3, 12.00), new Filme("As Branquelas", 2, 12.00), new Filme("O Senhor dos Aneis", 2, 16.00),
				new Filme("Ace Ventura", 2, 16.00)));
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		// 10 + 14 + 9 + 6 + 4
		assertThat(locacao.getValor(), CoreMatchers.is(43.00));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = new ArrayList<Filme>();
				filmes.addAll(Arrays.asList(new Filme("Uma linda mulher", 2, 10.00), new Filme("Top Gun", 1, 14.00),
				new Filme("Pato Donald", 3, 12.00), new Filme("As Branquelas", 2, 12.00), new Filme("O Senhor dos Aneis", 2, 16.00),
				new Filme("Ace Ventura", 2, 16.00)));
		
		// acao
		Locacao retorno = service.alugarFilme(usuario, filmes);

		// verificacao
		//assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		//assertThat(retorno.getDataRetorno(), caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes= Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		//acao
		try {
			Locacao retorno = service.alugarFilme(usuario, filmes);
			//verificacao
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), is("Usuario Negativado"));
		}
		
		//verificacao
		Mockito.verify(spc).possuiNegativacao(usuario);
		
	}
	
	@Test
	public void deveEnviarEmailParaLocacaoesAtrasadas() {
		//cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2= umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3= umUsuario().comNome("Usuario com atraso").agora();
		List<Locacao> locacoes = Arrays.asList(
				umLocacao().comUsuario(usuario).atrasado().agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().comUsuario(usuario3).atrasado().agora(),
				umLocacao().comUsuario(usuario3).atrasado().agora());
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		Mockito.verify(emailService).notificarAtraso(usuario);
		Mockito.verify(emailService, Mockito.times(2)).notificarAtraso(usuario3);
		Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		Mockito.verifyNoMoreInteractions(emailService);
	}
	
	@Test
	public void deveTratarErroNoSpc() throws Exception {
		
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes= Arrays.asList(umFilme().agora());
		
		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));
		
		//Verificacao
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Problemas com SPC, tente novamente");
		//expectedException.expectMessage("Falha catastrofica");
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = umLocacao().agora();
		
		// acao
		service.prorrogarLocacao(locacao, 3);
		
		// verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada =  argCapt.getValue();
		
		error.checkThat(locacaoRetornada.getValor(), is(30.00));
		error.checkThat(locacaoRetornada.getDataLocacao(), MatchersProprios.ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), MatchersProprios.ehHojeComDiferencaDeDias(3));
	}
}
