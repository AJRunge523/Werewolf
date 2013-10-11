#May need to change credentials depending on how the random
#werewolf selection goes

import requests
from requests.auth import HTTPBasicAuth
import json

username = "a"
password = "password"

headers = {'Content-type': 'application/json'}

r = requests.get('http://localhost:8080/werewolf/auth/players', auth=HTTPBasicAuth(username, password))

print (r.text) #Prints out the list of players

payload = {'lat' : 0.0, 'lon': 0.0}

#Updates location of player a and prints out player a's information
url = 'http://localhost:8080/werewolf/auth/players/nearbyPlayers' 

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print ('\n')

print (r.text)


#Attemps to kill ab - need to make sure that:
# 1. ab is in range - should be, since we won't change abc's location
# 2. It's night - check output window
# 3. abc is a werewolf, and ab is not a werewolf - change credentials/url
# accordingly, to make sure the killer is a werewolf and the target isn't
#Need to check output window to make sure it's day
url = 'http://localhost:8080/werewolf/auth/players/ab'

payload = {'type': 'kill'}

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print(r.text)
