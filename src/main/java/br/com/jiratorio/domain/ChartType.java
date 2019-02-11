package br.com.jiratorio.domain;

public enum ChartType {
    BAR,
    DOUGHNUT,
    PIE;

    public String chartName() {
        return name().toLowerCase();
    }
}
