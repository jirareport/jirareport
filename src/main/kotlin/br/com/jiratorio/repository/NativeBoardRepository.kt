package br.com.jiratorio.repository

import br.com.jiratorio.domain.BoardPreferences

interface NativeBoardRepository {

    fun findAllOwners(): Set<String>

    fun findIssuePeriodPreferencesByBoard(boardId: Long): BoardPreferences?

}
