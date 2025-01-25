import pdfplumber
import pandas as pd
import zipfile
import os


def extract_pdf_to_csv(pdf_path, start_page=3, end_page=191):
    with pdfplumber.open(pdf_path) as pdf:

        all_data = []
        
        for page_num in range(start_page - 1, end_page): 
            page = pdf.pages[page_num]
            table = page.extract_table() 
            if table:
                all_data.extend(table) 
        
      
        df = pd.DataFrame(all_data[1:], columns=all_data[0])  
        
        df['OD'] = df['OD'].replace({'OD': 'Seg. Odontológica'}) 
        df['AMB'] = df['AMB'].replace({'AMB': 'Seg. Ambulatorial'})  

        csv_file = os.path.join(os.getcwd(), 'Rol_de_Procedimentos_e_Eventos.csv')  
        df.to_csv(csv_file, sep=';', encoding='utf-8', index=False)
        
    return csv_file

def compress_to_zip(csv_file, zip_name='Teste_André_Rapela.zip'):
    zip_path = os.path.join(os.getcwd(), zip_name) 

    with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
        zipf.write(csv_file, os.path.basename(csv_file)) 
        
    os.remove(csv_file)

user_home = os.path.expanduser("~") 
pdf_path = os.path.join(user_home, 'Desktop', 'intuitive-care', 'intuitive-care-Teste', 'Python', 'testeWebscraping', 'anexos', 'Anexo_I_Rol_2021RN_465.2021_RN624_RN625.2024.pdf')

csv_file = extract_pdf_to_csv(pdf_path, start_page=3, end_page=191)


compress_to_zip(csv_file)

print("Processo concluído: O arquivo ZIP foi gerado com sucesso.")
