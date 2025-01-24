import os
import requests
from bs4 import BeautifulSoup
import zipfile

base_dir = os.getcwd() 
download_dir = os.path.join(base_dir, 'anexos')  

def download():
    for url in all_urls:
        try:
            if 'href' in url.attrs and ('Anexo_I' in url['href'] or 'Anexo_II' in url['href']) and url['href'].endswith('.pdf'):
                file_url = url['href']
                file_name = file_url.split('/')[-1]
                
                os.makedirs(download_dir, exist_ok=True)

                file_response = requests.get(file_url)

                with open(os.path.join(download_dir, file_name), 'wb') as f:
                    f.write(file_response.content)
                print(f"Arquivo baixado: {file_name}")
        except Exception as e:
            print(f"Erro ao baixar o arquivo: {e}")

def zip_directory(folder_path, zip_path):
    with zipfile.ZipFile(zip_path, mode='w') as zipf:
        len_dir_path = len(folder_path)
        for root, _, files in os.walk(folder_path):
            for file in files:
                file_path = os.path.join(root, file)
                zipf.write(file_path, file_path[len_dir_path:])
    print(f"Arquivos comprimidos em: {zip_path}")

url = 'https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos'
response = requests.get(url)
content = BeautifulSoup(response.text, 'html.parser')
all_urls = content.find_all('a')

download()

zip_directory(download_dir, os.path.join(base_dir, 'anexos-zip.zip'))
