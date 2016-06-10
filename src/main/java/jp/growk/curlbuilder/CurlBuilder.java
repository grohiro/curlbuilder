package jp.growk.curlbuilder;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author grohiro
 */
public class CurlBuilder {

	private final String command;

	public CurlBuilder() {
		this("curl");
	}
	public CurlBuilder(String command) {
		this.command = command;
	}

	/**
	 * Return curl command
	 * 
	 * @throw CurlBuidlerException
	 * @return curl command
	 */
	public String command(HttpRequestBase request) {
		try {
			StringBuilder curl = new StringBuilder(this.command);

			String method = request.getMethod();

			curl.append(" -X ").append(method);

			// HTTP headers
			Header[] list = request.getAllHeaders();
			if (list.length > 0) {
				for (Header h : list) {
					curl.append(" -H '" + encodeURL(h.getName()) + ": " + h.getValue() + "'");
				}
			}

			// URI
			curl.append(" ").append(request.getURI());

			// curl command options
			if (method.equalsIgnoreCase("post")) {
				HttpPost post = (HttpPost) request;
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				if (post.getEntity() != null) {
					post.getEntity().writeTo(stream);
				}
				String query = stream.toString();
				if (query.length() > 0) {
					String[] params = query.split("&");
					for (String param : params) {
						curl.append(String.format(" -d '%s'", param));
					}
				}
			}
			
			return curl.toString();

		} catch (Throwable t) {
			throw new CurlBuilderException(t);
		}
	}

	/**
	 * URLエンコード.
	 * 
	 * @param s
	 * @return
	 */
	private static String encodeURL(String s) {
		String response = "";
		try {
			response = URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			response = e.getMessage();
		}
		return response;
	}
}
