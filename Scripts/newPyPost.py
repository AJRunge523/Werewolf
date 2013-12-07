#Demonstrates functions for the villager players

import requests
from requests.auth import HTTPBasicAuth
import json

username = "a"
password = "password"

headers = {'Content-type': 'application/json'}

print ("Getting list of players")

r = requests.get('http://secret-wildwood-3803.herokuapp.com/auth/players', auth=HTTPBasicAuth(username, password))

print (r.text) #Prints out the list of players

payload = {'lat' : "37.2686429", 'lon': "-76.7126329"}

print(json.dumps(payload))

###Updates location of player a and prints out player a's information
url = 'http://secret-wildwood-3803.herokuapp.com/auth/players/location' 

print ("Updating player's location")

r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)

print(r.text)

##url = 'http://secret-wildwood-3803.herokuapp.com/auth/players/a'

##r = requests.get(url, auth=HTTPBasicAuth(username, password))
##
##print ('\n')
##
##print (r.text)
##
##
###Puts a vote on player abcd - player abcd will be killed at the next day/night cycle
###since they will have the most votes - can run script a second time to verify,
###also to verify that voting for a dead person is invalid
###Need to check output window to make sure it's day
##
##print ("Putting a vote on player abcd")
##
##url = 'http://secret-wildwood-3803.herokuapp.com/auth/players/abcd'
##
##payload = {'type': 'vote'}
##
##r = requests.post(url, auth=HTTPBasicAuth(username, password), data=json.dumps(payload), headers=headers)
##
##print(r.text)
