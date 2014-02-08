/**
 *
 * socialcounts - find social media strength of a URL
 * Copyright (c) 2014, Sandeep Gupta
 * 
 * http://sangupta.com/projects/socialcounts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.sangupta.socialcount;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.entity.ContentType;

import com.google.gson.FieldNamingPolicy;
import com.sangupta.jerry.http.WebInvoker;
import com.sangupta.jerry.http.WebRequest;
import com.sangupta.jerry.http.WebRequestMethod;
import com.sangupta.jerry.http.WebResponse;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.jerry.util.GsonUtils;
import com.sangupta.jerry.util.UriUtils;
import com.sangupta.socialcount.web.FacebookResponse;
import com.sangupta.socialcount.web.GoogleRequest;
import com.sangupta.socialcount.web.GoogleResponse;
import com.sangupta.socialcount.web.LinkedinResponse;
import com.sangupta.socialcount.web.PinterestResponse;
import com.sangupta.socialcount.web.TwitterResponse;

/**
 * Simple client to fetch the social share counts from various social
 * media sites for a given URL. The URL is canonicalized before sending
 * it for media shares.
 * 
 * The client is thread-safe and thus you can hit it as hard as you
 * want.
 * 
 * @author sangupta
 *
 */
public class SocialCountClient {
	
	/**
	 * An internal instance that is an ALL TRUE instance - that is all providers
	 * will be hit.
	 * 
	 */
	private static final SocialCountConfig ALL_TRUE_CONFIG = new SocialCountConfig();
	
	/**
	 * Find various social counts for the given URL. The returned object
	 * contains the various values. If a value is <code>-1</code>, it represents
	 * that something went wrong while fetching the value. All APIs are run
	 * sequentially one after another and thus may take time.
	 * 
	 * @param url
	 *            the url to analyze
	 * 
	 * @return the {@link SocialCounts} containing all metrics
	 */
	public static SocialCounts getSocialCounts(String url) {
		return getSocialCounts(url, ALL_TRUE_CONFIG);
	}
	
	/**
	 * Find various social counts for the given URL. This method allows you to
	 * control the providers that you want to hit.
	 * 
	 * The returned object contains the various values. If a value is
	 * <code>-1</code>, it represents that something went wrong while fetching
	 * the value. All APIs are run sequentially one after another and thus may
	 * take time.
	 * 
	 * @param url
	 *            the url to analyze
	 * 
	 * @param config
	 *            the provider configuration on which ones to hit
	 * 
	 * @return the {@link SocialCounts} containing all metrics
	 * 
	 */
	public static SocialCounts getSocialCounts(String url, SocialCountConfig config) {
		if(AssertUtils.isEmpty(url)) {
			throw new IllegalArgumentException("URL cannot be null/empty");
		}
		
		// prepare
		SocialCounts counts = new SocialCounts(url);

		// hit the various services one after another
		if(config.twitter) {
			getTwitterCount(counts);
		}
		
		if(config.facebook) {
			getFacebookCount(counts);
		}
		
		if(config.googlePlusOne) {
			getGooglePlusOneCount(counts);
		}
		
		if(config.googleShares) {
			getGoogleShares(counts);
		}

		if(config.linkedin) {
			getLinkedinCount(counts);
		}
		
		if(config.pinterest) {
			getPinterestCount(counts);
		}
		
		// mark complete - so that we can note down the time taken here
		counts.markComplete();
		
		return counts;
	}

	/**
	 * Compute the social counts from all providers, and return the metrics.
	 * The APIs are run in parallel to speed up the fetching of counts.
	 * 
	 * @param url
	 *            the url to analyze
	 * 
	 * @return the {@link SocialCounts} instance containing updated metrics
	 */
	public static SocialCounts getSocialCountsParallel(String url) {
		return getSocialCountsParallel(url, ALL_TRUE_CONFIG);
	}
	
	/**
	 * Compute the social counts from given chosen providers using the
	 * {@link SocialCountConfig} instance. The APIs are run in parallel to speed
	 * up the fetching of counts.
	 * 
	 * @param url
	 *            the url to analyze
	 * 
	 * @param config
	 *            the {@link SocialCountConfig} containing which providers to
	 *            hit
	 * 
	 * @return the {@link SocialCounts} instance containing updated metrics
	 * 
	 * @throws IllegalArgumentException
	 *             if the url is <code>null</code> or empty
	 */
	public static SocialCounts getSocialCountsParallel(String url, SocialCountConfig config) {
		if(AssertUtils.isEmpty(url)) {
			throw new IllegalArgumentException("URL cannot be null/empty");
		}
		
		// prepare
		final SocialCounts counts = new SocialCounts(url);
		
		// prepare a parallel execution thing
		ExecutorService pool = null;
		try {
			pool = Executors.newFixedThreadPool(6);
			List<Future<?>> list = new ArrayList<Future<?>>();
			
			// add tasks one by one
			if(config.facebook) {
				list.add(pool.submit(new Runnable() { @Override public void run() {  getFacebookCount(counts); } }));
			}
			if(config.twitter) {
				list.add(pool.submit(new Runnable() { @Override public void run() {  getTwitterCount(counts); } }));
			}
			if(config.googlePlusOne) {
				list.add(pool.submit(new Runnable() { @Override public void run() {  getGooglePlusOneCount(counts); } }));
			}
			if(config.googleShares) {
				list.add(pool.submit(new Runnable() { @Override public void run() {  getGoogleShares(counts); } }));
			}
			if(config.linkedin) {
				list.add(pool.submit(new Runnable() { @Override public void run() {  getLinkedinCount(counts); } }));
			}
			if(config.pinterest) {
				list.add(pool.submit(new Runnable() { @Override public void run() {  getPinterestCount(counts); } }));
			}
			
			// shutdown the pool
			pool.shutdown();
			
			// start reading
			for(Future<?> future : list) {
				try {
					future.get();
				} catch(Exception e) {
					// eat up
				}
			}
		} finally {
			if(pool != null && !pool.isShutdown()) {
				pool.shutdownNow();
			}
			
			counts.markComplete();
		}
		
		return counts;
	}

	/**
	 * Get linkedin shares for the given url.
	 * 
	 * @param counts
	 *            the {@link SocialCounts} instance to be updated and containing
	 *            the url
	 */
	public static void getLinkedinCount(SocialCounts counts) {
		String api = "http://www.linkedin.com/countserv/count/share?lang=en_US&callback=showCount&url=" + counts.encodedUri;
		WebResponse response = WebInvoker.getResponse(api);
		if(response == null || !response.isSuccess()) {
			return;
		}
		
		// remove the JSONP callback function name
		String json = response.getContent();
		json = json.substring("showCount(".length(), json.length() - 2);
		LinkedinResponse linkedin = GsonUtils.getGson().fromJson(json, LinkedinResponse.class);
		if(linkedin == null) {
			return;
		}
		
		counts.linkedinShares = linkedin.count;
	}

	/**
	 * Get pinterest pin count for the given url.
	 * 
	 * @param counts
	 *            the {@link SocialCounts} instance to be updated and containing
	 *            the url
	 */
	public static void getPinterestCount(SocialCounts counts) {
		String api = "http://api.pinterest.com/v1/urls/count.json?callback=showCount&url=" + counts.encodedUri;
		WebResponse response = WebInvoker.getResponse(api);
		if(response == null || !response.isSuccess()) {
			return;
		}
		
		// remove the JSONP callback function name
		String json = response.getContent();
		json = json.substring("showCount(".length(), json.length() - 1);
		PinterestResponse pinterest = GsonUtils.getGson().fromJson(json, PinterestResponse.class);
		if(pinterest == null) {
			return;
		}
		
		counts.pinterestPins = pinterest.count;
	}

	/**
	 * Find the Google Plus shares for a given url.
	 * 
	 * @param counts
	 *            the {@link SocialCounts} instance to be updated and containing
	 *            the url
	 */
	public static void getGooglePlusOneCount(SocialCounts counts) {
		String api = "https://clients6.google.com/rpc";
		GoogleRequest googleRequest = new GoogleRequest(counts.url);
		WebRequest request = WebInvoker.getWebRequest(api, WebRequestMethod.POST);
		request.addHeader("Content-type", "application/json");
		request.bodyString(googleRequest.toString(), ContentType.APPLICATION_JSON);
		
		WebResponse response = WebInvoker.executeSilently(request);
		if(response == null || !response.isSuccess()) {
			return;
		}
		
		GoogleResponse google = GsonUtils.getGson(FieldNamingPolicy.IDENTITY).fromJson(response.getContent(), GoogleResponse.class);
		if(google == null || google.result == null || google.result.metadata == null || google.result.metadata.globalCounts == null) {
			return;
		}
		
		counts.googlePlusOne = (long) google.result.metadata.globalCounts.count;
	}

	/**
	 * Find google+ shares for a given url. Note that this method is really slow
	 * because of the lack of a direct API from Google.
	 * 
	 * @param counts
	 *            the {@link SocialCounts} instance to be updated and containing
	 *            the url
	 */
	public static void getGoogleShares(SocialCounts counts) {
		String api = "https://plus.google.com/ripple/details?url=" + UriUtils.encodeURIComponent("http://facebook.com");
		WebResponse response = WebInvoker.getResponse(api);
		if(response == null || !response.isSuccess()) {
			return;
		}
		
		String st = response.getContent();
		int index = st.indexOf(" public shares");
		if(index <= 0) {
			return;
		}
		int end = st.substring(0, index).lastIndexOf("<div>");
		String count = st.substring(end + 5, index).trim();
		try {
			counts.googleShares = Long.parseLong(count);
		} catch(NumberFormatException e) {
			// eat up
		}
	}

	/**
	 * Find the facebook shares for a given URL.
	 * 
	 * Refer <a
	 * href="https://developers.facebook.com/docs/reference/fql/link_stat/">
	 * https://developers.facebook.com/docs/reference/fql/link_stat/</a> for
	 * more details.
	 * 
	 * @param counts
	 *            the {@link SocialCounts} instance to be updated and containing
	 *            the url
	 */
	public static void getFacebookCount(SocialCounts counts) {
		String query = "SELECT url, like_count, click_count, comment_count, share_count FROM link_stat WHERE url='" + counts.url + "'";
		String api = "http://graph.facebook.com/fql?q=" + UriUtils.encodeURIComponent(query);
		WebResponse response = WebInvoker.getResponse(api);
		if(response == null || !response.isSuccess()) {
			return;
		}

		FacebookResponse facebook = GsonUtils.getGson(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).fromJson(response.getContent(), FacebookResponse.class);
		if(facebook == null || facebook.data == null) {
			return;
		}
		
		counts.facebookShares = facebook.data[0].shareCount;
		counts.facebookComments = facebook.data[0].commentCount;
		counts.facebookLikes = facebook.data[0].likeCount;
		counts.facebookClicks = facebook.data[0].clickCount;
	}

	/**
	 * Find twitter shares for a given URL.
	 * 
	 * @param counts
	 *            the {@link SocialCounts} instance to be updated and containing
	 *            the url
	 */
	public static void getTwitterCount(SocialCounts counts) {
		String api = "https://cdn.api.twitter.com/1/urls/count.json?url=" + counts.encodedUri;
		WebResponse response = WebInvoker.getResponse(api);
		if(response == null || !response.isSuccess()) {
			return;
		}
		
		TwitterResponse twitter = GsonUtils.getGson().fromJson(response.getContent(), TwitterResponse.class);
		if(twitter == null) {
			return;
		}
		
		counts.twitter = twitter.count;
	}

}
