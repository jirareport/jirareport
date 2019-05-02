package br.com.jiratorio.domain.response.board

import br.com.jiratorio.domain.entity.Board

data class BoardFeatureResponse(
    val dueDate: Boolean,
    val epic: Boolean,
    val estimate: Boolean,
    val system: Boolean,
    val project: Boolean,
    val efficiency: Boolean
) {

    constructor(board: Board) : this(
        dueDate = !board.dueDateCF.isNullOrEmpty(),
        epic = !board.epicCF.isNullOrEmpty(),
        estimate = !board.estimateCF.isNullOrEmpty(),
        system = !board.systemCF.isNullOrEmpty(),
        project = !board.projectCF.isNullOrEmpty(),
        efficiency = !board.waitingColumns.isNullOrEmpty() && !board.touchingColumns.isNullOrEmpty()
    )

}
