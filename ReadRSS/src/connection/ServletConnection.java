package connection;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.galaksiya.education.rss.feed.RSSReader;

public class ServletConnection {
	private static final Logger log = Logger.getLogger(RSSReader.class);

	public void postRequest(String title, String link, Date date, String method) {

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost("http://localhost:7070/feeds");
			JSONObject json = new JSONObject();
			json.put("title", title);
			json.put("link", link);
			json.put("date", date);
			json.put("method", method);
			StringEntity input = new StringEntity(json.toString(), "UTF-8");
			input.setContentType("application/json");
			postRequest.setEntity(input);
			httpClient.execute(postRequest);

			httpClient.getConnectionManager().shutdown();

		} catch (IOException e) {

			log.info("classic I/O exception", e);

		}

	}

}
