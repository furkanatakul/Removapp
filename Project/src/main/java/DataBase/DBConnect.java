package DataBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class DBConnect {
	private Connection connection = null;

	public DBConnect() {
	}

	public Connection connectdb() {
		try {
			this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public void disconnect() {
	    try {
	        if (connection != null && !connection.isClosed()) {
	            connection.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	 public static void main(String[] args) {
	        Connection connection = null;
	        try {
	            DBConnect veritabaniBaglantisi = new DBConnect();
	            connection = veritabaniBaglantisi.connectdb();

	            if (connection != null) {
	            	String apiKey = APIKEY;
	                //URL url = new URL("https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey + "&page=" + page);
	                //URL url = new URL("https://api.themoviedb.org/3/movie/upcoming?api_key=" + apiKey);
	                URL url = new URL("https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey + "&with_genres=18");
	                //URL url = new URL("https://api.themoviedb.org/3/search/movie?api_key=" +apiKey+ "&query=Fermat");
	                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	                    conn.setRequestMethod("GET");

	                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                    StringBuilder response = new StringBuilder();
	                    String line;

	                    while ((line = reader.readLine()) != null) {
	                        response.append(line);
	                    }

	                    reader.close();
	                    conn.disconnect();

	                    ObjectMapper mapper = new ObjectMapper();
	                    JsonNode rootNode = mapper.readTree(response.toString());
	                    JsonNode resultsNode = rootNode.get("results");

	                    for (JsonNode filmNode : resultsNode) {
	                        String filmAdi = filmNode.get("title").asText();
	                        String aciklama = filmNode.get("overview").asText();
	                        String vizyonTarihi = filmNode.get("release_date").asText();
	                        String ulke = getUlke(filmNode.get("id").asInt(), apiKey);
	                        int filmSuresi = getFilmSuresi(filmNode.get("id").asInt(), apiKey);
	                        String oyuncular = getOyuncular(filmNode.get("id").asInt(), apiKey);
	                        String yonetmen = getYonetmen(filmNode.get("id").asInt(), apiKey);
	                        String posterPath = filmNode.get("poster_path").asText();
	                        String posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
	                        double filmPuani = filmNode.get("vote_average").asDouble();
	                        List<String> fragmanlar = getFragmanlar(filmNode.get("id").asInt(), apiKey);
	                        List<String> filmTurleri = getFilmTurleri(filmNode.get("id").asInt(), apiKey);
	                        if (filmExists(connection, filmAdi)) {
	                            updateFilm(connection, filmAdi, aciklama, vizyonTarihi, ulke, filmSuresi, oyuncular, posterUrl,
	                                    filmPuani, yonetmen, filmTurleri, fragmanlar);
	                        } else {
	                            insertFilm(connection, filmAdi, aciklama, vizyonTarihi, ulke, filmSuresi, oyuncular, posterUrl,
	                                    filmPuani, yonetmen, filmTurleri, fragmanlar);
	                        }
	                    }
	                    System.out.println("Movies have been successfully uploaded or updated to the database.");
	                
	            } else {
	                System.out.println("Database connection error!!");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        finally {
	            if(connection != null) {
	                try {
	                    connection.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    public static boolean filmExists(Connection connection, String filmAdi) throws SQLException {
	        String query = "SELECT COUNT(*) AS count FROM filmler WHERE film_adi = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, filmAdi);
	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    return resultSet.getInt("count") > 0;
	                }
	            }
	        }
	        return false;
	    }

	    public static void updateFilm(Connection connection, String filmAdi, String aciklama, String vizyonTarihi,
	            String ulke, int filmSuresi, String oyuncular, String posterUrl, double filmPuani, String yonetmen,
	            List<String> filmTurleri, List<String> fragmanlar) throws SQLException {
	        String query = "UPDATE filmler SET aciklama = ?, vizyon_tarihi = ?, ulke = ?, film_suresi = ?, oyuncular = ?, film_gorselleri = ?, film_puani = ?, yonetmen = ?, tur = ?, fragmanlar = ? WHERE film_adi = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, aciklama);
	            preparedStatement.setString(2, vizyonTarihi);
	            preparedStatement.setString(3, ulke);
	            preparedStatement.setInt(4, filmSuresi);
	            preparedStatement.setString(5, oyuncular);
	            preparedStatement.setString(6, posterUrl);
	            preparedStatement.setDouble(7, filmPuani);
	            preparedStatement.setString(8, yonetmen);
	            preparedStatement.setString(9, String.join(",", filmTurleri));
	            preparedStatement.setString(10, String.join(",", fragmanlar));
	            preparedStatement.setString(11, filmAdi);
	            preparedStatement.executeUpdate();
	        }
	    }

	    public static void insertFilm(Connection connection, String filmAdi, String aciklama, String vizyonTarihi,
	            String ulke, int filmSuresi, String oyuncular, String posterUrl, double filmPuani, String yonetmen,
	            List<String> filmTurleri, List<String> fragmanlar) throws SQLException {
	        String query = "INSERT INTO filmler (film_adi, aciklama, vizyon_tarihi, ulke, film_suresi, oyuncular, film_gorselleri, film_puani, yonetmen, tur, fragmanlar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, filmAdi);
	            preparedStatement.setString(2, aciklama);
	            preparedStatement.setString(3, vizyonTarihi);
	            preparedStatement.setString(4, ulke);
	            preparedStatement.setInt(5, filmSuresi);
	            preparedStatement.setString(6, oyuncular);
	            preparedStatement.setString(7, posterUrl);
	            preparedStatement.setDouble(8, filmPuani);
	            preparedStatement.setString(9, yonetmen);
	            preparedStatement.setString(10, String.join(",", filmTurleri));
	            preparedStatement.setString(11, String.join(",", fragmanlar));
	            preparedStatement.executeUpdate();
	        }
	    }

	    public static List<String> getFilmTurleri(int filmId, String apiKey) throws Exception {
	        URL url = new URL("https://api.themoviedb.org/3/movie/" + filmId + "?api_key=" + apiKey);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode rootNode = mapper.readTree(response.toString());
	            JsonNode genresNode = rootNode.get("genres");

	            List<String> filmTurleri = new ArrayList<>();
	            for (JsonNode genreNode : genresNode) {
	                String tur = genreNode.get("name").asText();
	                filmTurleri.add(tur);
	            }
	            return filmTurleri;
	        } finally {
	            con.disconnect(); 
	        }
	    }

	    public static String getUlke(int filmId, String apiKey) throws Exception {
	        URL url = new URL("https://api.themoviedb.org/3/movie/" + filmId + "?api_key=" + apiKey);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode rootNode = mapper.readTree(response.toString());
	            JsonNode productionCountriesNode = rootNode.get("production_countries");
	            String ulke = "";
	            if (productionCountriesNode != null && productionCountriesNode.isArray()
	                    && productionCountriesNode.size() > 0) {
	                ulke = productionCountriesNode.get(0).get("name").asText();
	            }
	            return ulke;
	        } finally {
	            con.disconnect(); 
	        }
	    }

	    public static int getFilmSuresi(int filmId, String apiKey) throws Exception {
	        URL url = new URL("https://api.themoviedb.org/3/movie/" + filmId + "?api_key=" + apiKey);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode rootNode = mapper.readTree(response.toString());
	            int filmSuresi = 0;
	            JsonNode runtimeNode = rootNode.get("runtime");
	            if (runtimeNode != null && !runtimeNode.isNull()) {
	                filmSuresi = runtimeNode.asInt();
	            }
	            return filmSuresi;
	        } finally {
	            con.disconnect(); 
	        }
	    }

	    public static String getOyuncular(int filmId, String apiKey) throws Exception {
	        URL url = new URL("https://api.themoviedb.org/3/movie/" + filmId + "/credits?api_key=" + apiKey);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode rootNode = mapper.readTree(response.toString());
	            JsonNode castNode = rootNode.get("cast");

	            StringBuilder oyuncular = new StringBuilder();
	            for (JsonNode actorNode : castNode) {
	                String oyuncuAdi = actorNode.get("name").asText();
	                oyuncular.append(oyuncuAdi).append(", ");
	            }
	            return oyuncular.toString();
	        } finally {
	            con.disconnect(); 
	        }
	    }

	    public static String getYonetmen(int filmId, String apiKey) throws Exception {
	        URL url = new URL("https://api.themoviedb.org/3/movie/" + filmId + "/credits?api_key=" + apiKey);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode rootNode = mapper.readTree(response.toString());

	            String yonetmen = "";
	            JsonNode crewNode = rootNode.get("crew");
	            if (crewNode != null) {
	                for (JsonNode node : crewNode) {
	                    if ("Director".equals(node.get("job").asText())) {
	                        yonetmen = node.get("name").asText();
	                        break;
	                    }
	                }
	            }
	            return yonetmen;
	        } finally {
	            con.disconnect(); 
	        }
	        
	    }
	    public static List<String> getFragmanlar(int filmId, String apiKey) throws Exception {
	        List<String> fragmanlar = new ArrayList<>();
	        URL url = new URL("https://api.themoviedb.org/3/movie/" + filmId + "/videos?api_key=" + apiKey);
	        HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");

	        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
	            String inputLine;
	            StringBuilder response = new StringBuilder();
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }

	            ObjectMapper mapper = new ObjectMapper();
	            JsonNode rootNode = mapper.readTree(response.toString());
	            JsonNode resultsNode = rootNode.get("results");

	            for (JsonNode result : resultsNode) {
	                String type = result.get("type").asText();
	                if ("Trailer".equals(type)) {
	                    String key = result.get("key").asText();
	                    String site = result.get("site").asText();
	                    if ("YouTube".equals(site)) {
	                        fragmanlar.add("https://www.youtube.com/watch?v=" + key);
	                    }
	                }
	            }
	        } finally {
	            con.disconnect(); 
	        }
	        return fragmanlar;
	    }
	}