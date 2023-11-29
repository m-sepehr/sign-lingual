import requests

def main(UserID, Token):
    # Get command-line arguments
    friend_ip = "IP ADDRESS HOLDER"
    friend_port = 12345
    uid = UserID
    token = Token

    # URL for the receiveUserData endpoint
    url = f"http://{friend_ip}:{friend_port}/receiveUserData"

    # Data to send in the POST request
    data = {
        'uid': uid,
        'token': token
    }

    # Send the HTTP POST request
    response = requests.post(url, data=data)

    # Print the response from the server
    print(response.text)

if __name__ == "__main__":
    main()
