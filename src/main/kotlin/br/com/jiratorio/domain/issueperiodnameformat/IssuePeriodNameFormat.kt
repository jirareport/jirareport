package br.com.jiratorio.domain.issueperiodnameformat

enum class IssuePeriodNameFormat(
    private val delegate: IssuePeriodNameFormatter
) : IssuePeriodNameFormatter by delegate {

    INITIAL_AND_FINAL_DATE(InitialAndFinalDateFormatter),
    MONTH(PatternBasedFormatter(pattern = "MMMM")),
    MONTH_AND_YEAR(PatternBasedFormatter(pattern = "MMMM/yyyy")),
    MONTH_AND_ABBREVIATED_YEAR(PatternBasedFormatter(pattern = "MMMM/yy")),
    ABBREVIATED_MONTH(PatternBasedFormatter(pattern = "MMM")),
    ABBREVIATED_MONTH_AND_YEAR(PatternBasedFormatter(pattern = "MMM/yyyy")),
    ABBREVIATED_MONTH_AND_ABBREVIATED_YEAR(PatternBasedFormatter(pattern = "MMM/yy"));

}
