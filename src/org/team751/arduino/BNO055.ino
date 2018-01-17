/*
 * Aidan Chandra 1/16/18 achandra19@priorypanther.com
 * 
 * Serial baudrate: 9600
 * 
 * The following functions are meant to be used with the 
 * Adafruit BNO055 breakout
 * 
 * The following functions are co-dependent (you cannot copy and paste just one of them)
 *    initializeIMU() & recal()
 *    
 *    
 * The following Arduino program is meant to test the BNO055 and provide copy-and-paste   
 *  functions to implement it for other programs
 *  
 * Startup
 *  initialize
 * Loop:
 *  print out the calibration data
 *  print out the temperature
 *  print out the x orientation
 *  
 *  if 'c' is sent through the serial monitor, the sensor will recalibrate. See the recal()
 *    function for notes
 *    
 *
 */

//Required Libraries
//Copy and paste the following to the top of your file
  #include <Wire.h>
  #include <Adafruit_Sensor.h>
  #include <Adafruit_BNO055.h>
  #include <utility/imumaths.h>
  Adafruit_BNO055 bno = Adafruit_BNO055();
//End here

void setup() {
  delay(1000);
  Serial.begin(9600);
  
  initializeIMU(true);
}

void loop() {
  delay(1000);
  
  getCalibrationData(true);
  getTemperature(true);
  getOrientation(true);
  
  if(Serial.read() == 'c')
    recal(true);
}

/*
 * Reclibrates the IMU - MUST BE KEPT ABSOLUTELY STILL
 * 
 */
void recal(boolean debug){
  if(debug)
    Serial.println("Recalibrating... HOLD STILL");
  initializeIMU(false);
}

/*
 * Returns an integer representing the current calibration
 *  status of the gyroscope (the only subsensor neccessary
 *  for getOrientation()
 *  
 *  0: Uncailbrated - do not use data
 *  1: Calibrated
 *  2: Well calibrated
 *  3: Very well calibrated - expect repeatable readings 
 *     with +/- 1.5 degree variability
 */
int getCalibrationData(boolean debug){
  uint8_t system, gyro, accel, mag = 0;
  bno.getCalibration(&system, &gyro, &accel, &mag);
  if(debug){
    Serial.println("Getting Calibration Data");
    Serial.print("    ");
    Serial.println(gyro, DEC);
  }
  return int(gyro);
}


/*
 * Returns the current orientation in degrees
 * debug true will print out verbose debug messages
 * 
 * Note: A delay is reccommended if this function will be
 *  called in quick succession. No more than 100ms is neccessary
 */
float getOrientation(boolean debug){
  imu::Vector<3> euler = bno.getVector(Adafruit_BNO055::VECTOR_EULER);
  if(debug)
    Serial.println("Getting orientation (X)");
  if(debug)
    Serial.println("    " + String(euler.x()));
  return (float)euler.x();
}


/*
 * Returns the temperature in degress C 
 * debug true will print out verbose debug messages
 */
int getTemperature(boolean debug){
  if(debug)
    Serial.println("Getting temperature");
  int8_t temp = bno.getTemp();
  if(debug)
    Serial.println("    " + String(temp));
  return temp;
}


/*
 * Intitializes the IMU. MUST BE CALLED FIRST
 * debug true will print out verbose debug messages
 * Returns true for success, false for failure
 * 
 * NOTE: Initializing or reinitializing the sensor RESETS THE POSITION TO 0.
 *        IF YOUR SENSOR WAS READING "340" AND YOU RECALIBRATE, 340 WILL NOW BE
 *        0, EVEN IF YOU DID NOT MOVE THE SENSOR.
 */
boolean initializeIMU(boolean debug){
  if(debug)
    Serial.println("Initializing BNO055");
  if(!bno.begin()){
    Serial.println("    Failed");
    return false;
  }
  if(debug) Serial.println("    Success");
  //bno.setExtCrystalUse(true);
  return true;
}

