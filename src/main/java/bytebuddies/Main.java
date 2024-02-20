package bytebuddies;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpResponse;

public class Main {

    private static final double gravitation = 6.67430e-11; //m^3/kg*s^2
    private static final double earthMass = 5.972e24; //kg
    private static final double earthRadius = 6371000; //m
    private static final double sLight = 299792458; //m/s
    private static final double UTCSyncConstant = 6.969290134e-10; //UTC sync constant
    private static final int internalClock = 60; //s

    private static double t = 0;
    private static int simRuns = 100;
    private static final String FILE_NAME = "coord_time.csv";

    public static void main(String[] args) {
        SatelliteTracker sT = new SatelliteTracker();

        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            File file = new File(FILE_NAME);
            System.out.println(file.getAbsolutePath());

            for (int i = 0; i < simRuns; i++) {
                HttpResponse<String> response = sT.getTracking();
                String responseString = response.body();

                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject positionObject = jsonObject.getJSONArray("positions").getJSONObject(0);
                double altitude = positionObject.getDouble("sataltitude")*1000; //m
                double orbitSpeed = Math.sqrt((gravitation*earthMass)/((earthRadius+altitude))); //m/s

                System.out.println(altitude);
                System.out.println(orbitSpeed);

                double discrepancyDelta = (1 - UTCSyncConstant + (gravitation*earthMass)/((altitude + earthRadius) * sLight*sLight) + (orbitSpeed*orbitSpeed)/(2*sLight*sLight))*internalClock;
                t += discrepancyDelta;
                writer.write(String.format("%.10f\n", t));
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
