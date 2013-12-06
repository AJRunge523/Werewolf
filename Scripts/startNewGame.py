#Creates a new game with the existing users in the user colletion and then
#prints out the list of players - basic information only, such as name and isDead

import requests
from requests.auth import HTTPBasicAuth
import json

username = "andrew"
password = "password"

url = 'http://secret-wildwood-3803.herokuapp.com/auth/restart'

headers = {'Content-type': 'application/json'}

payload = {'time': (900000)} #Creates a game with a 1 second day/night cycle

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print ("Starting new game")

print(r.text)

#check the list of players

r = requests.get('http://secret-wildwood-3803.herokuapp.com/auth/players', auth=HTTPBasicAuth(username, password))

print ("Verifying list of players")

print(r.text)

