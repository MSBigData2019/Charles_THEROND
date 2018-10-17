import requests
from bs4 import BeautifulSoup
import pandas as pd
import json

Urls="https://gist.github.com/paulmillr/2657075"

# Initialisation de rendu
profil_df = pd.DataFrame()
myhead = {'Authorization': 'token {}'.format('b349646a73be61ed820d699cf41e47501d54dcbb')}

# Fonction pour effacer les caracteres genants
def cleanText(txt):
    return txt.replace("\n","").replace("\t","").replace(" ","")


profil_df = pd.DataFrame()
req = requests.get(Urls)
soup = BeautifulSoup(req.content, "lxml")
querry1  = soup.select('table tbody tr td a ')
for result in querry1:
    if result.text==result['href'][-(len(result.text)):] and not "http" in result.text:
        line={}
        line["NOM"] = result.text
        line["RANK"] = str(result.parent.parent.th.string).replace("#","")
        get_num_repo = requests.get("https://api.github.com/users/" + result.text, headers=myhead)
        num_repo = 0
        #print(json.loads(get_num_repo.content))
        num_repo += int(json.loads(get_num_repo.content)["public_repos"] )
        if num_repo == 0:
            num_repo+=1

        line["NREPO"]=num_repo
        response = requests.get("https://api.github.com/users/" + result.text +"/repos?per_page=" + str(num_repo), headers=myhead)
        user_repo = json.loads(response.content)
        sum = 0
        for item in user_repo:
            sum += int(item['stargazers_count'])
        line["Sum_Star"]=sum
        line["moyenne"]=float(sum)/float(num_repo)
        profil_df=profil_df.append([line])
        print(line)

print(profil_df.sort_values(by='moyenne',ascending=False).head())



