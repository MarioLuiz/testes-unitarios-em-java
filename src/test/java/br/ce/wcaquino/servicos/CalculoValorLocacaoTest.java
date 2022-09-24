package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exception.FilmeSemEstoqueException;
import br.ce.wcaquino.exception.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	
	private LocacaoService service;
	
	@Parameter
	public List<Filme> filmes;
	
	@Parameter(value=1)
	public Double valorLocacao;
	
	@Parameter(value=2)
	public String cenario;
	
	@Before
	public void setup() {
		service = new LocacaoService();
		LocacaoDAO dao = new LocacaoDAOFake();
		service.setlocacaoDAO(dao);
	}
	
	private static Filme filme1 = umFilme().comValor(4.00).agora(); 
	private static Filme filme2 = umFilme().comValor(4.00).agora(); 
	private static Filme filme3 = umFilme().comValor(4.00).agora(); 
	private static Filme filme4 = umFilme().comValor(4.00).agora(); 
	private static Filme filme5 = umFilme().comValor(4.00).agora(); 
	private static Filme filme6 = umFilme().comValor(4.00).agora(); 
	private static Filme filme7 = umFilme().comValor(4.00).agora(); 
	
	@Parameters(name = "Teste {index} = {2}")
	public static Collection<Object []> getParametros() {
		return Arrays.asList(new Object[][] {
			{Arrays.asList(filme1, filme2), 8.00, "2 filmes: Sem desconto"},
			{Arrays.asList(filme1, filme2, filme3), 11.00, "3 filmes: 25%"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.00, "4 filmes: 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.00, "5 filmes: 75%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.00, "6 filmes: 100%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6, filme7), 18.00, "7 filmes: Sem desconto"},
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		
		//cenario
		Usuario usuario = new Usuario("Mario");
		
		// acao
		Locacao locacao = service.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(locacao.getValor(), CoreMatchers.is(valorLocacao));
	}

}
