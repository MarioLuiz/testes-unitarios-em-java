package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) {
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar metodo para salvar
		
		
		return locacao;
	}
	
	@Test
	public void teste() {
		
		// cenario
		LocacaoService service = new  LocacaoService();
		Usuario usuario =  new Usuario("Mario");
		Filme filme = new Filme("Uma linda mulher", 1, 10.00);
		
		// acao
		Locacao locacao = service.alugarFilme(usuario,filme);
		
		// verificacao
		Assert.assertTrue(DataUtils.isMesmaData(new Date(),locacao.getDataLocacao()));
		
		Assert.assertTrue(DataUtils.isMesmaData(DataUtils.obterDataComDiferencaDias(1),locacao.getDataRetorno()));
		
		Assert.assertTrue(locacao.getValor() == 10.00);
		
	}
}