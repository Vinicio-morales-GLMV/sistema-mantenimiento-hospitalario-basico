import requests
import json

# Configuración de Supabase
url = "https://hwjkyhuvelarpoxkvtzb.supabase.co/rest/v1/usuarios"
headers = {
    "apikey": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk",
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh3amt5aHV2ZWxhcnBveGt2dHpiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM4Mzg2MzksImV4cCI6MjA2OTQxNDYzOX0.4m3yre74R5lfR7sQhnQKZc1ZhpZ_zPw9-40BZKCP5Bk",
    "Content-Type": "application/json"
}

try:
    # Obtener todos los usuarios
    response = requests.get(url, headers=headers)
    print(f"Código de respuesta: {response.status_code}")
    print(f"Respuesta: {response.text}")
    
    if response.status_code == 200:
        usuarios = response.json()
        print(f"\nTotal de usuarios: {len(usuarios)}")
        
        for i, usuario in enumerate(usuarios):
            print(f"\nUsuario {i+1}:")
            for key, value in usuario.items():
                print(f"  {key}: {value}")
    
    # Probar búsqueda específica
    print("\n" + "="*50)
    print("PROBANDO BÚSQUEDA ESPECÍFICA")
    
    # Buscar el usuario Thomas
    test_url = url + "?email=eq.thomas.andrade@hospitec.com&select=*"
    response = requests.get(test_url, headers=headers)
    print(f"Búsqueda Thomas - Código: {response.status_code}")
    print(f"Búsqueda Thomas - Respuesta: {response.text}")
    
    # Buscar con password_hash
    test_url2 = url + "?email=eq.thomas.andrade@hospitec.com&password_hash=eq.thomas1234&select=*"
    response2 = requests.get(test_url2, headers=headers)
    print(f"Búsqueda con password - Código: {response2.status_code}")
    print(f"Búsqueda con password - Respuesta: {response2.text}")
    
except Exception as e:
    print(f"Error: {e}") 