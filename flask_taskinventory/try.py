from datetime import datetime
import pytz

# Mendapatkan waktu saat ini dalam UTC
current_time_utc = datetime.utcnow()

# Menentukan zona waktu WIB
wib = pytz.timezone('Asia/Jakarta')

# Mengonversi waktu UTC ke WIB
current_time_wib = current_time_utc.now(wib)
print("Current WIB time:", current_time_wib)

datetime.now(wib)