package br.com.leonardoferreira.jirareport.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public enum EstimateFieldReference {

    ISSUE_TYPE("Tipo de issue"),
    SYSTEM("Sistema"),
    TASK_SIZE("Tamanho/estimativa"),
    EPIC("Épico"),
    PROJECT("Projeto"),
    PRIORITY("Prioridade");

    @Getter
    private final String description;

    EstimateFieldReference(final String description) {
        this.description = description;
    }

    public static List<EstimateFieldReference> retrieveCustomList(final boolean system, final boolean taskSize,
                                                                  final boolean epic, final boolean project) {
        List<EstimateFieldReference> list = new ArrayList<>();
        list.add(ISSUE_TYPE);
        if (system) {
            list.add(SYSTEM);
        }
        if (taskSize) {
            list.add(TASK_SIZE);
        }
        if (epic) {
            list.add(EPIC);
        }
        if (project) {
            list.add(PROJECT);
        }

        return list;
    }
}
