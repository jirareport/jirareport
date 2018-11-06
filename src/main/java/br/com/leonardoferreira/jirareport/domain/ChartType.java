package br.com.leonardoferreira.jirareport.domain;

public enum ChartType {
    BAR,
    DOUGHNUT,
    PIE;

    public String chartName() {
        return name().toLowerCase();
    }
}
