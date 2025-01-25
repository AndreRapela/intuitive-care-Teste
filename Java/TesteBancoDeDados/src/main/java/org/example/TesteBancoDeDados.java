package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TesteBancoDeDados {

    public static void transferirDadosTrimestrais(Connection conexao, String[] periodos) {
        try (Statement stmt = conexao.createStatement()) {
            for (String periodo : periodos) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS periodo_" + periodo + " (" +
                        "data_evento DATE, " +
                        "ans_id VARCHAR(255), " +
                        "conta_codigo VARCHAR(255), " +
                        "descricao_evento TEXT, " +
                        "saldo_final VARCHAR(255))";
                stmt.execute(createTableSQL);

                try (BufferedReader br = new BufferedReader(new FileReader("dados_csv/" + periodo + ".csv"))) {
                    String linha;
                    br.readLine(); // Ignorar cabeçalho
                    String insertSQL = "INSERT INTO periodo_" + periodo + " (data_evento, ans_id, conta_codigo, descricao_evento, saldo_final) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = conexao.prepareStatement(insertSQL);

                    while ((linha = br.readLine()) != null) {
                        String[] valores = linha.split(";");
                        pstmt.setString(1, valores[0]);
                        pstmt.setString(2, valores[1]);
                        pstmt.setString(3, valores[2]);
                        pstmt.setString(4, valores[3]);
                        pstmt.setString(5, valores[4]);
                        pstmt.executeUpdate();
                    }
                }

                String updateSQL = "UPDATE periodo_" + periodo + " SET ans_id = REPLACE(ans_id, '\"', '')";

                stmt.execute(updateSQL);
            }

            criarTabelaCadastro(conexao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void criarTabelaCadastro(Connection conexao) {
        try (Statement stmt = conexao.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS cadastro_operadoras (" +
                    "ans_registro VARCHAR(255), " +
                    "cnpj VARCHAR(255), " +
                    "razao_social VARCHAR(255), " +
                    "nome_fantasia VARCHAR(255), " +
                    "tipo_modalidade VARCHAR(255), " +
                    "endereco TEXT, " +
                    "numero VARCHAR(255), " +
                    "complemento VARCHAR(255), " +
                    "bairro VARCHAR(255), " +
                    "cidade VARCHAR(255), " +
                    "estado VARCHAR(255), " +
                    "cep VARCHAR(255), " +
                    "ddd VARCHAR(255), " +
                    "telefone VARCHAR(255), " +
                    "fax VARCHAR(255), " +
                    "email VARCHAR(255), " +
                    "responsavel VARCHAR(255), " +
                    "cargo_responsavel VARCHAR(255), " +
                    "data_registro DATE)";
            stmt.execute(createTableSQL);

            try (BufferedReader br = new BufferedReader(new FileReader("dados_csv/Relatorio_cadastro.csv"))) {
                String linha;
                br.readLine(); // Ignorar cabeçalho
                String insertSQL = "INSERT INTO cadastro_operadoras (ans_registro, cnpj, razao_social, nome_fantasia, tipo_modalidade, endereco, numero, complemento, bairro, cidade, estado, cep, ddd, telefone, fax, email, responsavel, cargo_responsavel, data_registro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conexao.prepareStatement(insertSQL);

                while ((linha = br.readLine()) != null) {
                    String[] valores = linha.split(";");
                    for (int i = 0; i < valores.length; i++) {
                        pstmt.setString(i + 1, valores[i]);
                    }
                    pstmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void maioresDespesasSemestre(Connection conexao) {
        System.out.println("As 10 operadoras com maiores despesas no último período:");
        try (Statement stmt = conexao.createStatement()) {
            String query = "SELECT razao_social, saldo_final FROM cadastro_operadoras INNER JOIN periodo_3t2021 ON ans_registro = ans_id WHERE descricao_evento LIKE '%EVENTOS/ SINISTROS CONHECIDOS OU AVISADOS DE ASSISTÊNCIA A SAÚDE MEDICO HOSPITALAR%' ORDER BY CAST(saldo_final AS DECIMAL(20, 2)) DESC LIMIT 10";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getString("razao_social") + ": " + rs.getString("saldo_final"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void maioresDespesasAno(Connection conexao) {
        System.out.println("As 10 operadoras com maiores despesas no último ano:");
        try (Statement stmt = conexao.createStatement()) {
            String query = "SELECT razao_social, saldo_final FROM (" +
                    "SELECT razao_social, saldo_final FROM cadastro_operadoras INNER JOIN periodo_1t2021 ON ans_registro = ans_id UNION ALL " +
                    "SELECT razao_social, saldo_final FROM cadastro_operadoras INNER JOIN periodo_2t2021 ON ans_registro = ans_id UNION ALL " +
                    "SELECT razao_social, saldo_final FROM cadastro_operadoras INNER JOIN periodo_3t2021 ON ans_registro = ans_id " +
                    ") AS despesas ORDER BY CAST(saldo_final AS DECIMAL(20, 2)) DESC LIMIT 10";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println(rs.getString("razao_social") + ": " + rs.getString("saldo_final"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/base_dados";
        String user = "root";
        String password = "123456";

        String[] periodosTrimestrais = {"1t2020", "1t2021", "2t2020", "2t2021", "3t2020", "3t2021", "4t2020"};

        try (Connection conexao = DriverManager.getConnection(url, user, password)) {
            transferirDadosTrimestrais(conexao, periodosTrimestrais);
            maioresDespesasSemestre(conexao);
            maioresDespesasAno(conexao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
