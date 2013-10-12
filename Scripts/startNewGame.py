#Creates a new game with the existing users in the user colletion and then
#prints out the list of players - basic information only, such as name and isDead

import requests
from requests.auth import HTTPBasicAuth
import json

username = "andrew"
password = "password"

url = 'http://localhost:8080/werewolf/auth/restart'

headers = {'Content-type': 'application/json'}

payload = {'time': (1000)} #Creates a game with a 1 second day/night cycle

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print(r.text)

#check the list of players

r = requests.get('http://localhost:8080/werewolf/auth/players', auth=HTTPBasicAuth(username, password))

print(r.text)

