#include <Servo.h>

//Variables de Servos
Servo servoEntrada; //AZUL
Servo servoPlastico; //VERDE
Servo servoMetal; //AMARILLO

//Variables sensores
int sensorPlastico = A0; //Entrada fotocelda
float valorLuz = 0;
int pinLaser = 5; //Salida láser
int pinInductivo = 8;
int pinCapacitivo = 12;

int pinInfraPlastico = 3;
int pinInfraMetal = 6;
int pinInfraPrincipal = 12;

//Variables arduinos
int estadoPlastico = 0;
int estadoMetal = 0;
int estadoOrganico = 0;

//Variables para el tacho lleno o vacio
int estadoTachoPlastico = 0;
int estadoTachoMetal = 0;
int estadoTachoOrganico = 0;

int isMetal = 0;
float isPlastico = 0;




void setup() {
  Serial.begin(9600);
  servoEntrada.attach(9);
  servoPlastico.attach(11);
  servoMetal.attach(10);

  //Se colocan en sus posiciones de cerrado
  servoEntrada.write(90); //Se abre horario a 90
  servoPlastico.write(180); //Se abre antihorario a 90
  servoMetal.write(180); //Se abre antihorario a 90

  pinMode(sensorPlastico, INPUT); //Fotocelda recibe
  pinMode(pinLaser, OUTPUT); //Láser emite
  pinMode(pinCapacitivo, INPUT); //Capacitivo recibe
  pinMode(pinInductivo, INPUT);
  pinMode(pinInfraPlastico, INPUT);
  pinMode(pinInfraMetal, INPUT);
  pinMode(pinInfraPrincipal, INPUT);
}

void loop() {
  estadoPlastico = 0;
  estadoMetal = 0;
  estadoOrganico = 0;
  
  // estado de los tachos
  
  //estadoTachoPlastico = 0;
  //estadoTachoMetal = 0;
  //estadoTachoOrganico = 0;
  

  isMetal = digitalRead(pinInductivo);
  isPlastico = 0;


  if (digitalRead(pinInfraPlastico) == LOW) {
    estadoPlastico = 1;
  }

  if (digitalRead(pinInfraMetal) == LOW) {
    estadoMetal = 1;
  }
  // si el tacho de plastico esta lleno
  
 /* if (digitalRead(pinInfraPlastico) == LOW){
      estadoTachoPlastico = 1;
      estadoTachoMetal = 0;
      estadoTachoOrganico = 0;
      Serial.print("Tacho plastico lleno");
      Serial.print("-");
      Serial.print(estadoTachoPlastico);
      Serial.print("-");
      Serial.print(estadoTachoMetal);
      Serial.print("-");
      Serial.print(estadoTachoOrganico);
      delay(500);
      
  }
  // si el tacho de metal esta lleno 

  if (digitalRead(pinInfraMetal) == LOW){
      estadoTachoPlastico = 0;
      estadoTachoMetal = 1;
      estadoTachoOrganico = 0;
      Serial.print("Tacho metal lleno");
      Serial.print("-");
      Serial.print(estadoTachoPlastico);
      Serial.print("-");
      Serial.print(estadoTachoMetal);
      Serial.print("-");
      Serial.print(estadoTachoOrganico);
      delay(500);
  }

  */

  if (digitalRead(pinInfraPrincipal) == LOW) {

    if (isMetal == HIGH) {

      Serial.print("ES METAL");
      Serial.print("-");
      Serial.print(estadoPlastico);
      Serial.print("-");
      Serial.print(estadoMetal);
      Serial.print("-");
      Serial.print(estadoOrganico);
      Serial.print("-");

      Serial.print(isMetal);
      Serial.print("-");
      Serial.println(isPlastico);

      servoMetal.write(90); // Se abre el metal

      delay(1000);
      servoEntrada.write(180); //Se abren las puertas

      delay(3000);
      servoEntrada.write(90); //Se cierran las puertas
      servoMetal.write(180);

      delay(2000);
    }

    else {

      digitalWrite(pinLaser, HIGH);
      delay(1000);
      valorLuz = analogRead(sensorPlastico);

      digitalWrite(pinLaser, LOW);

      if (valorLuz > 850) {

        isPlastico = 1;

        Serial.print("ES PLASTICO");
        Serial.print("-");
        Serial.print(estadoPlastico);
        Serial.print("-");
        Serial.print(estadoMetal);
        Serial.print("-");
        Serial.print(estadoOrganico);
        Serial.print("-");

        Serial.print(isMetal);
        Serial.print("-");
        Serial.println(isPlastico);
        
        servoPlastico.write(90); // Se abre el plastico

        delay(1000);
        servoEntrada.write(180); //Se abren las puertas

        delay(3000);
        servoEntrada.write(90); //Se cierran las puertas
        delay(500);
        servoPlastico.write(180);

        delay(2000);
      }

      else {

        Serial.print("ES ORGANICO");
        Serial.print("-");
        Serial.print(estadoPlastico);
        Serial.print("-");
        Serial.print(estadoMetal);
        Serial.print("-");
        Serial.print(estadoOrganico);
        Serial.print("-");

        Serial.print(isMetal);
        Serial.print("-");
        Serial.println(isPlastico);

        servoEntrada.write(180); //Se abren las puertas

        delay(3000);
        servoEntrada.write(90); //Se cierran las puertas

        delay(2000);
      }
    }

  }
  delay(1000);
}
