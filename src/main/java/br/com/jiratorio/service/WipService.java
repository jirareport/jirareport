package br.com.jiratorio.service;

import br.com.jiratorio.domain.entity.Issue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface WipService {

    Double calcAvgWip(LocalDate start, LocalDate end,
                    List<Issue> issues, Set<String> wipColumns);

}
