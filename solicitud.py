import requests, serial, time

arduino = serial.Serial('/dev/ttyACM0', 9600)

time.sleep(1)

txt = ''

while True:
    
    inductor = 1
    fotoelectrico = 0
    tPlastico = 0
    tMetal = 0
    tOrganico = 0
    
    valores = []
    
    while arduino.inWaiting() > 0:
        txt = str(arduino.readline())[2:-5]
        print(txt)
        
        valores = txt.split("-")   
        
        inductor = valores[4]
        fotoelectrico = valores[5]
        tPlastico = valores[1]
        tMetal = valores[2]
        tOrganico = valores[3]
        

        params = {'inductor':inductor,'fotoelectrico':fotoelectrico,'tPlastico':tPlastico,'tMetal':tMetal,'tOrganico':tOrganico}
        response = requests.post('http://192.168.43.130/PST/query/addRecord.php', data = params)
        print(response.text)

        
        
            