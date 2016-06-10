package jp.growk.curlbuilder;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;
import org.junit.Test;

public class CurlBuilderTest {
	
	CurlBuilder curlBuilder;
	
	HttpGet get;
	
	HttpPost post;
	EntityBuilder entityBuilder;
	
	URI localhost;

	/**
	 * setup
	 * @throws URISyntaxException 
	 */
	@Before
	public void setUp() throws URISyntaxException {
		curlBuilder = new CurlBuilder();
		get = new HttpGet();
		post = new HttpPost();
		entityBuilder = EntityBuilder.create();
		localhost = new URI("http://localhost/index.html");
	}

	/**
	 * GET http://localhost/index.html
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public void testSimpleGetRequest() throws URISyntaxException {
		get.setURI(localhost);
		String command = curlBuilder.command(get);
		assertEquals("curl -X GET http://localhost/index.html", command);
	}

	/**
	 * HTTP headers
	 */
	@Test
	public void testGetWithHeader() {
		get.setURI(localhost);
		get.setHeader("Content-Type", "application/json");;
		get.setHeader("X-USER-ID", "foobar");
		get.setHeader("X-USER-NAME", "Tanaka Taro");
		String command = curlBuilder.command(get);
		assertEquals("curl -X GET -H 'Content-Type: application/json' -H 'X-USER-ID: foobar' -H 'X-USER-NAME: Tanaka Taro' http://localhost/index.html", command);
		
	}
	
	/**
	 * POST
	 */
	@Test
	public void testSimplePostRequest() {
		post.setURI(localhost);
		String command = curlBuilder.command(post);
		assertEquals("curl -X POST http://localhost/index.html", command);
	}
	
	/**
	 * 
	 */
	@Test
	public void testPostRequest() {
		post.setURI(localhost);
		entityBuilder.setParameters(map("email", "foo@example.com"), map("password", "bar"));
		post.setEntity(entityBuilder.build());
		String command = curlBuilder.command(post);
		assertEquals("curl -X POST http://localhost/index.html -d 'email=foo%40example.com' -d 'password=bar'", command);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	private static final NameValuePair map(String name, String value) {
		return new BasicNameValuePair(name, value);
	}
	
}
