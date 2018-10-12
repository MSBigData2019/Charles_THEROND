import requests
from bs4 import BeautifulSoup
import pandas as pd

Urls=["https://www.reuters.com/finance/stocks/financial-highlights/LVMH.PA",
      "https://www.reuters.com/finance/stocks/financial-highlights/AIR.PA",
      "https://www.reuters.com/finance/stocks/financial-highlights/DANO.PA"]

# Initialisation de rendu
quarterEndDecembre = pd.DataFrame()
actionEvolution = pd.DataFrame()
dividendYield = pd.DataFrame()

# Fonction pour effacer les caracteres genants
def cleanText(txt):
    return txt.replace("\n","").replace("\t","").replace(" ","")

quarterEndDecembre = pd.DataFrame()

for url in Urls:
    #Lecture du web
    req = requests.get(url)
    soup = BeautifulSoup(req.content, "lxml")
    querry1  = soup.select('span[class$="Header"]')
    print(querry1)

