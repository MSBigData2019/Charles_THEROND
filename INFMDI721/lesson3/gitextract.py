import threading

import requests
from bs4 import BeautifulSoup
import pandas as pd
import json

Urls="https://gist.github.com/paulmillr/2657075"

# Initialisation de rendu
profil_df = pd.DataFrame()
myhead = {'Authorization': 'token {}'.format('34090fea264c455a8ced9d2a8e5061e3d281c80f')}

# Fonction pour effacer les caracteres genants
def cleanText(txt):
    return txt.replace("\n","").replace("\t","").replace(" ","")

def get_line(nom,rank,outline):
    get_num_repo = requests.get("https://api.github.com/users/" + result.text, headers=myhead)
    num_repo = 0
    Line={}
    num_repo += int(json.loads(get_num_repo.content)["public_repos"] )
    if num_repo == 0:
        num_repo+=1
    Line["NOM"] = nom
    Line["RANK"] = rank
    Line["NREPO"] = num_repo
    outline.append(Line)



profil_df = pd.DataFrame()
req = requests.get(Urls)
soup = BeautifulSoup(req.content, "lxml")
querry1  = soup.select('table tbody tr td a ')
n_thread=0
tmp=[]
for result in querry1:
    if result.text==result['href'][-(len(result.text)):] and not "http" in result.text:
        line={}
        line["NOM"] = result.text
        line["RANK"] = str(result.parent.parent.th.string).replace("#","")

        # if n_thread<6:
        #     n_thread+=1
        #     tmp.append([result.text,str(result.parent.parent.th.string).replace("#","")])
        # else:
        #     n_thread=0
        #     job=[]
        #     print("")
        #
        #     for i in range(6):
        #         out_list = list()
        #         thread = threading.Thread(target=get_line(tmp[i][0], tmp[i][1], out_list))
        #         job.append(thread)
        #     for j in job:
        #         j.start()
        #
        #     # Ensure all of the threads have finished
        #     for j in job:
        #         j.join()
        #     profil_df=profil_df.append(out_list)
        #     tmp=[]

        get_num_repo = requests.get("https://api.github.com/users/" + result.text, headers=myhead)
        num_repo = 0
        #print(json.loads(get_num_repo.content))
        num_repo += int(json.loads(get_num_repo.content)["public_repos"] )
        if num_repo == 0:
            num_repo+=1

        line["NREPO"]=num_repo
        sum = 0
        n_page=round(num_repo/100)+1
        for page in range(n_page):
            print(page)
            response = requests.get("https://api.github.com/users/" + result.text +"/repos?per_page=100&page=" +str(page) , headers=myhead)
            user_repo = json.loads(response.content)
            for item in user_repo:
                sum += int(item['stargazers_count'])
        line["Sum_Star"]=sum
        line["moyenne"]=float(sum)/float(num_repo)
        profil_df=profil_df.append([line])

        #
        print(line)

print(profil_df.head())#sort_values(by='moyenne',ascending=False).head())



