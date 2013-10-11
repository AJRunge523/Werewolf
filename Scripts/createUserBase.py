##Populates database with 6 users - one admin (credentials are andrew : password)
## and 5 users - credentials are {a, ab, abc, abcd, abcde} : password


import requests
from requests.auth import HTTPBasicAuth
import json


data = json.dumps({'firstName':'andrew', 'lastName':'runge', 'userName':'andrew', 'password':'password','imageURL':'nope', 'role':'ROLE_ADMIN'})

url = 'http://localhost:8080/werewolf/newAccount'

headers = {'Content-type': 'application/json'}

r = requests.post(url, data=data, headers=headers)

data = json.dumps({'firstName':'andrew', 'lastName':'runge', 'userName':'a', 'password':'password','imageURL':'nope', 'role':'ROLE_USER'})

r = requests.post(url, data=data, headers=headers)

data = json.dumps({'firstName':'andrew', 'lastName':'runge', 'userName':'ab', 'password':'password','imageURL':'nope', 'role':'ROLE_USER'})

r = requests.post(url, data=data, headers=headers)

data = json.dumps({'firstName':'andrew', 'lastName':'runge', 'userName':'abc', 'password':'password','imageURL':'nope', 'role':'ROLE_USER'})

r = requests.post(url, data=data, headers=headers)

data = json.dumps({'firstName':'andrew', 'lastName':'runge', 'userName':'abcd', 'password':'password','imageURL':'nope', 'role':'ROLE_USER'})

r = requests.post(url, data=data, headers=headers)

data = json.dumps({'firstName':'andrew', 'lastName':'runge', 'userName':'abcde', 'password':'password','imageURL':'nope', 'role':'ROLE_USER'})

r = requests.post(url, data=data, headers=headers)
