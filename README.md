# Teste processo seletivo IntuitiveCare BackEnd

# [TESTE 1 - WebScraping]
### Este teste deve ser realizado nas linguagens Python ou Java. E o código deverá executar o/a:


## Tarefas de código:
* 1.1. Acesso ao site: https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-dasociedade/atualizacao-do-rol-de-procedimentos
* 1.2. Download dos Anexos I e II em formato PDF:
* 1.3. Compactação de todos os anexos em um único arquivo (formatos ZIP, RAR, etc.).


# [TESTE 2 - Transformação de dados] 
### Crie um código em Python ou Java que execute as seguintes tarefas

## Tarefas de código
* 2.1. Extraia os dados da tabela Rol de Procedimentos e Eventos em Saúde do PDF do Anexo I do teste 1 (todas as páginas)
* 2.2. Salve os dados em uma tabela estruturada, em formato csv.
* 2.3. Compacte o csv em um arquivo denominado "Teste_{seu_nome}.zip".
* 2.4. Substitua as abreviações das colunas OD e AMB pelas descrições completas, conforme a legenda no 
rodapé.



# [TESTE 3 - Banco de dados] 
### Crie scripts .sql (compatíveis com MySQL 8. ou Postgres >10.0) que executem as tarefas abaixo

## Tarefas de preparação
* 3.1. Baixe os arquivos dos últimos 2 anos do repositório 
público https://dadosabertos.ans.gov.br/FTP/PDA/demonstracoes_contabeis/
* 3.2. Baixe os Dados cadastrais das Operadoras Ativas na ANS no formato CSV em https://dadosabertos.ans.gov.br/FTP/PDA/operadoras_de_plano_de_saude_ativas/Relatorio_cadop.csv


## Tarefas de código
* 3.3. Crie queries para estruturar tabelas necessárias para o arquivo csv.
* 3.4. Elabore queries para importar o conteúdo dos arquivos preparados, atentando para o encoding correto.
* 3.5. Desenvolva uma query analítica para responder:
• Quais as 10 operadoras com maiores despesas em "EVENTOS/ SINISTROS CONHECIDOS OU 
AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR" no último trimestre?
• Quais as 10 operadoras com maiores despesas nessa categoria no último ano?

# [TESTE 4 - API] 
### Desenvolva uma interface web usando Vue.js que interaja com um servidor em Python para realizar as tarefas abaixo.


## Tarefas de código
* 4.2. Crie um servidor com uma rota que realize uma busca textual na lista de cadastros de operadoras (preparada anteriormente) e retorne os registros mais relevantes.
* 4.3. Elabore uma coleção no Postman para demonstrar o resultado
