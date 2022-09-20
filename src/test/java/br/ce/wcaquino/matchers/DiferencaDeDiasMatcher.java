package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

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
		desc.appendText("Diferença de dias não bate");
    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(diferencaDeDias));
    }
}
