import json

import requests
import pandas as pd

url="https://www.open-medicaments.fr/api/v1/medicaments?query=paracetamol"

req = requests.get(url)
df = pd.read_json(req.content)

df = df.drop(["codeCIS"],axis=1)
df["split"] = df["denomination"].str.split(" ")
df[["NOM","LABO","QTE","UNIT","TYPE1","TYPE2"]] = df["denomination"].str.split(" ",expand=True)


df["QUANTITE"]=df.QTE+" "+df.UNIT
# df["QUANTITE"]=df.QUANTITE.str.replace(',','')

df["TYPE"]=df.TYPE1+" "+df.TYPE2

print(df.drop(["denomination","split","QTE","UNIT","TYPE1","TYPE2"],axis=1).head(10))