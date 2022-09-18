package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Date;

public class DiferencaDeDiasMatcher extends TypeSafeMatcher<Date> {

    private int diferencaDeDias;

    public DiferencaDeDiasMatcher(int diferencaDeDias) {
        this.diferencaDeDias = diferencaDeDias;
    }

    @Override
    public void describeTo(Description description) {

    }

    @Override
    protected boolean matchesSafely(Date data) {
        return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(diferencaDeDias));
    }
}
