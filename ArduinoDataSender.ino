#include <MillisTimer.h>

unsigned long requestID;
char requestToken = '-'; // Character used to separate command from ID number.

MillisTimer timer = MillisTimer(10);

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(10);
  timer.setInterval(1000);
  timer.expiredHandler(myTimerFunction);
  timer.setRepeats(3); // Resend 3 times.
  timer.start();
}

void loop() {
  if (Serial.available() > 0) { // If the RoboRIO has sent a query command...
    String request = Serial.readString();
    Serial.println(request);
    if (request.charAt(0) == 'Q' && request.charAt(1) == '-') {
      request = request.substring(request.indexOf("-") + 1);
      requestID = request.toInt();
      Serial.println(requestID);
      for (int i = 0; i < 3; i++) { // Want to send the data 3 times if RoboRIO doesn't send back "OK-###"
        sendData();
      }
    }
  }
}

void myTimerFunction(MillisTimer &mt)
{
  Serial.print(Constant("Repeat: "));
  Serial.println(mt.getRemainingRepeats());
  timer.run();
}

void sendData() { <<IN PROGRESS>>
  while (!Serial.readString().equals("OK-" + requestID)) { // While the RoboRIO hasn't sent the right verification token. 
    Serial.print(requestID);
    Serial.print("-");
    Serial.print(getRotation());
    Serial.print("-");
    Serial.print(getDistanceTraveled());
    Serial.print("-");
    Serial.println(getVelocity());
    delay(2000);
  }
}

int getRotation() {

}

int getDistanceTraveled() {

}

int getVelocity() {

}
