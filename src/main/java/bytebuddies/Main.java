package bytebuddies;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.http.HttpResponse;

public class Main {

    private static final double gravitation = 6.67430e-11; //m^3/kg*s^2
    private static final double earthMass = 5.972e24; //kg
    private static final double earthRadius = 6371000; //m
    private static final double sLight = 299792458; //m/s
    private static final double UTCSyncConstant = 6.969290134e-10; //UTC sync constant
    private static final int internalClock = 60; //s

    private static double t = 0;
    private static int simRuns = 1440;
    private static final String FILE_NAME = "coord_time_CSV.csv";

    public static void main(String[] args) {
        SatelliteTracker sT = new SatelliteTracker();

        try {
            FileWriter writer = new FileWriter(FILE_NAME);

            BufferedReader altitudeReader = new BufferedReader(new FileReader("h_data.csv"));
            BufferedReader velocityReader = new BufferedReader(new FileReader("v_data.csv"));

            for (int i = 0; i < simRuns; i++) {

                //With the API data you can only have like 800 api calls per hour, I need 1440.
                /*
                HttpResponse<String> response = sT.getTracking();
                String responseString = response.body();

                JSONObject jsonObject = new JSONObject(responseString);
                JSONObject positionObject = jsonObject.getJSONArray("positions").getJSONObject(0);
                double h = positionObject.getDouble("sataltitude")*1000; //m
                double v = Math.sqrt((gravitation*earthMass)/((earthRadius+h))); //m/s

                System.out.println(h);
                System.out.println(v);
                */

                double h = Double.parseDouble(altitudeReader.readLine());
                double v = Double.parseDouble(velocityReader.readLine());

                double discrepancyDelta = (1 - UTCSyncConstant + (gravitation*earthMass)/((h + earthRadius) * sLight*sLight) + (v*v)/(2*sLight*sLight))*internalClock;
                t += discrepancyDelta;
                writer.write(String.format("%.10f\n", t));
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
