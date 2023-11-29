import threading
from flask import Flask, request
from zeroconf import Zeroconf, ServiceInfo
import socket

# converting the IP address to its byte representation
address_byte = socket.inet_aton("127.0.0.1")

service_type = "_http._tcp.local."
service_name = "MyService"
service_port = 12345

zeroconf = Zeroconf()
full_service_name = f"{service_name}.{service_type}"  # complete service name including the type
info = ServiceInfo(service_type, full_service_name, addresses=[address_byte], port=service_port, weight=0, priority=0)
zeroconf.register_service(info)

app = Flask(__name__)

@app.route('/receiveUserData', methods=['POST'])
def receive_user_data():
    uid = request.form.get('uid')
    token = request.form.get('token')

    print(f"Received UID: {uid}, Token: {token}")

    # This is where the actions are to be performed

    return "Data received successfully"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=service_port)

zeroconf.unregister_service(info)
zeroconf.close()

       