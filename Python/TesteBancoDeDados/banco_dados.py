import mysql.connector

def transferir_dados_trimestrais(periodos):
    try:
        for periodo in periodos:
            cur.execute(f"""
                CREATE TABLE periodo_{periodo}(
                data_evento DATE,
                ans_id VARCHAR(255),
                conta_codigo VARCHAR(255),
                descricao_evento TEXT,
                saldo_final VARCHAR(255)
            )
            """)
            with open(f'dados_csv/{periodo}.csv', 'r') as arquivo_csv:
                next(arquivo_csv)  
                for linha in arquivo_csv:
                    valores = linha.strip().split(';')
                    cur.execute(f"""
                        INSERT INTO periodo_{periodo} 
                        (data_evento, ans_id, conta_codigo, descricao_evento, saldo_final)
                        VALUES (%s, %s, %s, %s, %s)
                    """, valores)
            cur.execute(f"""
                UPDATE periodo_{periodo} 
                SET ans_id = REPLACE(ans_id, '"', '')
            """)
        criar_tabela_cadastro()
    except Exception as erro:
        print(erro)


def criar_tabela_cadastro():
    try:
        cur.execute("""
            CREATE TABLE cadastro_operadoras(
            ans_registro VARCHAR(255),
            cnpj VARCHAR(255),
            razao_social VARCHAR(255),
            nome_fantasia VARCHAR(255),
            tipo_modalidade VARCHAR(255),
            endereco TEXT,
            numero VARCHAR(255),
            complemento VARCHAR(255),
            bairro VARCHAR(255),
            cidade VARCHAR(255),
            estado VARCHAR(255),
            cep VARCHAR(255),
            ddd VARCHAR(255),
            telefone VARCHAR(255),
            fax VARCHAR(255),
            email VARCHAR(255),
            responsavel VARCHAR(255),
            cargo_responsavel VARCHAR(255),
            data_registro DATE
        )
        """)
        with open(f'dados_csv/Relatorio_cadastro.csv', 'r') as arquivo_cadastro:
            next(arquivo_cadastro)  
            for linha in arquivo_cadastro:
                valores = linha.strip().split(';')
                cur.execute(f"""
                    INSERT INTO cadastro_operadoras 
                    (ans_registro, cnpj, razao_social, nome_fantasia, tipo_modalidade, endereco, numero, complemento, bairro, cidade, estado, cep, ddd, telefone, fax, email, responsavel, cargo_responsavel, data_registro)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, valores)
    except Exception as erro:
        print(erro)


def maiores_despesas_semestre():
    print('As 10 operadoras com maiores despesas no último período:')
    cur.execute("""
        SELECT razao_social, saldo_final 
        FROM cadastro_operadoras, periodo_3t2021
        WHERE ans_id = ans_registro 
        AND descricao_evento LIKE '%EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR%'
        ORDER BY CAST(saldo_final AS DECIMAL(20, 2)) DESC
        LIMIT 10
    """)
    for resultado in cur.fetchall():
        print(resultado)


def maiores_despesas_ano():
    print('As 10 operadoras com maiores despesas no último ano:')
    cur.execute("""
        SELECT razao_social, saldo_final 
        FROM cadastro_operadoras, periodo_1t2021 
        UNION ALL
        SELECT razao_social, saldo_final 
        FROM cadastro_operadoras, periodo_2t2021 
        UNION ALL
        SELECT razao_social, saldo_final 
        FROM cadastro_operadoras, periodo_3t2021
        WHERE ans_id = ans_registro 
        AND descricao_evento LIKE '%EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR%'
        ORDER BY CAST(saldo_final AS DECIMAL(20, 2)) DESC
        LIMIT 10
    """)
    for resultado in cur.fetchall():
        print(resultado)


try:
    conexao = mysql.connector.connect(
        host="localhost",
        user="root",
        password="123456",
        database="base_dados"
    )
    cur = conexao.cursor()
except Exception as erro:
    print(erro)

periodos_trimestrais = ['1t2020', '1t2021', '2t2020', '2t2021', '3t2020', '3t2021', '4t2020'] 

transferir_dados_trimestrais(periodos_trimestrais)
conexao.commit()
maiores_despesas_semestre()
maiores_despesas_ano()
cur.close()
conexao.close()
