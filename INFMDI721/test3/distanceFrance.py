import requests
from bs4 import BeautifulSoup
import pandas as pd
import json
url="http://www.linternaute.com/ville/classement/villes/population"

# Initialisation de rendu
quarterEndDecembre = pd.DataFrame()

# Fonction pour effacer les caracteres genants
def cleanText(txt):
    return txt.replace("\n","").replace("\t","").replace(" ","")

matrice = pd.DataFrame()


req = requests.get(url)
soup = BeautifulSoup(req.content, "lxml")
querry1  = soup.select("table.odTable tbody tr")
ville_tab=[]
for ligne in querry1:
    ville_tab.append(str(str(ligne.find_next("td").find_next("td").a.string).replace("(La","").split(" ")[:-1]).replace("['","").replace("', '","").replace("']",""))

matrice = pd.DataFrame(columns=ville_tab,index=ville_tab)
for ind in matrice.index:
    for col in matrice.columns:
        if ind!=col:
            get_distance = requests.get("https://www.distance24.org/route.json?stops="+ str(ind)+"|"+str(col))
            print(str(ind)+"|"+str(col))
            print(json.loads(get_distance.content)["distance"])
            matrice.loc[ind,col]=json.loads(get_distance.content)["distance"]
            matrice.loc[col,ind]=json.loads(get_distance.content)["distance"]
        else:
            break
#https://www.distance24.org/route.json?stops=Paris|Lyon

print(matrice)