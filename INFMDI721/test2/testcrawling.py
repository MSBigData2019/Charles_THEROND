import requests
from bs4 import BeautifulSoup
import pandas as pd
import numpy as np


# Liste des url à traiter
Urls=["https://www.darty.com/nav/recherche?p=200&s=relevence&text=acer&fa=790",
      "https://www.darty.com/nav/recherche?p=200&s=relevence&text=dell&fa=790"]

# Initialisation de rendu
quarterEndDecembre = pd.DataFrame()
actionEvolution = pd.DataFrame()
dividendYield = pd.DataFrame()

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
    Prix=[]
    for prix in querry1:
        Prix.append(str(prix.text).replace(",",".").replace("€","").replace("*","").replace("\\xa",""))
    print(Prix)
    # print(np.mean(Prix))
    print(querry1[0].text)
    # print(querry1.span.span)
    # row1 = querry1.parent.parent.findNext('tr').select('td.data')
    # RQ1["# of Estimates"]=row1[0].string
    # RQ1["Mean"]=row1[1].string
    # RQ1["High"]=row1[2].string
    # RQ1["Low 1 Year Ago"]=row1[3].string
    # RQ1["Company"]= titre[:10]
    #
    # quarterEndDecembre = quarterEndDecembre.append([RQ1])
    #
    # #Question 2 et 3
    # querry21=soup.find("div",{"class","sectionQuoteDetail"}).span.findNext('span').text
    # querry22=soup.select_one("div.sectionQuoteDetail span.valueContentPercent span").text
    # querry3= soup.find(text="% Shares Owned:").parent.parent.findNext("td").text
    #
    # RQ2["Price"]=cleanText(querry21)
    # RQ2["Change rate"]=cleanText(querry22)
    # RQ2["Company"]=titre[:10]
    # RQ2["% Shares Owned"]=cleanText(querry3)
    #
    # actionEvolution = actionEvolution.append([RQ2])
    #
    # #Question 4
    # querry4= soup.find(text="Dividend Yield").parent.parent.select("td[class='data']")
    # RQ4["Company"]=cleanText(querry4[0].text)
    # RQ4["Sector"]=cleanText(querry4[1].text)
    # RQ4["Indusrty"]=cleanText(querry4[2].text)
    # RQ4["Compagnie"]=titre[:10]
    #
    # dividendYield = dividendYield.append([RQ4])



# Affichage des resulats
# print(quarterEndDecembre.set_index('Company'))
# print(actionEvolution.set_index('Company'))
# print(dividendYield.set_index('Compagnie'))
