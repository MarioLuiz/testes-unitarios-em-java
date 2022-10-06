package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DiferencaDeDiasMatcher extends TypeSafeMatcher<Date> {

    private int diferencaDeDias;

    public DiferencaDeDiasMatcher(int diferencaDeDias) {
        this.diferencaDeDias = diferencaDeDias;
    }

    @Override
    public void describeTo(Description desc) {
    	Date dataEsperada = DataUtils.obterDataComDiferencaDias(diferencaDeDias);
    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		desc.appendText(dateFormat.format(dataEsperada) + " - Diferença de dias não bate");
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(diferencaDeDias));
    }
}
