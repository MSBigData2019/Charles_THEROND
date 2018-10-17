import requests
from bs4 import BeautifulSoup
import pandas as pd
import json

Urls="https://gist.github.com/paulmillr/2657075"

# Initialisation de rendu
profil_df = pd.DataFrame()
head = {'Authorization': 'token {}'.format('e6deb30a9e5e1b0fa3731b631576f285daaa889a')}

# Fonction pour effacer les caracteres genants
def cleanText(txt):
    return txt.replace("\n","").replace("\t","").replace(" ","")


profil_df = pd.DataFrame()
req = requests.get(Urls)
soup = BeautifulSoup(req.content, "lxml")
querry1  = soup.select('table tbody tr td a ')
for result in querry1:
    if result.text==result['href'][-(len(result.text)):] and not "http" in result.text:
        print(result.text)
        line={}
        line["NOM"] = result.text
        line["RANK"] = str(result.parent.parent.th.string).replace("#","")
        get_num_repo = requests.get("https://api.github.com/users/"+result.text, headers=head)
        num_repo = 0
        num_repo += json.loads(get_num_repo.content)["public_repos"]
        line["NREPO"]=num_repo
        response = requests.get("https://api.github.com/users/"+result.text+"/repos?per_page="+str(num_repo), headers=head)
        user_repo = json.loads(response.content)
        sum = 0
        for item in user_repo:
            sum += int(item['stargazers_count'])
        line["Sum_Star"]=sum
        line["moyenne"]=float(sum)/float(num_repo)
        profil_df=profil_df.append([line])

print(profil_df.head())



get_num_repo = requests.get("https://api.github.com/users/onevcat", headers=head)
num_repo = json.loads(get_num_repo.content)["public_repos"]

response = requests.get("https://api.github.com/users/onevcat/repos?per_page="+str(num_repo), headers=head)
user_repo=json.loads(response.content)

for item in user_repo:
    print(item['full_name'])
    print(item['stargazers_count'])