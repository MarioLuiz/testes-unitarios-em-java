package br.ce.wcaquino.matchers;

import java.util.Calendar;

public class MatchersProprios {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(Calendar.MONDAY);
	}

	public static DiferencaDeDiasMatcher ehHoje() {return new DiferencaDeDiasMatcher(0); }

	public static DiferencaDeDiasMatcher ehHojeComDiferencaDeDias( int quantidadeDia) { return new DiferencaDeDiasMatcher(quantidadeDia);}

}
