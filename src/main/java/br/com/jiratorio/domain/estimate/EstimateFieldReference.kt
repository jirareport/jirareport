package br.com.jiratorio.domain.estimate

import java.util.ArrayList

enum class EstimateFieldReference(val description: String) {

    ISSUE_TYPE("Tipo de issue"),
    SYSTEM("Sistema"),
    TASK_SIZE("Tamanho/estimativa"),
    EPIC("Épico"),
    PROJECT("Projeto"),
    PRIORITY("Prioridade");

    companion object {
        fun retrieveCustomList(
            system: Boolean,
            taskSize: Boolean,
            epic: Boolean,
            project: Boolean
        ): List<EstimateFieldReference> {
            val list = ArrayList<EstimateFieldReference>()
            list.add(ISSUE_TYPE)
            if (system) {
                list.add(SYSTEM)
            }
            if (taskSize) {
                list.add(TASK_SIZE)
            }
            if (epic) {
                list.add(EPIC)
            }
            if (project) {
                list.add(PROJECT)
            }

            return list
        }
    }
}
