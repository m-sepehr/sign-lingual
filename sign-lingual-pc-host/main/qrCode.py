import cv2
from pyzbar.pyzbar import decode
import subprocess

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


if __name__ == "__main__":
    # Scan the QR code and configure Wi-Fi
    qr_code_data = scan_qr_code()
    configure_wifi(qr_code_data)