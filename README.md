[![Build Status](https://travis-ci.org/LeonardoFerreiraa/jirareport.svg?branch=master)](https://travis-ci.org/LeonardoFerreiraa/jirareport)

# Jiratório

teste

# Setup

## Front End

```bash
$ npm install
$ ./node_modules/gulp/bin/gulp.js
```
## Token API Feriados

Para gerar seu token, possibilitando assim acessar a API de Feriados, basta acessar `http://www.calendario.com.br/api_feriados_municipais_estaduais_nacionais.php` e solicitar seu token por email.

## Database & Jira URL

Copiar `src/main/resources/application.example.yml` para `src/main/resources/appplication.yml` e alterar `jira.url`, `spring.datasource.username`, `spring.datasource.password` e `holiday.token`.
