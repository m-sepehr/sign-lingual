import requests

# Replace with your friend's machine IP address and port
friend_ip = "ip_address_placeholder"
friend_port = 12345

# URL for the receiveUserData endpoint
url = f"http://{friend_ip}:{friend_port}/receiveUserData"

# Random UID and token
uid = "random_uid"
token = "random_token"

# Data to send in the POST request
data = {
    'uid': uid,
    'token': token
}

# Send the HTTP POST request
response = requests.post(url, data=data)

# Print the response from the server
print(response.text)

