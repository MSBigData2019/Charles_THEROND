import requests
from bs4 import BeautifulSoup
import pandas as pd
import numpy as np


# Liste des url à traiter
Urls=["https://www.darty.com/nav/recherche?p=200&s=relevence&text=acer&fa=790",
      "https://www.darty.com/nav/recherche?p=200&s=relevence&text=dell&fa=790"]



# Fonction pour effacer les caracteres genants
def cleanText(txt):
    return txt.replace("\n","").replace("\t","").replace(" ","")

for url in Urls:
    #Lecture du web
    req = requests.get(url)
    RQ1={}
    # RQ2={}
    # RQ4={}
    print(url)
    soup = BeautifulSoup(req.content, "lxml")
    # #Nom de la societe
    # titre =soup.select_one("div#sectionTitle h1").text
    #
    # #Question 1
    querry1 = soup.select("span.darty_prix")
    querry2 = soup.select("p.darty_prix_barre_remise")
    Remise=[]
    Prix=[]
    for prix in querry1:
        try:
            Prix.append(float(str(prix.text).replace(",",".").replace("€","").replace("*","").replace(' ',"")))
        except:
            print(str(prix.text).replace(",",".").replace("€","").replace("*","").replace('\\xa',""))

    for remise in querry2:
        Remise.append(int(str(remise.text).replace("%","").replace(" ","")))
    print(Prix)
    print(Remise)
    print()
    print("nombre de remises "+str(len(Remise)))
    print("moyenne des remises "+ str(np.mean(Remise)))
    print()
    print("prix max "+str(np.max(Prix)))
    print("prix moyen "+str(np.mean(Prix)))
    print("prix min "+str(np.min(Prix)))

# Dell gagne