import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApiClient {
    private static final String API_KEY = "d9ca55e64b5ec91bb05c125e3ac4b844"; // Replace with your API key
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        String city = "London"; // You can change the city name

        try {
            String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
            conn.disconnect();

            parseAndDisplayWeatherData(response.toString());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private static void parseAndDisplayWeatherData(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        String cityName = jsonObject.getString("name");
        JSONObject main = jsonObject.getJSONObject("main");
        double temperature = main.getDouble("temp");
        double feelsLike = main.getDouble("feels_like");
        int humidity = main.getInt("humidity");

        JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("Weather in " + cityName + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Feels Like: " + feelsLike + "°C");
        System.out.println("Description: " + description);
        System.out.println("Humidity: " + humidity + "%");
    }
}
