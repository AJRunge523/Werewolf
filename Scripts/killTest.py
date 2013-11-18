#May need to change credentials depending on how the random
#werewolf selection goes

import requests
from requests.auth import HTTPBasicAuth
import json

username = "a"
password = "password"

headers = {'Content-type': 'application/json'}


print ("Getting List of players")

r = requests.get('http://secret-wildwood-3803.herokuapp.com/auth/players', auth=HTTPBasicAuth(username, password))


print (r.text) #Prints out the list of players

payload = {'lat' : 0.0, 'lon': 0.0}

#Updates location of player a and prints out player a's information
url = 'http://secret-wildwood-3803.herokuapp.com/auth/players/nearbyPlayers' 

print ("Retrieving nearby players. As part of this, player's information will be updated")

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print ('\n')

print (r.text)


#Attemps to kill ab - need to make sure that:
# 1. ab is in range - should be, since we won't change abc's location
# 2. It's night - check output window
# 3. abc is a werewolf, and ab is not a werewolf - change credentials/url
# accordingly, to make sure the killer is a werewolf and the target isn't
#Need to check output window to make sure it's day
url = 'http://secret-wildwood-3803.herokuapp.com/auth/players/ab'

payload = {'type': 'kill'}

print ("Attempting to kill player abc")

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print(r.text)
