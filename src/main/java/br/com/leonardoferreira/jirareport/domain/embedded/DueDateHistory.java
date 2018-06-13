package br.com.leonardoferreira.jirareport.domain.embedded;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author s2it_leferreira
 * @since 11/06/18 15:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DueDateHistory implements Serializable {
    private static final long serialVersionUID = 7542115783552544574L;

    private LocalDateTime created;

    private LocalDate dueDate;

}
