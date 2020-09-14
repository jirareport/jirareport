package br.com.jiratorio.strategy.issueperiodnameformat

import br.com.jiratorio.domain.IssuePeriodNameFormat
import br.com.jiratorio.extension.toLocalDate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.util.Locale

internal class IssuePeriodNameFormatTest {

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 02/01/2020, [01/01/2020 - 02/01/2020]",
        "01/01/2020, 31/01/2020, [01/01/2020 - 31/01/2020]",
        "15/01/2020, 17/01/2020, [15/01/2020 - 17/01/2020]",
        "15/01/2020, 15/02/2020, [15/01/2020 - 15/02/2020]"
    )
    fun `should format with INITIAL_AND_FINAL_DATE`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.INITIAL_AND_FINAL_DATE)
            .format(start.toLocalDate(), end.toLocalDate())

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 31/01/2020, Janeiro",
        "01/02/2020, 29/02/2020, Fevereiro",
        "01/03/2020, 31/03/2020, Março",
        "01/04/2020, 30/04/2020, Abril",
        "01/05/2020, 31/05/2020, Maio",
        "01/06/2020, 30/06/2020, Junho",
        "01/07/2020, 31/07/2020, Julho",
        "01/08/2020, 31/08/2020, Agosto",
        "01/09/2020, 30/09/2020, Setembro",
        "01/10/2020, 31/10/2020, Outubro",
        "01/11/2020, 30/11/2020, Novembro",
        "01/12/2020, 31/12/2020, Dezembro",
        "01/01/2020, 15/01/2020, [01/01/2020 - 15/01/2020]"
    )
    fun `should format with MONTH`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.MONTH)
            .format(start.toLocalDate(), end.toLocalDate(), Locale("pt", "BR'"))

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 31/01/2020, Janeiro/2020",
        "01/02/2020, 29/02/2020, Fevereiro/2020",
        "01/03/2020, 31/03/2020, Março/2020",
        "01/04/2020, 30/04/2020, Abril/2020",
        "01/05/2020, 31/05/2020, Maio/2020",
        "01/06/2020, 30/06/2020, Junho/2020",
        "01/07/2020, 31/07/2020, Julho/2020",
        "01/08/2020, 31/08/2020, Agosto/2020",
        "01/09/2020, 30/09/2020, Setembro/2020",
        "01/10/2020, 31/10/2020, Outubro/2020",
        "01/11/2020, 30/11/2020, Novembro/2020",
        "01/12/2020, 31/12/2020, Dezembro/2020",
        "01/01/2020, 15/01/2020, [01/01/2020 - 15/01/2020]"
    )
    fun `should format with MONTH_AND_YEAR`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.MONTH_AND_YEAR)
            .format(start.toLocalDate(), end.toLocalDate(), Locale("pt", "BR'"))

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 31/01/2020, Janeiro/20",
        "01/02/2020, 29/02/2020, Fevereiro/20",
        "01/03/2020, 31/03/2020, Março/20",
        "01/04/2020, 30/04/2020, Abril/20",
        "01/05/2020, 31/05/2020, Maio/20",
        "01/06/2020, 30/06/2020, Junho/20",
        "01/07/2020, 31/07/2020, Julho/20",
        "01/08/2020, 31/08/2020, Agosto/20",
        "01/09/2020, 30/09/2020, Setembro/20",
        "01/10/2020, 31/10/2020, Outubro/20",
        "01/11/2020, 30/11/2020, Novembro/20",
        "01/12/2020, 31/12/2020, Dezembro/20",
        "01/01/2020, 15/01/2020, [01/01/2020 - 15/01/2020]"
    )
    fun `should format with MONTH_AND_ABBREVIATED_YEAR`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.MONTH_AND_ABBREVIATED_YEAR)
            .format(start.toLocalDate(), end.toLocalDate(), Locale("pt", "BR'"))

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 31/01/2020, Jan",
        "01/02/2020, 29/02/2020, Fev",
        "01/03/2020, 31/03/2020, Mar",
        "01/04/2020, 30/04/2020, Abr",
        "01/05/2020, 31/05/2020, Mai",
        "01/06/2020, 30/06/2020, Jun",
        "01/07/2020, 31/07/2020, Jul",
        "01/08/2020, 31/08/2020, Ago",
        "01/09/2020, 30/09/2020, Set",
        "01/10/2020, 31/10/2020, Out",
        "01/11/2020, 30/11/2020, Nov",
        "01/12/2020, 31/12/2020, Dez",
        "01/01/2020, 15/01/2020, [01/01/2020 - 15/01/2020]"
    )
    fun `should format with ABBREVIATED_MONTH`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.ABBREVIATED_MONTH)
            .format(start.toLocalDate(), end.toLocalDate(), Locale("pt", "BR'"))

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 31/01/2020, Jan/2020",
        "01/02/2020, 29/02/2020, Fev/2020",
        "01/03/2020, 31/03/2020, Mar/2020",
        "01/04/2020, 30/04/2020, Abr/2020",
        "01/05/2020, 31/05/2020, Mai/2020",
        "01/06/2020, 30/06/2020, Jun/2020",
        "01/07/2020, 31/07/2020, Jul/2020",
        "01/08/2020, 31/08/2020, Ago/2020",
        "01/09/2020, 30/09/2020, Set/2020",
        "01/10/2020, 31/10/2020, Out/2020",
        "01/11/2020, 30/11/2020, Nov/2020",
        "01/12/2020, 31/12/2020, Dez/2020",
        "01/01/2020, 15/01/2020, [01/01/2020 - 15/01/2020]"
    )
    fun `should format with ABBREVIATED_MONTH_AND_YEAR`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.ABBREVIATED_MONTH_AND_YEAR)
            .format(start.toLocalDate(), end.toLocalDate(), Locale("pt", "BR'"))

        assertEquals(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "01/01/2020, 31/01/2020, Jan/20",
        "01/02/2020, 29/02/2020, Fev/20",
        "01/03/2020, 31/03/2020, Mar/20",
        "01/04/2020, 30/04/2020, Abr/20",
        "01/05/2020, 31/05/2020, Mai/20",
        "01/06/2020, 30/06/2020, Jun/20",
        "01/07/2020, 31/07/2020, Jul/20",
        "01/08/2020, 31/08/2020, Ago/20",
        "01/09/2020, 30/09/2020, Set/20",
        "01/10/2020, 31/10/2020, Out/20",
        "01/11/2020, 30/11/2020, Nov/20",
        "01/12/2020, 31/12/2020, Dez/20",
        "01/01/2020, 15/01/2020, [01/01/2020 - 15/01/2020]"
    )
    fun `should format with ABBREVIATED_MONTH_AND_ABBREVIATED_YEAR`(start: String, end: String, expected: String) {
        val result = IssuePeriodNameFormatter.from(IssuePeriodNameFormat.ABBREVIATED_MONTH_AND_ABBREVIATED_YEAR)
            .format(start.toLocalDate(), end.toLocalDate(), Locale("pt", "BR'"))

        assertEquals(expected, result)
    }

}
