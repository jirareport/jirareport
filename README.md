[![Build Status](https://travis-ci.org/LeonardoFerreiraa/jirareport.svg?branch=master)](https://travis-ci.org/LeonardoFerreiraa/jirareport)

# Jiratório

Work in progress

# Setup

## Front End

```bash
$ npm install
$ ./node_modules/gulp/bin/gulp.js
```

## Database & Jira URL

copy `src/main/resources/application.example.yml` to `src/main/resources/appplication.yml` 
change `jira.url`, `spring.datasource.username`, `spring.datasource.password`


##Token API Feriados

Para gerar seu token, possibilitando assim acessar a API de Feriados, basta acessar `http://www.calendario.com.br/api_feriados_municipais_estaduais_nacionais.php` e solicitar seu token por email.

Obs: Olhe sua caixa de SPAM também, pois o email pode cair lá. 