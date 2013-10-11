#Demonstrates functions for the villager players

import requests
from requests.auth import HTTPBasicAuth
import json

username = "a"
password = "password"

headers = {'Content-type': 'application/json'}

r = requests.get('http://localhost:8080/werewolf/auth/players', auth=HTTPBasicAuth(username, password))

print (r.text) #Prints out the list of players

payload = {'lat' : 37.271459, 'lon': -76.711725}

#Updates location of player a and prints out player a's information
url = 'http://localhost:8080/werewolf/auth/players/location' 

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

url = 'http://localhost:8080/werewolf/auth/players/a'

r = requests.get(url, auth=HTTPBasicAuth(username, password))

print ('\n')

print (r.text)


#Puts a vote on player abcd - player abcd will be killed at the next day/night cycle
#since they will have the most votes - can run script a second time to verify,
#also to verify that voting for a dead person is invalid
#Need to check output window to make sure it's day
url = 'http://localhost:8080/werewolf/auth/players/abcd'

payload = {'type': 'vote'}

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print(r.text)
