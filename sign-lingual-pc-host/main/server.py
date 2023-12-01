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

app = Flask(__name__)

# Global variables
running = False
database_url = ''
user_token = ''
uid = ''

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
                return obj.data.decode("utf-8")


        cv2.imshow("QR Code Scanner", frame)

        # Break the loop if 'q' key is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

# Function to configure Wi-Fi based on QR code data
def configure_wifi(qr_code_data):
    # Extract Wi-Fi information from QR code
    wifi_info = qr_code_data.split(';')
    first_term = wifi_info[0].split(':')[-1]
    ssid = first_term.split('|')[0]
    userID =first_term.split('|')[-2]
    token =first_term.split('|')[-1]
    password = wifi_info[2].split(':')[-1]

    # Configure Wi-Fi using subprocess (for Windows)
    subprocess.run(['networksetup', '-setairportnetwork', 'en0', ssid, password])

# ----------------- QR Code Scanning -----------------


# ----------------- Firebase Authentication -----------------
def get_ready_status():
    url = f"{database_url}/ready.json?auth=" + user_token
    response = requests.get(url)
    ready_status = response.json()
    print("Ready status response:", ready_status, "Type:", type(ready_status))
    return ready_status
# ----------------- Firebase Authentication -----------------

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
    global running
    script_process = None

    while True:
        if uid and user_token and database_url:
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

if __name__ == '__main__':
    # Scan the QR code and configure Wi-Fi
    qr_code_data = scan_qr_code()
    configure_wifi(qr_code_data)

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