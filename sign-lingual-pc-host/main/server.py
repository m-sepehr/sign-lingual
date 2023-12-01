import threading
from flask import Flask, request
from zeroconf import Zeroconf, ServiceInfo
import socket
import requests
import time
import sys
import os
import subprocess
import cv2
from pyzbar.pyzbar import decode
import socket
import json
from pynput import keyboard
import pdb
from requests.exceptions import ConnectionError, Timeout

app = Flask(__name__)

# Global variables
running = False
database_url = ''
user_token = ''
uid = ''


# ----------------- Keyboard Input for Reset -----------------
def on_press(key):
    global uid, user_token, database_url
    try:
        if key.char == 'q':
            print('q was pressed')
            print("Resetting configuration...")
            # Reset the UID, user token, and database URL
            uid, user_token, database_url = None, None, None
            # Delete the configuration file
            os.remove('config.json')

            # Trigger QR code scanning and reconfiguration here
            qr_code_data = scan_qr_code()
            configure_wifi(qr_code_data)

            # Send the local IP address to Firebase
            local_ip = get_ip_address()
            send_data(local_ip)

    except AttributeError:
        pass
# --------------------------------------------------

# ----------------- Keyboard Listener -----------------
def start_keyboard_listener():
    listener = keyboard.Listener(on_press=on_press)
    listener.start()
# -----------------------------------------------------

# ----------------- Saving configuration -----------------
# Function to save configuration data
def save_config(data, filename='config.json'):
    with open(filename, 'w') as file:
        json.dump(data, file)
# ---------------------------------------------------------

# ----------------- Loading configuration -----------------
# Function to load configuration data
def load_config(filename='config.json'):
    try:
        with open(filename, 'r') as file:
            return json.load(file)
    except FileNotFoundError:
        return None
# ---------------------------------------------------------

# ----------------- QR Code Scanning -----------------
# Function to scan QR code
def scan_qr_code():
    # Open a video capture device (you may need to change the index based on your setup)
    cap = cv2.VideoCapture(0)

    while True:
        ret, frame = cap.read()

        # Decode QR code
        decoded_objects = decode(frame)
    
        for obj in decoded_objects:
            # Assuming the QR code contains Wi-Fi credentials in the format "WIFI:S:SSID;T:WPA;P:password;;"
            if obj.type == 'QRCODE' and b'WIFI:' in obj.data.upper():
                os.system(f'afplay resources/Glass.aiff')
                return obj.data.decode("utf-8")


        #cv2.imshow("QR Code Scanner", frame)

        # Break the loop if 'q' key is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

# function to configure Wi-Fi based on QR code data
def configure_wifi(qr_code_data):
    global uid, user_token, database_url, network_online
    # Extract Wi-Fi information from QR code
    wifi_info = qr_code_data.split(';')
    first_term = wifi_info[0].split(':')[-1]
    ssid = first_term.split('|')[0]
    uid =first_term.split('|')[-2]
    database_url = "https://signlingual-901cc-default-rtdb.firebaseio.com/users/" + uid
    user_token =first_term.split('|')[-1]
    password = wifi_info[2].split(':')[-1]

    config_data = {
        'uid' : uid
    }

    save_config(config_data)  # saving the configuration data

    # Configure Wi-Fi using subprocess (for Windows)
    subprocess.run(['networksetup', '-setairportnetwork', 'en0', ssid, password])
    
    while (True):
        if is_connected_to_internet():
            network_online = True
            break
        else:
            network_online = False
        time.sleep(1)

# ------------------------------------------------------

# ----------------- Checking network connection -----------------
def is_connected_to_internet(url='http://www.google.com/', timeout=5):
    try:
        # Attempt to make a request to the specified URL
        requests.get(url, timeout=timeout)
        print("Connected to internet.")
        return True
    except requests.ConnectionError:
        print("No internet connection available.")
    return False
# ---------------------------------------------------------------

# ----------------- Retrieving local IP address -----------------
def get_ip_address():
    # Attempt to connect to an external host to determine the local IP
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))  # Google's DNS
        ip_address = s.getsockname()[0]
        s.close()
    except Exception as e:
        print(f"Error getting IP Address: {e}")
        ip_address = None
    return ip_address
# ---------------------------------------------------------------

# ----------------- Sending local IP to Firebase -----------------
def send_data(local_ip):
    global user_token, database_url
    response = requests.put(f"{database_url}/ip_address.json?auth=" +  user_token, json=local_ip)
    print("Sending data response:", response.status_code, response.json())  # Debugging print
    if response.status_code == 200:
        print("Data sent to Firebase.")
    else:
        print(f"Failed to send data. Status code: {response.status_code}")
# ---------------------------------------------------------------

# ----------------- Firebase Authentication -----------------
def get_ready_status():
    url = f"{database_url}/ready.json?auth=" + user_token
    try:
        response = requests.get(url)
        if response.status_code == 200:
            ready_status = response.json()
            print("Ready status response:", ready_status, "Type:", type(ready_status))
            return ready_status
        else:
            print(f"Error: Received non-200 response {response.status_code}")
            return None
    except ConnectionError:
        print("Error: Failed to connect to the Firebase URL.")
        # Handle the connection error (e.g., retry, log, or take alternative actions)
        return None
    except Timeout:
        print("Error: Request to Firebase timed out.")
        # Handle timeout error
        return None
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        # Handle other potential exceptions
        return None
# ---------------------------------------------------------------

@app.route('/receiveUserData', methods=['POST'])
def receive_user_data():
    global uid, user_token, database_url
    uid = request.form.get('uid')
    user_token = request.form.get('token')
    database_url = "https://signlingual-901cc-default-rtdb.firebaseio.com/users/" + uid
    print(f"Received UID: {uid}, Token: {user_token}")

    # setting environment variables to pass uid and token to the subprocess
    os.environ['USERID'] = uid
    os.environ['TOKEN'] = user_token
    return "Data received successfully"

def check_status_loop():
    global running, network_online
    script_process = None

    while True:
        print (network_online)
        if uid and user_token and database_url and network_online:
            ready = get_ready_status()
            if ready and not running:
                print(".....Launching......")

                # start the subprocess for hand detection
                script_process = subprocess.Popen(['python', 'useModel.py'])

                running = True

            elif not ready and running:
                print(".....Stopping......")

                # kill the subprocess for hand detection
                if script_process:
                    script_process.terminate()
                    script_process = None

                running = False
        time.sleep(1)

network_online = is_connected_to_internet()

if __name__ == '__main__':

    # Try to load existing configuration
    config = load_config()

    if config:
        uid = config['uid']
        database_url = "https://signlingual-901cc-default-rtdb.firebaseio.com/users/" + uid
    else:
        # Configuration does not exist, scan QR code and configure Wi-Fi
        qr_code_data = scan_qr_code()
        configure_wifi(qr_code_data)

    # Start the keyboard listener thread
    keyboard_thread = threading.Thread(target=start_keyboard_listener)
    keyboard_thread.start()
    
    local_ip = get_ip_address()

    if (user_token != ''):
        send_data(local_ip)

    # Starting the server
    service_type = "_http._tcp.local."
    service_name = "MyService"
    service_port = 12345
    address_byte = socket.inet_aton("127.0.0.1")
    zeroconf = Zeroconf()
    full_service_name = f"{service_name}.{service_type}"
    info = ServiceInfo(service_type, full_service_name, addresses=[address_byte], port=service_port, weight=0, priority=0)
    zeroconf.register_service(info)

    status_thread = threading.Thread(target=check_status_loop)
    status_thread.start()

    try:
        app.run(host='0.0.0.0', port=service_port)
    finally:
        zeroconf.unregister_service(info)
        zeroconf.close()