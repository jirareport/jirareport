package br.com.leonardoferreira.jirareport.service;

import br.com.leonardoferreira.jirareport.domain.form.AccountForm;
import br.com.leonardoferreira.jirareport.domain.vo.AccountVO;

/**
 * Created by lferreira on 3/26/18
 */
public interface AuthService {
    AccountVO login(AccountForm accountForm);
}
