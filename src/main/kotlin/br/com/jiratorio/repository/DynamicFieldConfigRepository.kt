package br.com.jiratorio.repository

import br.com.jiratorio.domain.entity.DynamicFieldConfig
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DynamicFieldConfigRepository : CrudRepository<DynamicFieldConfig, Long>
